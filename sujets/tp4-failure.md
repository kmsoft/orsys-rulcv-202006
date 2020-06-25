# TP4: failure tolerance

Le but de ce TP est d'expérimenter la librairie _Fault Tolerance_ de `eclipse.microprofile`.
Pour cela, on simulera des pannes des micro-services et la gateway utilisera les annotations de gestion de panne.

Liens utiles:
- https://www.tomitribe.com/blog/microprofile-fault-tolerance-annotations/
- https://quarkus.io/guides/microprofile-fault-tolerance

## Simulation des pannes

Pour simuler les pannes de nos micro-services, créer 2 annotations qui activent un `ContainerRequestFilter`:
- `MakeSlowFilter`: ce filtre appelle `Thread.sleep` de manière aléatoire. Éventuellement, le délai peut être passé en paramètre de l'annotation.
- `SimulateNetworkFailtureFilter`: ce filtre avorte la requête en cours avec le code d'erreur `504 Gateway Timeout` (erreur généralement reportée quand l'infrastucture réseau échoue à contacter le serveur).

Mettre le code de ces filtres/annotations dans un package `common` qui représenterait une librairie commune à tous les micro-services.

Voici un exemple de filtre associé à une annotation:
```java
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
	String myParam() default "foo";
}
...
@Provider
@MyAnnotation
public class MyFilter implements ContainerRequestFilter {
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Quand la classe du filtre n'est pas annotée avec @PreMatching, le filtre est
		// appelé après que la méthode répondant à la requête ait été trouvée et avant
		// l'appel de cette méthode.
		// Cela permet de récupérer la méthode (en tant qu'objet de l'API reflection) qui sera 
		// appelée. A partir de cet objet `Method`, on peut lire les éventuels paramètre de l'annotation.
		Method method = resourceInfo.getResourceMethod();
		MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
		...
	}
}
...
@Path("/foo")
public class MyResource {
	@GET
	@MyAnnotation(myParam="bar")
	Foo get() {...}
}
```

## Gestion des pannes

- Utiliser les annotations créées sur quelques endpoints des micro-services. On peut aussi utiliser les annotations sur les clients REST qui appellent nos micro-services.
- Expérimenter les annotations de `microprofile.faulttolerance` dans la gateway