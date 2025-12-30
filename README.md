# Student Tracker - Application Web MVC

Ce projet est une application web dynamique de gestion académique développée en Java (Jakarta EE). Elle permet de gérer une base de données d'étudiants via une interface sécurisée suivant l'architecture MVC (Modèle-Vue-Contrôleur).

## Fonctionnalités

L'application propose un système CRUD (Create, Read, Update, Delete) complet avec gestion des accès :

- **Authentification sécurisée** : Système de login avec gestion des sessions.
- **Gestion des rôles** :
    - **Instructeur** : Accès total (Lister, Ajouter, Modifier, Supprimer des étudiants).
    - **Étudiant** : Accès restreint (Consultation de la liste uniquement).
- **Persistance des données** : Intégration avec MySQL via JDBC et JNDI.
- **Sécurité** : Utilisation de filtres (Filters) pour protéger les routes et de cookies pour la personnalisation.
- **Interface dynamique** : Utilisation de JSTL et Expression Language (EL) pour un affichage propre sans scriptlets.

## Technologies et Environnement

- **IDE** : IntelliJ IDEA
- **Langages** : Java (Jakarta EE), SQL, HTML5, CSS3
- **Serveur d'application** : Apache Tomcat 11
- **Base de données** : MySQL
- **Gestionnaire de dépendances** : Maven
- **Architecture** : MVC & DAO (Data Access Object)

## Installation et Utilisation

### 1. Prérequis
- Java JDK 17 ou supérieur.
- Apache Tomcat (Version 10 ou 11 recommandée).
- MySQL Server.

### 2. Configuration de la base de données (IMPORTANT)
Pour que l'application fonctionne, vous devez disposer de la même structure de base de données que le projet.
1. Ouvrez votre terminal MySQL ou votre client SQL préféré.
2. Utilisez le contenu du fichier `sql.txt` fourni dans le dépôt pour créer les tables et insérer les données initiales (étudiants et utilisateurs).
   > **Note** : L'utilisateur doit impérativement avoir la même base de données SQL que celle définie dans les fichiers `sql.txt`.

### 3. Configuration de l'IDE (IntelliJ IDEA)
1. Clonez le dépôt sur votre machine.
2. Ouvrez le projet dans **IntelliJ IDEA**.
3. Assurez-vous que le projet est reconnu comme un projet **Maven** (clic droit sur `pom.xml` > *Add as Maven Project*).
4. Configurez votre serveur **Tomcat** dans IntelliJ :
   - Allez dans *Run* > *Edit Configurations*.
   - Ajoutez une nouvelle configuration *Tomcat Server* > *Local*.
   - Dans l'onglet *Deployment*, ajoutez l'artéfact du projet (`war exploded`).
5. Modifiez le fichier `context.xml` (situé dans `src/main/webapp/META-INF/`) pour y renseigner vos identifiants MySQL (username et password).

### 4. Lancement
- Démarrez le serveur Tomcat via IntelliJ.
- L'application sera accessible par défaut à l'adresse : `http://localhost:8080/webstudentbook`

## Structure du Projet

- `src/main/java` : Contient les Servlets (Contrôleurs), les JavaBeans (Modèles) et les classes DAO (Accès DB).
- `src/main/webapp` : Contient les pages JSP, les fichiers CSS et les configurations WEB-INF/META-INF.
- `sql.txt` : Scripts de création de la base de données.

## 🎓 Compétences acquises
- Mise en œuvre de l'architecture MVC et du pattern DAO.
- Gestion des ressources serveur JNDI et des pools de connexions.
- Sécurisation des applications web (Sessions, Cookies et Filtres).
- Développement d'interfaces dynamiques avec JSTL.
