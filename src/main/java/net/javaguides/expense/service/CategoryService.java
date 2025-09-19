package net.javaguides.expense.service;

import net.javaguides.expense.dto.CategoryDto;

import java.util.List;

/**
 * Service-Interface für Kategorie-Geschäftslogik - Business Logic Layer.
 *
 * Definiert den Vertrag für alle kategorienspezifischen Geschäftsoperationen.
 * Implementiert das Service-Pattern zur Kapselung der Geschäftslogik und
 * zur Trennung von Controller- und Repository-Schicht.
 *
 * Vorteile des Service-Patterns:
 * - Geschäftslogik-Kapselung: Komplexe Operationen werden hier zentralisiert
 * - Transaktionsmanagement: Services können transaktionale Boundaries definieren
 * - Wiederverwendbarkeit: Geschäftslogik kann von verschiedenen Controllern genutzt werden
 * - Testbarkeit: Business Logic kann isoliert getestet werden
 * - Lose Kopplung: Controller sind nicht direkt mit Repository gekoppelt
 *
 * Diese Interface arbeitet ausschließlich mit DTOs, um die Trennung zwischen
 * API-Layer (DTOs) und Persistierung-Layer (Entities) zu gewährleisten.
 */
public interface CategoryService {

    /**
     * Erstellt eine neue Kategorie im System.
     *
     * Geschäftslogik umfasst:
     * - Validierung der Eingabedaten (Name nicht leer, eindeutig)
     * - Konvertierung DTO -> Entity
     * - Persistierung über Repository
     * - Konvertierung Entity -> DTO für Response
     *
     * @param categoryDto Die zu erstellende Kategorie (ohne ID)
     * @return CategoryDto mit generierter ID und persistierten Daten
     * @throws IllegalArgumentException wenn categoryDto ungültige Daten enthält
     * @throws RuntimeException wenn Name bereits existiert (Unique Constraint)
     */
    CategoryDto createCategory(CategoryDto categoryDto);

    /**
     * Ruft eine spezifische Kategorie anhand ihrer ID ab.
     *
     * Geschäftslogik umfasst:
     * - Suche nach Kategorie mit gegebener ID
     * - Konvertierung Entity -> DTO
     * - Fehlerbehandlung bei nicht gefundener Kategorie
     *
     * @param categoryId Die eindeutige ID der gesuchten Kategorie
     * @return CategoryDto der gefundenen Kategorie
     * @throws RuntimeException wenn keine Kategorie mit gegebener ID existiert
     */
    CategoryDto getCategoryById(Long categoryId);

    /**
     * Ruft alle im System vorhandenen Kategorien ab.
     *
     * Geschäftslogik umfasst:
     * - Abruf aller Kategorien aus der Datenbank
     * - Massenkonvertierung Entity List -> DTO List
     * - Potentielle Sortierung oder Filterung (je nach Anforderung)
     *
     * @return Liste aller CategoryDto-Objekte (kann leer sein)
     * @apiNote Für große Datenmengen sollte Pagination implementiert werden
     */
    List<CategoryDto> getAllCategories();

    /**
     * Aktualisiert eine bestehende Kategorie.
     *
     * Geschäftslogik umfasst:
     * - Validierung dass Kategorie mit ID existiert
     * - Validierung der neuen Daten (Name eindeutig, nicht leer)
     * - Update der bestehenden Entity
     * - Persistierung der Änderungen
     * - Rückgabe der aktualisierten Daten als DTO
     *
     * @param categoryId Die ID der zu aktualisierenden Kategorie
     * @param categoryDto Die neuen Kategorie-Daten (ID wird ignoriert)
     * @return CategoryDto mit den aktualisierten Daten
     * @throws RuntimeException wenn Kategorie nicht existiert oder Name nicht eindeutig
     */
    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);

    /**
     * Löscht eine Kategorie aus dem System.
     *
     * Geschäftslogik umfasst:
     * - Validierung dass Kategorie existiert
     * - Prüfung ob Kategorie noch von Ausgaben referenziert wird (Referential Integrity)
     * - Physische Löschung aus der Datenbank
     *
     * @param categoryId Die ID der zu löschenden Kategorie
     * @throws RuntimeException wenn Kategorie nicht existiert
     * @throws RuntimeException wenn Kategorie noch von Ausgaben verwendet wird
     *
     * @implNote In produktiven Systemen könnte hier Soft-Delete implementiert werden
     */
    void deleteCategory(Long categoryId);

    /*
     * ZUKÜNFTIGE ERWEITERUNGEN könnten umfassen:
     *
     * - findCategoriesByNameContaining(String searchTerm) : Suchfunktion
     * - getCategoriesWithUsageCount() : Kategorien mit Anzahl verwendeter Ausgaben
     * - archiveCategory(Long categoryId) : Soft-Delete statt physischer Löschung
     * - bulkCreateCategories(List<CategoryDto> categories) : Massenerstellung
     * - validateCategoryName(String name) : Separate Validierungsmethode
     */
}