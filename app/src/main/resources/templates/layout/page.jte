@import gg.jte.Content
@import dto.BasePage
@import util.NamedRoutes
@param Content content
@param BasePage page = null

<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
            crossorigin="anonymous">
    <title>Анализатор страниц</title>
    </head>
    <body class="d-flex flex-column min-vh-100">
      <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
          <a class="navbar-brand" href="/">Анализатор страниц</a>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarNav">
            <div class="navbar-nav">
              <a class="nav-link" href="/">Главная</a>
              <a class="nav-link" href="${NamedRoutes.urlsPath()}">Сайты</a>
            </div>
          </div>
        </div>
      </nav>

       @if(page != null && page.getFlash() != null)
         <div class="rounded-0 m-0 alert alert-dismissible fade show alert-${page.getFlashType()}" role="alert">
           <p class="m-0">${page.getFlash()}</p>
           <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
         </div>
       @endif

        <main class="flex-grow-1">
          ${content}
        </main>
    </body>
</html>
