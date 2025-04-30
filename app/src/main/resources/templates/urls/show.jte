@import dto.urls.UrlPage
@import util.NamedRoutes
@import java.time.format.DateTimeFormatter
@import java.util.Date
@param UrlPage page

@template.layout.page(

    page = page,
    content = @`
        <section>
          <div class="container-lg mt-5">
            <h1>Сайт: ${page.getUrl().getName()}</h1>
            <table class="table table-bordered table-hover mt-3">
              <tbody>
                <tr>
                  <td>ID</td>
                  <td>
                    ${page.getUrl().getId()}
                  </td>
                </tr>
                <tr>
                  <td>Имя</td>
                  <td>
                    ${page.getUrl().getName()}
                  </td>
                </tr>
                <tr>
                  <td>Дата создания</td>
                  <td>
                    !{DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                      String date = dtf.format(page.getUrl().getCreatedAt().toLocalDateTime());}
                    ${date}
                  </td>
                </tr>
              </tbody>
            </table>
            <h2 class="mt-5">Проверки</h2>
            <form method="post" action="${NamedRoutes.urlCheck(page.getUrl().getId())}">
              <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
          </div>
        </section>
    `,
)