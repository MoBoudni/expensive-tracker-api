# Expense Tracker App

Eine moderne REST API-basierte Anwendung für die Verwaltung von Ausgaben-Kategorien, entwickelt mit Spring Boot und MySQL.

## Projektübersicht

Diese Anwendung implementiert eine vollständige CRUD-API für die Verwaltung von Kategorien in einem Ausgabenverfolgungssystem. Sie folgt bewährten Architekturmustern und verwendet moderne Java-Technologien für eine saubere, wartbare Codebasis.

## Technologie-Stack

- **Java 21** - Moderne Java-Version mit Records und Pattern Matching
- **Spring Boot 3.2.2** - Framework für die Anwendungsentwicklung
- **Spring Data JPA** - Datenpersistierung und Repository-Pattern
- **Hibernate 6.4.1** - ORM-Framework für Datenbankoperationen
- **MySQL 8.3.0** - Relationale Datenbank
- **Lombok** - Code-Generierung für Boilerplate-Reduktion
- **Maven** - Build-Management und Dependency-Verwaltung
- **JUnit 5** - Test-Framework

## Architektur

Das Projekt folgt einer mehrschichtigen Architektur (Layered Architecture):

```
├── Controller Layer    # REST-Endpunkte (CategoryController)
├── Service Layer      # Geschäftslogik (CategoryService)
├── Repository Layer   # Datenzugriff (CategoryRepository)
├── Entity Layer       # Datenmodell (Category)
└── DTO Layer         # Datenübertragung (CategoryDto)
```

### Verwendete Design Patterns

- **Repository Pattern** - Abstraktion der Datenzugriffsschicht
- **Service Pattern** - Kapselung der Geschäftslogik
- **DTO Pattern** - Trennung von API- und Domain-Objekten
- **Mapper Pattern** - Konvertierung zwischen Entities und DTOs

## API-Endpunkte

### Kategorien verwalten

| Method   | Endpoint                | Beschreibung             |
|----------|-------------------------|--------------------------|
| `GET`    | `/api/categories`       | Alle Kategorien abrufen  |
| `GET`    | `/api/categories/{id}`  | Kategorie nach ID abrufen|
| `POST`   | `/api/categories`       | Neue Kategorie erstellen |
| `PUT`    | `/api/categories/{id}`  | Kategorie aktualisieren  |
| `DELETE` | `/api/categories/{id}`  | Kategorie löschen        |

### Beispiel-Requests

**Kategorie erstellen:**
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Lebensmittel"}'
```

**Alle Kategorien abrufen:**
```bash
curl http://localhost:8080/api/categories
```

## Installation und Setup

### Voraussetzungen

- Java 21 oder höher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### 1. Repository klonen

```bash
git clone <repository-url>
cd expense-tracker-app
```

### 2. Datenbank konfigurieren

MySQL-Datenbank erstellen:
```sql
CREATE DATABASE expense_tracker_db;
CREATE USER 'expense_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON expense_tracker_db.* TO 'expense_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Anwendungskonfiguration

Erstellen Sie `src/main/resources/application.properties`:

```properties
# Datenbank-Konfiguration
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker_db
spring.datasource.username=expense_user
spring.datasource.password=your_password

# JPA/Hibernate Konfiguration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Server-Konfiguration
server.port=8080
```

### 4. Anwendung starten

```bash
# Mit Maven
mvn spring-boot:run

# Oder kompilierte JAR ausführen
mvn clean package
java -jar target/expense-tracker-app-0.0.1-SNAPSHOT.jar
```

Die Anwendung ist verfügbar unter: `http://localhost:8080`

## Tests ausführen

```bash
# Alle Tests ausführen
mvn test

# Nur Integrationstests
mvn test -Dtest="*IT"

# Test-Coverage-Report generieren
mvn jacoco:report
```

## Datenmodell

### Category Entity

| Feld   | Typ      | Beschreibung  | Constraints                 |
|--------|----------|---------------|-----------------------------|
| `id`   | `Long`   | Eindeutige ID | Primary Key, Auto-generated |
| `name` | `String` | Kategoriename | Not Null, Unique            |

### CategoryDto

```json
{
  "id": 1,
  "name": "Lebensmittel"
}
```

## Entwicklung

### Code-Stil

- Verwenden Sie aussagekräftige Variablen- und Methodennamen
- Dokumentieren Sie öffentliche APIs mit JavaDoc
- Folgen Sie den Spring Boot Best Practices
- Nutzen Sie Lombok zur Reduzierung von Boilerplate-Code

### Neue Features hinzufügen

1. **Entity erweitern** - Neue Felder zu Category hinzufügen
2. **DTO anpassen** - CategoryDto entsprechend erweitern
3. **Mapper aktualisieren** - Mapping-Logik anpassen
4. **Service erweitern** - Geschäftslogik implementieren
5. **Controller erweitern** - REST-Endpunkte hinzufügen
6. **Tests schreiben** - Unit- und Integrationstests

### Nützliche Maven-Commands

```bash
# Abhängigkeiten aktualisieren
mvn versions:display-dependency-updates

# Code-Quality prüfen
mvn checkstyle:check

# Dependency-Tree anzeigen
mvn dependency:tree

# Spring Boot DevTools nutzen (Hot Reload)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Deployment

### Docker (Optional)

```dockerfile
FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/expense-tracker-app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
docker build -t expense-tracker .
docker run -p 8080:8080 expense-tracker
```

### Produktionsüberlegungen

- **Security**: Spring Security für Authentication/Authorization implementieren
- **Logging**: Structured Logging mit Logback/SLF4J
- **Monitoring**: Actuator-Endpunkte aktivieren
- **Caching**: Redis/Hazelcast für Performance-Optimierung
- **Validation**: Bean Validation für Input-Validierung
- **Error Handling**: Globale Exception-Handler implementieren

## Lizenz

Dieses Projekt steht unter der MIT-Lizenz. Details siehe [LICENSE](LICENSE) Datei.

## Beitragen

Contributions sind willkommen! Bitte:

1. Fork das Repository
2. Feature-Branch erstellen (`git checkout -b feature/amazing-feature`)
3. Änderungen committen (`git commit -m 'Add amazing feature'`)
4. Branch pushen (`git push origin feature/amazing-feature`)
5. Pull Request erstellen

## Support

Bei Fragen oder Problemen öffnen Sie ein Issue im Repository oder kontaktieren Sie das Entwicklungsteam.

---

**Entwickelt mit Spring Boot und Java 21** ☕
