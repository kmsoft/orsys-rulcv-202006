# Formation Micro-services en Java

## Liens utiles

Les 7 articles de NGINX sur les microservices, par Chris Richardson (auteur connu dans le domaine des micro-services): https://www.nginx.com/blog/introduction-to-microservices/

10 bonnes pratiques pour écrire des APIs REST: https://medium.com/@mwaysolutions/10-best-practices-for-better-restful-api-cbe81b06f291

Vidéo d'un exemple de désastre d'utilisation des micro-services: https://www.youtube.com/watch?v=gfh-VCTwMw8

À propos de la gestion des pannes, article sur Hystrix (lib Java de gestion de panne apparue avant MicroProfile): https://github.com/Netflix/Hystrix/wiki

## Glossaire

- _CDI_ : Context and Dependency Injection.
  
  La solution JEE d'injection de dépendance. Définit les annotations comme `@Inject` et `@ApplicationScoped`.

- _JAX-RS_: Java API for RESTful Web Services.

  La solution JEE pour écrire des APIs REST. Définit les annotations comme `@Path`, `@GET` et les classes comme `Response`, `NotFoundException`.

- _OpenAPI_: spécification pour documenter les APIs REST sous la forme d'un document JSON ou YAML. Quarkus est capable de générer un tel document.