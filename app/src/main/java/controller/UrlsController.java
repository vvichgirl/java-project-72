package controller;

import dto.urls.UrlPage;
import dto.urls.UrlsPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import model.Url;
import model.UrlCheck;
import repository.UrlChecksRepository;
import repository.UrlsRepository;
import util.NamedRoutes;

import static io.javalin.rendering.template.TemplateUtil.model;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UrlsController {
    public static void create(Context ctx) throws SQLException {
        try {
            var urlParam = ctx.formParam("url").trim().toLowerCase();
            URI uri = new URI(urlParam);
            URL url = uri.toURL();
            String protocol = url.getProtocol();
            String host = url.getHost();
            Integer port = url.getPort();
            String portValue = port == -1 ? "" : ":" + port;

            String urlTitle = String.format("%s://%s%s", protocol, host, portValue);

            if (UrlsRepository.existUrl(urlTitle)) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flash-type", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                Url urlObj = new Url(urlTitle);
                UrlsRepository.save(urlObj);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.sessionAttribute("flash-type", "success");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (URISyntaxException | IllegalArgumentException | MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlsRepository.getEntities();
        Map<Long, UrlCheck> lastChecks = UrlChecksRepository.getLastChecks();
        UrlsPage page = new UrlsPage(urls, lastChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlsRepository.getUrlById(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        List<UrlCheck> checks = UrlChecksRepository.getChecksByUrlId(id);
        UrlPage page = new UrlPage(url, checks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", model("page", page));
    }

}
