package net.javaguides.expense.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.entity.Category;
import net.javaguides.expense.mapper.CategoryMapper;
import net.javaguides.expense.repository.CategoryRepository;
import net.javaguides.expense.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Konkrete Implementierung des CategoryService-Interfaces.
 *
 * Diese Klasse enthält die tatsächliche Geschäftslogik für alle kategorienspezifischen
 * Operationen. Sie fungiert als Vermittler zwischen Controller-Layer (Web) und
 * Repository-Layer (Datenpersistierung).
 *
 * Architekturmuster:
 * - Service Implementation Pattern: Konkrete Umsetzung des Service-Contracts
 * - Dependency Injection: Repository wird über Konstruktor injiziert
 * - DTO-Entity-Mapping: Trennung zwischen API-Objekten und Datenbank-Objekten
 */
@AllArgsConstructor  // Lombok: Generiert Konstruktor mit allen final Feldern für Dependency Injection
@Service            // Spring: Markiert Klasse als Service-Bean, wird automatisch von Spring verwaltet
public class CategoryServiceImpl implements CategoryService {

    /**
     * Repository für Datenbankzugriffe auf Category-Entitäten.
     * Wird durch Constructor Injection (via @AllArgsConstructor) eingebunden.
     * Spring injiziert automatisch die JPA-Implementation zur Laufzeit.
     */
    private CategoryRepository categoryRepository;

    /**
     * Erstellt eine neue Kategorie im System.
     *
     * Ablauf der Geschäftslogik:
     * 1. DTO -> Entity Konvertierung (für Datenbankoperationen)
     * 2. Persistierung in der Datenbank (Auto-ID-Generierung)
     * 3. Entity -> DTO Konvertierung (für API-Response)
     *
     * @param categoryDto Kategorie-Daten vom API-Request
     * @return Erstellte Kategorie mit generierter ID
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        // Konvertiere CategoryDto zu Category Entity für Datenbankoperationen
        // Mapper kapselt die Transformationslogik
        Category category = CategoryMapper.mapToCategory(categoryDto);

        // Speichere Category-Objekt in Datenbank-Tabelle "categories"
        // JPA generiert automatisch die ID und führt INSERT-Statement aus
        // @GeneratedValue sorgt für Auto-Increment der ID
        Category savedCategory = categoryRepository.save(category);

        // Konvertiere gespeicherte Entity zurück zu DTO für API-Response
        // DTO enthält jetzt die generierte ID aus der Datenbank
        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    /**
     * Ruft eine spezifische Kategorie anhand ihrer ID ab.
     *
     * Verwendung von Optional-Pattern für sichere Null-Behandlung:
     * - findById() gibt Optional<Category> zurück (kann leer sein)
     * - orElseThrow() wirft Exception wenn Kategorie nicht existiert
     *
     * @param categoryId Eindeutige ID der gesuchten Kategorie
     * @return Gefundene Kategorie als DTO
     * @throws RuntimeException wenn keine Kategorie mit dieser ID existiert
     */
    @Override
    public CategoryDto getCategoryById(Long categoryId) {

        // Suche Kategorie in Datenbank, werfe Exception wenn nicht gefunden
        // Optional-Pattern verhindert NullPointerException
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        // Konvertiere gefundene Entity zu DTO für API-Response
        return CategoryMapper.mapToCategoryDto(category);
    }

    /**
     * Ruft alle im System vorhandenen Kategorien ab.
     *
     * Verwendung von Stream API für funktionale Programmierung:
     * - stream(): Konvertiert List zu Stream für Pipeline-Operationen
     * - map(): Transformiert jede Entity zu DTO mittels Mapper
     * - collect(): Sammelt transformierte Objekte in neue Liste
     *
     * @return Liste aller Kategorien als DTOs (leer wenn keine vorhanden)
     */
    @Override
    public List<CategoryDto> getAllCategories() {

        // Lade alle Category-Entities aus der Datenbank
        List<Category> categories = categoryRepository.findAll();

        // Transformiere Entity-Liste zu DTO-Liste mit Stream API
        // Funktionale Transformation: Entity -> DTO für jedes Element
        return categories.stream()
                .map((category) -> CategoryMapper.mapToCategoryDto(category))
                .collect(Collectors.toList());
    }

    /**
     * Aktualisiert eine bestehende Kategorie.
     *
     * Update-Strategie:
     * 1. Existierende Entity aus Datenbank laden (Fehler wenn nicht vorhanden)
     * 2. Entity-Felder mit neuen Werten aus DTO aktualisieren
     * 3. Modifizierte Entity speichern (JPA führt UPDATE aus)
     * 4. Aktualisierte Entity als DTO zurückgeben
     *
     * @param categoryId ID der zu aktualisierenden Kategorie
     * @param categoryDto Neue Kategorie-Daten (ID wird ignoriert)
     * @return Aktualisierte Kategorie als DTO
     * @throws RuntimeException wenn Kategorie nicht existiert
     */
    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {

        // Lade existierende Category-Entity aus Datenbank
        // Fehler wenn Kategorie mit dieser ID nicht existiert
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        // Aktualisiere die Entity-Felder mit neuen Werten aus DTO
        // Nur der Name wird aktualisiert (ID bleibt unverändert)
        category.setName(categoryDto.name());

        // Speichere modifizierte Entity (JPA erkennt Änderungen und führt UPDATE aus)
        // Hibernate Dirty Checking: Nur geänderte Felder werden in UPDATE einbezogen
        Category updatedCategory = categoryRepository.save(category);

        // Konvertiere aktualisierte Entity zu DTO für API-Response
        return CategoryMapper.mapToCategoryDto(updatedCategory);
    }

    /**
     * Löscht eine Kategorie aus dem System.
     *
     * Lösch-Strategie:
     * 1. Existenz der Kategorie prüfen (Fehler wenn nicht vorhanden)
     * 2. Physische Löschung aus der Datenbank
     *
     * Hinweis: Diese Implementierung führt Hard-Delete aus.
     * In produktiven Systemen sollte geprüft werden, ob die Kategorie
     * noch von Ausgaben referenziert wird (Foreign Key Constraints).
     *
     * @param categoryId ID der zu löschenden Kategorie
     * @throws RuntimeException wenn Kategorie nicht existiert
     */
    @Override
    public void deleteCategory(Long categoryId) {

        // Prüfe ob Kategorie mit gegebener ID in Datenbank existiert
        // Lade Entity um sicherzustellen, dass sie vor Löschung existiert
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        // Führe physische Löschung der Entity aus der Datenbank durch
        // JPA generiert DELETE-Statement für diese Entity
        categoryRepository.delete(category);

        // Alternative: categoryRepository.deleteById(categoryId)
        // würde direkter sein, aber weniger explizit bei der Existenzprüfung
    }

    /*
     * VERBESSERUNGSMÖGLICHKEITEN für eine produktive Anwendung:
     *
     * 1. Exception Handling:
     *    - Custom Exceptions statt generischer RuntimeException
     *    - Spezifische Fehlercodes und Nachrichten
     *
     * 2. Validierung:
     *    - Input-Validierung (Name nicht leer, Länge prüfen)
     *    - Business Rules (Name-Eindeutigkeit prüfen)
     *
     * 3. Transaktionsmanagement:
     *    - @Transactional für atomare Operationen
     *    - Rollback-Strategien bei Fehlern
     *
     * 4. Logging:
     *    - Structured Logging für Monitoring
     *    - Audit Trail für Änderungen
     *
     * 5. Performance:
     *    - Pagination für getAllCategories()
     *    - Caching für häufig abgerufene Daten
     */
}