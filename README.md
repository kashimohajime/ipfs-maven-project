Prérequis:
Java JDK 17+
Apache Maven 3.9+
IPFS (Kubo) installé(voir tp)

Vérification :
java --version
mvn --version
ipfs --version

Lancer IPFS dans un terminal :
ipfs daemon
➡️ Laisser le démon IPFS actif.

Compiler le projet à la racine du projet :
cd ipfs-miniproject
mvn clean package

Exécuter l’application
mvn javafx:run

➡️ L’interface JavaFX s’ouvre et permet :
Upload d’un fichier vers IPFS
Download depuis IPFS
Pin / Unpin

🚨 Remarques:
Modifier la ligne 16 dans pom.xml avec la version de votre jdk installé
Utiliser de préférence l'IDE VScode avec l'extension de java : Extension Pack for Java
