package net.javaguides.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.component.page.AppShellConfigurator;

/**
 * Haupt-Anwendungsklasse für die Expense Tracker Anwendung.
 *
 * Diese Klasse dient als Einstiegspunkt (Entry Point) für die gesamte Spring Boot Anwendung.
 * Sie enthält die main-Methode, die beim Starten der Anwendung ausgeführt wird.
 *
 * Die @SpringBootApplication Annotation ist eine Kombination aus drei wichtigen Annotationen:
 * - @Configuration: Markiert diese Klasse als Konfigurationsklasse
 * - @EnableAutoConfiguration: Aktiviert Spring Boots automatische Konfiguration
 * - @ComponentScan: Scannt automatisch nach Spring-Komponenten im aktuellen Package und Subpackages
 *
 * Package-Struktur wird automatisch gescannt:
 * - net.javaguides.expense.controller (REST Controller)
 * - net.javaguides.expense.service (Business Logic Services)
 * - net.javaguides.expense.repository (Data Access Repositories)
 * - net.javaguides.expense.entity (JPA Entities)
 * - net.javaguides.expense.dto (Data Transfer Objects)
 * - net.javaguides.expense.mapper (Entity-DTO Mapper)
 */
// Spring Boot: Haupt-Annotation für Anwendungskonfiguration und -start
@SpringBootApplication
/*
	 * ERWEITERTE KONFIGURATIONSMÖGLICHKEITEN:
	 *
	 * Diese Hauptklasse könnte erweitert werden um:
	 *
	 * 1. Custom Bean Definitions:
	 *    @Bean
	 *    public CommandLineRunner demo(CategoryRepository repository) {
	 *        return (args) -> {
	 *            // Initialisierung von Test-Daten
	 *        };
	 *    }
	 *
	 * 2. Profile-spezifische Konfiguration:
	 *    @Profile("dev")
	 *    @Bean
	 *    public DataSource devDataSource() { ... }
	 *
	 * 3. Event Listeners:
	 *    @EventListener(ApplicationReadyEvent.class)
	 *    public void onStartup() {
	 *        log.info("Expense Tracker Application gestartet!");
	 *    }
	 *
	 * 4. Scheduler Aktivierung:
	 *    @EnableScheduling für periodische Tasks
	 *
	 * 5. Security Konfiguration:
	 *    @EnableWebSecurity für Authentication/Authorization
	 */
@Theme("my-theme")
public class ExpenseTrackerAppApplication implements AppShellConfigurator {

    /**
     * Haupt-Einstiegsmethode der Anwendung.
     *
     * Diese Methode wird beim Start der Anwendung (z.B. via java -jar oder IDE) aufgerufen.
     * SpringApplication.run() startet den gesamten Spring Application Context und:
     *
     * Initialisierungsschritte:
     * 1. Lädt application.properties/application.yml Konfiguration
     * 2. Scannt alle Klassen im Package nach Spring-Annotationen
     * 3. Erstellt und konfiguriert alle Spring Beans (Services, Repositories, Controller)
     * 4. Initialisiert Datenbank-Verbindung (HikariCP Connection Pool)
     * 5. Startet Hibernate/JPA Entity Manager
     * 6. Erstellt/Aktualisiert Datenbank-Schema (wenn configured)
     * 7. Startet eingebetteten Tomcat-Server auf Port 8080
     * 8. Registriert alle REST-Endpunkte (/api/categories/*)
     * 9. Aktiviert Health Checks und Monitoring
     * 10. Anwendung ist bereit für HTTP-Requests
     *
     * @param args Kommandozeilen-Argumente (können Spring-Properties überschreiben)
     *             Beispiele:
     *             --server.port=8081 (ändert Server-Port)
     *             --spring.profiles.active=dev (aktiviert dev-Profile)
     *             --spring.datasource.url=... (überschreibt DB-URL)
     */
    public static void main(String[] args) {
        // Startet die komplette Spring Boot Anwendung
        // Übergibt diese Klasse als Konfigurationsklasse und die Kommandozeilen-Argumente
        SpringApplication.run(ExpenseTrackerAppApplication.class, args);
        /*
		 * Nach erfolgreichem Start ist die Anwendung verfügbar unter:
		 * - HTTP Base URL: http://localhost:8080
		 * - REST API Endpoints: http://localhost:8080/api/categories
		 * - Actuator Health Check: http://localhost:8080/actuator/health (falls aktiviert)
		 * - H2 Console: http://localhost:8080/h2-console (falls H2 DB verwendet)
		 *
		 * Die Anwendung läuft bis sie manuell gestoppt wird (Ctrl+C) oder
		 * ein unbehandelter Fehler auftritt.
		 */
    }
}
