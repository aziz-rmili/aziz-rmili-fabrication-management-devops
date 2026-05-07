# 🏭 Fabrication Management — DevOps Project

> Système de gestion des ordres de fabrication industrielle — Spring Boot REST API déployé avec une chaîne DevOps complète.

[![CI Pipeline](https://github.com/aziz-rmili/aziz-rmili-fabrication-management-devops/actions/workflows/ci.yml/badge.svg)](https://github.com/aziz-rmili/aziz-rmili-fabrication-management-devops/actions/workflows/ci.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aziz-rmili_fabrication-management&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=aziz-rmili_fabrication-management)
[![Docker Image](https://img.shields.io/docker/v/azizrmili/fabrication-management?label=Docker%20Hub)](https://hub.docker.com/r/azizrmili/fabrication-management)

---

## 📋 Description du projet

Application REST de gestion de production industrielle permettant de :
- Gérer les **machines** de fabrication (CRUD + états : DISPONIBLE, EN_MAINTENANCE, HORS_SERVICE)
- Gérer les **employés** et leur affectation aux machines
- Créer et suivre les **ordres de fabrication**
- Gérer le catalogue des **produits**

**Stack technique :** Spring Boot 3.2 · Java 17 · MySQL · Docker · Kubernetes · ArgoCD · Prometheus · Grafana

---

## 🏗️ Architecture DevOps

```
┌─────────────────────────────────────────────────────────────────┐
│                        DEVELOPER                                 │
│  git push → feat/branch → PR → dev → main                       │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                   GITHUB ACTIONS (CI)                            │
│  Lint → Tests (H2) → SonarCloud → Docker Build → Trivy Scan     │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                     DOCKER HUB                                   │
│  azizrmili/fabrication-management:latest                         │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│               ARGOCD (GitOps CD)                                 │
│  Sync auto k8s/ → Minikube                                       │
│  Deployment + Service + ConfigMap                                │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│               MONITORING (Prometheus + Grafana)                  │
│  /actuator/prometheus → métriques JVM + HTTP                     │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📁 Structure du projet

```
fabrication-management/
├── backend/                          # Spring Boot API
│   ├── src/
│   │   ├── main/java/com/gestion/fabrication/
│   │   │   ├── controller/           # REST Controllers
│   │   │   ├── entity/               # Entités JPA
│   │   │   ├── repository/           # Spring Data Repositories
│   │   │   ├── service/              # Logique métier
│   │   │   └── FabricationApplication.java
│   │   └── test/                     # Tests unitaires (H2)
│   ├── Dockerfile                    # Multi-stage build
│   └── pom.xml                       # Maven + SonarQube + JaCoCo
├── k8s/                              # Kubernetes manifests
│   ├── deployment.yaml
│   ├── service.yaml
│   └── configmap.yaml
├── .github/
│   └── workflows/
│       └── ci.yml                    # Pipeline CI/CD complet
└── README.md
```

---

## 🚀 Lancement local

### Prérequis
- Java 17+
- Maven 3.9+
- Docker
- MySQL (ou utiliser H2 pour les tests)

### Lancement avec MySQL
```bash
# 1. Démarrer MySQL
docker run -d --name mysql-fab \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=fabrication_db \
  -p 3306:3306 mysql:8

# 2. Builder le projet
cd backend
mvn clean package -DskipTests

# 3. Lancer l'application
java -jar target/fabrication-1.0.0.jar
```

### Lancement avec Docker Compose
```bash
docker compose up -d
```

### Tests uniquement (H2, sans MySQL)
```bash
cd backend
mvn test -Dspring.profiles.active=test
```

---

## 🔗 Endpoints API

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/machines` | Lister toutes les machines |
| POST | `/api/machines` | Créer une machine |
| GET | `/api/machines/disponibles` | Machines disponibles |
| PATCH | `/api/machines/{id}/maintenance/debut` | Démarrer maintenance |
| GET | `/api/employes` | Lister les employés |
| GET | `/api/produits` | Lister les produits |
| GET | `/api/ordres` | Lister les ordres de fabrication |
| GET | `/actuator/health` | Health check |
| GET | `/actuator/prometheus` | Métriques Prometheus |

📚 **Documentation Swagger** : `http://localhost:8080/swagger-ui.html`

---

## ⚙️ Variables d'environnement (GitHub Secrets)

| Secret | Description |
|--------|-------------|
| `DOCKER_USERNAME` | Nom d'utilisateur Docker Hub |
| `DOCKER_PASSWORD` | Token Docker Hub |
| `SONAR_TOKEN` | Token SonarCloud |

---

## 🐳 Docker

```bash
# Build
docker build -t fabrication-management ./backend

# Run
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/fabrication_db \
  -e SPRING_DATASOURCE_PASSWORD=root \
  fabrication-management
```

---

## ☸️ Kubernetes (Minikube)

```bash
# Démarrer Minikube
minikube start

# Appliquer les manifests
kubectl apply -f k8s/

# Vérifier
kubectl get pods
kubectl get services
```

---

## 📊 Monitoring

- **Prometheus** : scrape `/actuator/prometheus` toutes les 15s
- **Grafana** : dashboard JVM + HTTP requests + métriques custom
- **Alerting** : alerte si disponibilité < 99%

---

## 👤 Auteur

**Med Aziz Rmili** — IT Business School  
Module : Pratique DevOps, Chaînes d'outils et Automatisation
