# crispy-octo-eureka
this is my first java spring project. It is Coding Sharing platform.I will upgrate it in the future
in this project i used h2 database and spring boot fraemwork
below avaliable commands:

/code/new - return page of creating code snippet
/api/code/new - taking JSON with code snippet to save it to the database and returning uuid of current snippet

/code/{uuid} - get page with snippet by uuid
/code/latest - get page with 10 latest and unrestricted snippets

/api/code/uuid - get JSON with code snippet
/api/code/latest - get array of JSONs (10 latest snippets)
