package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

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
    private static MockWebServer server;
    private static String baseUrl;

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", fileName)
                .toAbsolutePath().normalize();
    }

    private static String readFixture(String fileName) throws IOException {
        var path = getFixturePath(fileName);
        return Files.readString(path).trim();
    }

    @BeforeAll
    public static void startServer() throws IOException {
        String resultHtml = readFixture("index.html");
        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(resultHtml));
        server.start();
    }

    @BeforeEach
    public final void setUp() {
        app = App.getApp();
    }

    @AfterAll
    public static void shutDown() throws IOException {
        server.shutdown();
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
        Date currentDate = new Date();
        Timestamp createdAt = new Timestamp(currentDate.getTime());
        var url = new Url("https://www.example.com", createdAt);
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
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
            assertEquals(1, UrlsRepository.getEntities().size());
        });
    }

    @Test
    public void testExistUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);

            var requestBodyExist = "url=https://www.example.com/test/1";
            var responseExist = client.post(NamedRoutes.urlsPath(), requestBodyExist);

            assertEquals(1, UrlsRepository.getEntities().size());
        });
    }

    @Test
    public void testCreateIncorrectUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=incorrect";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertEquals(0, UrlsRepository.getEntities().size());
        });
    }

    @Test
    public void testCreateUrlCheck() throws SQLException {
        baseUrl = server.url("/").toString();
        Date currentDate = new Date();
        Timestamp createdAt = new Timestamp(currentDate.getTime());
        var url = new Url(baseUrl, createdAt);
        UrlsRepository.save(url);
        var id = url.getId();

        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlCheck(id));
            assertThat(response.code()).isEqualTo(200);
            var list = UrlChecksRepository.findEntitiesByUrlId(id);
            var urlCheck = list.get(0);
            assertThat(urlCheck.getStatusCode()).isEqualTo(200);
            assertThat(urlCheck.getDescription()).isEqualTo("Page Analyzer – сайт, который анализирует указанные страницы на SEO пригодность.");
            assertThat(urlCheck.getTitle()).isEqualTo("Page Analyzer");
            assertThat(urlCheck.getH1()).isEqualTo("Анализатор страниц");
        });
    }

}
