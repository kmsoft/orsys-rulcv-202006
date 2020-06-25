# TP5: Docker

Le but du TP est d'expérimenter les principaux concepts de Docker.

## La ligne de commande Docker (Docker CLI)

But: expérimenter la commande `docker` en utilisant l'_image_` Docker d'[Alpine Linux](https://alpinelinux.org/). Cette distribution de Linux est particulièrement légère et est appropriée comme image de base Docker.

Afin de trouver les commandes à appliquer, on pourra utiliser `docker --help` ou la [référence en ligne](https://docs.docker.com/engine/reference/commandline/docker/).

### Gestion des images

Une _image_ Docker définit un ensemble de fichiers avec éventuellement la commande à lancer lorsque l'on va instancier cette image. On peut stocker et gérer plusieurs images sur sa machine. Les images sont soit _construites_ localement, soit _tirées_ depuis un _registre_ Docker. Par défaut, le registre Docker est [hub.docker.com](https://hub.docker.com).

- _Lister_ les images installées sur votre machine
- _Tirer_ l'image `alpine` et vérifier qu'elle apparaît bien dans le listing des images
- _Supprimer_ l'image `alpine`

### Gestion des conteneurs

Un _conteneur_ est une instance d'une image. Les conteneurs d'une même image possèdent leurs propres copies des fichiers de l'image. Un conteneur a un _cycle de vie_ (arrêté, démarré, en pause, ...) sur lesquelles les commandes Docker peuvent agir.

#### Lancement/suppression de conteneurs

- _Lister_ les conteneurs en cours d'exécution: la liste doit être vide.
- _Lancer_ un conteneur de l'image `alpine` qui affiche un message. Notez que la commande se charge de _tirer_ l'image, _créer_ le conteneur et le _démarrer_. Pour afficher un message, la commande Linux est par exemple `echo Bonjour` (commande qui sera lancée par la commande Docker).
- _Lister_ les __conteneurs__ en cours d'exécution: la liste doit être vide puisque le conteneur a terminé
- _Lister_ les __images__ : `alpine` doit apparaître
- _Lister_ __tous__ les conteneurs (pas seulement ceux en cours d'exécution) : le conteneur préalablement lancé doit être listé.
- _Supprimer_ le conteneur en utilisant soit son nom soit son id. Vérifier qu'il a bien disparu mais que l'image est toujours là.
- _Lancer_ un conteneur comme précédemment, mais cette fois ajouter l'option `--rm` qui le supprimera une fois terminé. Vérifier que le conteneur est bien supprimé une fois terminé. Par la suite, il est recommandé d'utiliser cette option systématiquement pour ne pas garder des conteneurs inutilisés sur son disque.

#### Intéraction avec un conteneur en cours d'exécution

Soit la commande Linux suivante:

	sh -c "while true; do date; sleep 1; done"

Cette commande lance l'interpréteur de commande `sh` en lui passant la commande à exécuter (boucle infinie qui affiche la date).

- Lancer un conteneur qui exécute cette commande: la date doit être affichée dans le terminal toutes les secondes. Ne pas oublier l'option `--rm`.
- Faire `Ctrl+C`. Ceci ne tue pas le conteneur mais le _détache_ : sa sortie n'est plus redirigée vers la console.
- Vérifier que le conteneur est toujours dans la liste des conteneurs en cours d'exécution.
- Tester la commande `attach` permettant d'_attacher_ le conteneur à la console.
- Dans une autre console, afficher les 10 derniers _logs_ du conteneur.
- _Exécuter_ une commande dans le conteneur en cours d'exécution (par ex. `echo hello`).
- _Tuer_ le conteneur en cours d'exécution. Il ne devrait plus apparaître dans le listing de tous les conteneurs s'il avait été démarré avec l'option appropriée.

## Création d'une image Docker

### Programme de test Java

Soit le programme Java suivant qui écrit la date toutes les secondes d'un fichier:
```java
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

public class DateWriter {
	private static final String fileOutputPathPropertyKey = "com.example.outputPath";
	public static void main(String[] args) throws IOException, InterruptedException {
		String fileOutputPath = System.getProperty(fileOutputPathPropertyKey);
		if (fileOutputPath == null) {
			throw new RuntimeException("Property '"+fileOutputPathPropertyKey+"' is not defined");
		}
		try(PrintWriter writer = new PrintWriter(fileOutputPath)) {
			while(true) {
				writer.println(Instant.now().toString());
				writer.flush();
				Thread.sleep(1000);
			}
		}
	}
}
```

- Compiler le programme pour Java8 (l'image de base que nous utiliserons par la suite fournit une JVM version 8)
```sh
javac -target 8 -source 8 DateWriter.java
```
- Créer un jar à partir de la classe compilée
```sh
jar cfe DateWriter.jar DateWriter DateWriter.class
```
- Lancer le jar en passant en paramètre la propriété spécifiant le chemin du fichier où la date est écrite.
```sh
java -Dcom.example.outputPath=date.txt -jar DateWriter.jar
```
- Vérifier que le fichier est bien mis à jour toutes les secondes.

### Création du `Dockerfile`

Créer un `Dockerfile` qui définit une image permettant de faire tourner le programme java compilé ci-avant:
- part de l'image `openjdk:8-jre-alpine` (image minimaliste basée sur Alpine Linux et embarquant une JVM): instruction `FROM`
- copie le jar dans l'image: instruction `COPY`
- lance la commande `mkdir /data` qui crée le dossier `/data` dans l'image: `RUN`
- définit la commande à lancer au démarrage du conteneur: lance le jar en spécifiant `/data/date.txt` comme chemin où écrire la date: `CMD`

[Èxample Dockerfile](https://docs.docker.com/get-started/part2/#sample-dockerfile)

### Construction de l'image

Construire l'image _taggée_ "datewriter". Elle doit apparaître dans le listing des images:
```txt
REPOSITORY             TAG                 IMAGE ID            CREATED             SIZE
datewriter             latest              d7682adbc46e        2 minutes ago       84.9MB
```

### Test de l'image

- Lancer un conteneur de l'image construite:
  - ne pas oublier l'option `--rm` ;
  - utiliser l'option `--detach` pour lancer le conteneur en mode _détaché_ (sorties du conteneur pas redirigées vers la console ⇒ console non bloquée) ;
  - utiliser l'option `--name datewriter` pour nommer le conteneur plutôt que laisser Docker lui assigner un nom aléatoire
- Exécuter un shell dans le conteneur de sorte à pouvoir lancer la commande `tail -f <fichier de sortie>` afin de tester notre programme:
```sh
docker exec -it <nom du conteneur> sh
```
Cette commande fait exécuter la commande `sh` au conteneur. La commande `sh` démarre un interpréteur de commande qui attend des commandes sur l'entrée standard. L'option `-i` permet de garder l'entrée standard du conteneur ouverte. L'option `-t` permet de créer le canal de communication entre votre console et `sh`. À partir de là, ce qui est saisi au clavier peut être interprété par le shell du conteneur.

## Utilisation d'un `volume`

À ce stade, `datewriter` écrit dans son système de fichiers privé: personne d'autre ne peut lire le fichier et la suppression du conteneur implique la suppression du fichier.

Le but est de lancer en parallèle de notre `datewriter`, un autre conteneur qui va lire le fichier produit par `datewriter`. Pour cela, nous allons créer un _volume_ qui est un espace de stockage qui peut être partagé par plusieurs conteneurs.

- Créer le volume `datevol`:
```sh
docker volume create datevol
```
- Vérifier que le volume apparaît dans le listing des volumes:
```sh
docker volume list
```
- Lancer le conteneur `datewriter`:
  - `--name datewriter`
  - `--rm`
  - `--detach`
  - `--volume datevol:/data`: monter le volume `datevol` dans le dossier `data`
- Lancer un conteneur `alpine` qui fait un `tail -f` du fichier `date.txt`.
