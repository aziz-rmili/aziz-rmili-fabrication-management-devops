# ================================
# ÉTAPE 1 : BUILD
# On utilise Maven + JDK 17 pour compiler le projet
# ================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Dossier de travail dans le conteneur
WORKDIR /app

# Copier d'abord le pom.xml pour profiter du cache Maven
# Si le pom.xml ne change pas, Maven ne re-télécharge pas les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Compiler et créer le fichier JAR (sans les tests pour aller plus vite)
RUN mvn clean package -DskipTests

# ================================
# ÉTAPE 2 : RUN
# Image légère pour exécuter le JAR
# ================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copier uniquement le JAR généré depuis l'étape build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 8080 (Tomcat embarqué)
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
