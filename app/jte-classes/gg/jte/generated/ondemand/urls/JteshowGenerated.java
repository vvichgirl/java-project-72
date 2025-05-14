package gg.jte.generated.ondemand.urls;
import dto.urls.UrlPage;
import util.NamedRoutes;
import java.time.format.DateTimeFormatter;
import java.util.Date;
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,4,4,6,6,10,10,13,13,13,19,19,19,25,25,25,31,32,32,33,33,33,39,39,39,39,39,39,39,39,39,54,54,57,57,57,60,60,60,63,63,63,66,66,66,69,69,69,72,72,73,73,73,76,76,81,81,81,81,81,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n        <section>\r\n          <div class=\"container-lg mt-5\">\r\n            <h1>Сайт: ");
				jteOutput.setContext("h1", null);
				jteOutput.writeUserContent(page.getUrl().getName());
				jteOutput.writeContent("</h1>\r\n            <table class=\"table table-bordered table-hover mt-3\">\r\n              <tbody>\r\n                <tr>\r\n                  <td>ID</td>\r\n                  <td>\r\n                    ");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(page.getUrl().getId());
				jteOutput.writeContent("\r\n                  </td>\r\n                </tr>\r\n                <tr>\r\n                  <td>Имя</td>\r\n                  <td>\r\n                    ");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(page.getUrl().getName());
				jteOutput.writeContent("\r\n                  </td>\r\n                </tr>\r\n                <tr>\r\n                  <td>Дата создания</td>\r\n                  <td>\r\n                    ");
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                      String date = dtf.format(page.getUrl().getCreatedAt().toLocalDateTime());
				jteOutput.writeContent("\r\n                    ");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(date);
				jteOutput.writeContent("\r\n                  </td>\r\n                </tr>\r\n              </tbody>\r\n            </table>\r\n            <h2 class=\"mt-5\">Проверки</h2>\r\n            <form method=\"post\"");
				var __jte_html_attribute_0 = NamedRoutes.urlCheck(page.getUrl().getId());
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" action=\"");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("form", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(">\r\n              <button type=\"submit\" class=\"btn btn-primary\">Запустить проверку</button>\r\n            </form>\r\n            <table class=\"table table-bordered table-hover mt-3\">\r\n              <thead>\r\n                <tr>\r\n                  <th class=\"col-1\">ID</th>\r\n                  <th class=\"col-1\">Код ответа</th>\r\n                  <th>title</th>\r\n                  <th>h1</th>\r\n                  <th>description</th>\r\n                  <th class=\"col-2\">Дата проверки</th>\r\n                </tr>\r\n              </thead>\r\n              <tbody>\r\n              ");
				for (var check : page.getChecks()) {
					jteOutput.writeContent("\r\n                <tr>\r\n                  <td>\r\n                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getId());
					jteOutput.writeContent("\r\n                  </td>\r\n                  <td>\r\n                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getStatusCode());
					jteOutput.writeContent("\r\n                  </td>\r\n                  <td>\r\n                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getTitle());
					jteOutput.writeContent("\r\n                  </td>\r\n                  <td>\r\n                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getH1());
					jteOutput.writeContent("\r\n                  </td>\r\n                  <td>\r\n                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getDescription());
					jteOutput.writeContent("\r\n                  </td>\r\n                  <td>\r\n                    ");
					String dateCheck = dtf.format(check.getCreatedAt().toLocalDateTime());
					jteOutput.writeContent("\r\n                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(dateCheck);
					jteOutput.writeContent("\r\n                  </td>\r\n                </tr>\r\n              ");
				}
				jteOutput.writeContent("\r\n              </tbody>\r\n            </table>\r\n          </div>\r\n        </section>\r\n    ");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlPage page = (UrlPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
