# TP1: mise en place de Quarkus

Le but est de créer un projet basé sur Quarkus, une implémentation JEE créée par RedHat. Ce projet nous servira pour les TPs suivants.

## Outils nécessaires

- [JDK](https://www.oracle.com/fr/java/technologies/javase-downloads.html)
- Un environnement de développement: Eclipse, IntelliJ ou Visual Studio Code
  
	Si vous n'avez pas d'IDE installé, [Visual Studio Code](https://code.visualstudio.com/) avec les extensions Quarkus et "Language Support for Java(TM) by Red Hat" est un bon choix.

- [Postman](https://www.postman.com/) à moins que vous soyez habitué à [SoapUI](https://www.soapui.org/)

## Création du projet

Si vous utilisez Visual Studio Code, générer un projet avec la commande `Quarkus: Generate a Quarkus project` (pour exécuter une commande sur VS Code: `Ctrl+Shift+P` et saisir la commande). Sinon, générer le projet depuis https://code.quarkus.io/ sans sélectionner d'extensions supplémentaires.

## Lancement de l'application

Pour Eclipse: https://developers.redhat.com/blog/2019/05/09/create-your-first-quarkus-project-with-eclipse-ide-red-hat-codeready-studio/

Sur Visual Studio Code, exécuter la commande `Quarkus: Debug current Quarkus project`.

## Tester

Tester le endpoint `/hello` dans Postman.
