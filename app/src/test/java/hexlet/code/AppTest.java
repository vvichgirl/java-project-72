package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Url;
import repository.UrlsRepository;
import repository.UrlChecksRepository;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import util.NamedRoutes;

public class AppTest {

    private static Javalin app;
    private static MockWebServer mockServer;
    private static String baseUrl;

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }

    private static String readFixture(String fileName) throws IOException {
        var path = getFixturePath(fileName);
        return Files.readString(path).trim();
    }

    @BeforeAll
    public static void startServer() throws IOException {
        String resultHtml = readFixture("index.html");
        mockServer = new MockWebServer();
        mockServer.enqueue(new MockResponse().setBody(resultHtml));
        mockServer.start();
    }

    @BeforeEach
    public final void setUp() {
        app = App.getApp();
    }

    @AfterAll
    public static void shutDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        Url url = new Url("https://www.example.com");
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("999"));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCreateUrl() {
        String inputUrl = "https://www.example.com";

        JavalinTest.test(app, (server, client) -> {
            String requestBody = "url=" + inputUrl;
            var response = client.post(NamedRoutes.urlsPath(), requestBody);

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(inputUrl);

            var actualUrl = UrlsRepository.getUrlByName(inputUrl);
            assertThat(actualUrl).isNotNull();
        });
    }

    @Test
    public void testExistUrl() {
        String inputUrl = "https://www.example.com";
        String inputUrlExist = "https://www.example.com/test/1";

        JavalinTest.test(app, (server, client) -> {
            String requestBody = "url=" + inputUrl;
            var response = client.post(NamedRoutes.urlsPath(), requestBody);

            assertEquals(1, UrlsRepository.getEntities().size());

            String requestBodyExist = "url=" + inputUrlExist;
            var responseExist = client.post(NamedRoutes.urlsPath(), requestBodyExist);

            assertEquals(1, UrlsRepository.getEntities().size());
        });
    }

    @Test
    public void testCreateIncorrectUrl() {
        String inputUrl = "incorrect";

        JavalinTest.test(app, (server, client) -> {
            String requestBody = "url=" + inputUrl;
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertEquals(0, UrlsRepository.getEntities().size());
        });
    }

    @Test
    public void testCreateUrlCheck() throws SQLException {
        baseUrl = mockServer.url("/").toString().replaceAll("/$", "");
        var url = new Url(baseUrl);
        UrlsRepository.save(url);
        var id = url.getId();

        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlCheck(id));
            assertThat(response.code()).isEqualTo(200);
            var list = UrlChecksRepository.getChecksByUrlId((id));
            var urlCheck = list.get(0);
            assertThat(urlCheck).isNotNull();
            assertThat(urlCheck.getStatusCode()).isEqualTo(200);
            assertThat(urlCheck.getDescription()).isEqualTo(
                    "Page Analyzer – сайт, который анализирует "
                    + "указанные страницы на SEO пригодность.");
            assertThat(urlCheck.getTitle()).isEqualTo("Page Analyzer");
            assertThat(urlCheck.getH1()).isEqualTo("Анализатор страниц");
        });
    }
}
