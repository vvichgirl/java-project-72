package controller;

import dto.urls.UrlPage;
import dto.urls.UrlsPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import model.Url;
import repository.UrlsRepository;
import util.NamedRoutes;

import static io.javalin.rendering.template.TemplateUtil.model;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

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

            Date currentDate = new Date();
            Timestamp createdAt = new Timestamp(currentDate.getTime());

            if (UrlsRepository.existUrl(urlTitle)) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flash-type", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                Url urlObj = new Url(urlTitle, createdAt);
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
        var urls = UrlsRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void check(Context ctx)  {

    }
}
