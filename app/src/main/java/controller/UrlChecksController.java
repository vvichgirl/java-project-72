package controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import model.Url;
import model.UrlCheck;
import repository.UrlChecksRepository;
import repository.UrlsRepository;
import util.NamedRoutes;

import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UrlChecksController {

    public static void create(Context ctx) throws SQLException {
        Long idUrl = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlsRepository.getUrlById(idUrl)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        String urlTitle = url.getName();

        try {
            HttpResponse<String> result = Unirest.get(urlTitle).asString();
            int statusCode = result.getStatus();
            String body = result.getBody();

            Document html = Jsoup.parse(body);
            String title = html.title();
            String h1 = html.selectFirst("h1") == null ? "" : html.selectFirst("h1").text();
            String description = html.select("meta[name=description]").attr("content");

            UrlCheck urlCheckObj = new UrlCheck(statusCode, title, h1, description, idUrl);
            UrlChecksRepository.save(urlCheckObj);

            ctx.redirect(NamedRoutes.urlPath(idUrl));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.urlPath(idUrl));
        }

    }

}
