# Quizz REST

Plusieurs réponses possibles

------

## REST :

- est un style d'architecture ?
- est un protocole ?
- signifie "Resource Transfer" ?
- signifie "Representational State Transfer" ?

------

## REST ou pas REST ?

```
GET /counter
Accept: application/json
------------------------------
200 OK
Content-Type: application/json
{"value":10}
```

------

## REST ou pas REST ?

```
GET /counter
Accept: application/xml
------------------------------
200 OK
Content-Type: application/xml
<counter value="10"/>
```

------

## REST ou pas REST ?

```
GET /setCounter?value=1
Accept: application/json
------------------------------
200 OK
Content-Type: application/json
{"value":10}
```

------

## REST ou pas REST ?

```
PUT /counter
Accept: application/json
"10"
------------------------------
200 OK
Content-Type: application/json
{"error":"Number format error"}
```

---

## JAX-RS

- est l'implémentation de la spéc REST-Easy ?
- est une spéc JEE pour faire du REST ?
- repose sur l'API servlet ?

---

## CDI

- est une extension de JAX-RS ?
- est une spéc JEE d'une lib d'injection de dépendance ?
- signifie "Context Dependency Injection" ?

---

## CDI ou JAX-RS

- @GET
- @Path
- @Inject
- @RequestScoped

---

## Un "filtre"

- est un concept propre à JAX-RS ?
- permet de transformer les requêtes avant qu'elles n'atteignent les contrôleurs ?
- permet de transformer les réponses ?
- s'applique sur pour toutes les requêtes ?