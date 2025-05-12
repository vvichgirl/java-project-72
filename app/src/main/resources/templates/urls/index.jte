@import dto.urls.UrlsPage
@import util.NamedRoutes
@import java.time.format.DateTimeFormatter
@import java.util.Date
@param UrlsPage page

@template.layout.page(

    page = page,
    content = @`
        <div class="container-lg mt-5">
          <h1>Сайты</h1>
          <table class="table table-bordered table-hover mt-3">
            <thead>
              <tr>
                <th class="col-1">ID</th>
                <th>Имя</th>
                <th class="col-2">Последняя проверка</th>
                <th class="col-1">Код ответа</th>
              </tr>
            </thead>
            <tbody>
              @for(var url : page.getUrls())
                <tr>
                  <td>
                    ${url.getId()}
                  </td>
                  <td>
                    <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                  </td>
                  @if(page.getLastChecks() != null && page.getLastChecks().get(url.getId()) != null)
                    <td>
                      !{DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        String dateCheck = dtf.format(page.getLastChecks().get(url.getId()).getCreatedAt().toLocalDateTime());}
                      ${dateCheck}
                    </td>
                    <td>
                      ${page.getLastChecks().get(url.getId()).getStatusCode()}
                    </td>
                  @else
                    <td>
                    </td>
                    <td>
                    </td>
                  @endif
                </tr>
              @endfor
            </tbody>
          </table>
        </div>
    `,
)
