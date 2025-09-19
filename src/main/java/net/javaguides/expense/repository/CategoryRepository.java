package net.javaguides.expense.repository;

import net.javaguides.expense.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository-Interface für Category Entity - Data Access Layer.
 *
 * Implementiert das Repository-Pattern für die Datenbankzugriffe auf Category-Entitäten.
 * Erweitert JpaRepository, welches bereits alle grundlegenden CRUD-Operationen bereitstellt.
 *
 * Spring Data JPA erstellt zur Laufzeit automatisch eine Implementierung dieses Interfaces
 * mit allen erforderlichen Datenbankoperationen.
 *
 * Generische Parameter:
 * - Category: Die Entity-Klasse, die verwaltet wird
 * - Long: Der Datentyp des Primärschlüssels (id-Feld in Category)
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /*
     * AUTOMATISCH BEREITGESTELLTE METHODEN durch JpaRepository<Category, Long>:
     *
     * === CRUD-Operationen (Create, Read, Update, Delete) ===
     * - save(Category entity)              : Speichert oder aktualisiert eine Kategorie
     * - saveAll(List<Category> entities)   : Speichert mehrere Kategorien
     * - findById(Long id)                  : Findet Kategorie anhand ID (Optional<Category>)
     * - findAll()                          : Lädt alle Kategorien
     * - findAll(Sort sort)                 : Lädt alle Kategorien sortiert
     * - findAll(Pageable pageable)         : Lädt Kategorien mit Pagination
     * - count()                            : Zählt alle Kategorien
     * - existsById(Long id)                : Prüft ob Kategorie mit ID existiert
     * - deleteById(Long id)                : Löscht Kategorie anhand ID
     * - delete(Category entity)            : Löscht spezifische Kategorie
     * - deleteAll()                        : Löscht alle Kategorien
     *
     * === TRANSAKTIONSMANAGEMENT ===
     * Alle Methoden sind automatisch transaktional:
     * - Lesevorgänge: @Transactional(readOnly = true)
     * - Schreibvorgänge: @Transactional mit automatischem Rollback bei Exceptions
     *
     * === QUERY-GENERIERUNG ===
     * Spring Data JPA kann automatisch Queries basierend auf Methodennamen generieren:
     *
     * Beispiele für custom Query-Methoden (können bei Bedarf hinzugefügt werden):
     * - findByName(String name)              : Findet Kategorie nach Name
     * - findByNameContaining(String name)    : Findet Kategorien die den Namen enthalten
     * - findByNameIgnoreCase(String name)    : Case-insensitive Suche nach Name
     * - existsByName(String name)            : Prüft ob Kategorie mit Name existiert
     * - countByNameContaining(String name)   : Zählt Kategorien die den Namen enthalten
     *
     * Oder mit @Query für komplexere Abfragen:
     * @Query("SELECT c FROM Category c WHERE c.name LIKE %:name%")
     * List<Category> findByNamePattern(@Param("name") String name);
     */

    // Derzeit sind keine custom Methoden erforderlich, da JpaRepository
    // alle benötigten CRUD-Operationen für das Category-Management bereitstellt.
    // Custom Query-Methoden können hier bei Bedarf hinzugefügt werden.
}