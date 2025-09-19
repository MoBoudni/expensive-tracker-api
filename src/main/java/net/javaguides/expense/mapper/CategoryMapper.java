package net.javaguides.expense.mapper;

import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.entity.Category;

/**
 * Mapper-Klasse für die Konvertierung zwischen Category Entity und CategoryDto.
 *
 * Implementiert das Mapper-Pattern zur Trennung von Domain-Objekten (Entities)
 * und Data Transfer Objects (DTOs). Diese Trennung bietet folgende Vorteile:
 *
 * - Kapselung: Interne Datenbankstruktur bleibt vor externen APIs verborgen
 * - Flexibilität: Entity und DTO können sich unabhängig voneinander entwickeln
 * - Sicherheit: Verhindert ungewollte Exposition sensibler Entity-Felder
 * - Wartbarkeit: Zentrale Konvertierungslogik an einem Ort
 *
 * Verwendet statische Methoden für bessere Performance und einfache Verwendung
 * ohne Instanziierung der Mapper-Klasse.
 */
public class CategoryMapper {

    /**
     * Konvertiert ein CategoryDto in eine Category Entity.
     *
     * Wird typischerweise verwendet wenn:
     * - Daten aus API-Requests (JSON) in Entities umgewandelt werden
     * - Vor dem Speichern in die Datenbank
     * - Im Service-Layer für Geschäftslogik
     *
     * @param categoryDto Das zu konvertierende DTO (aus API Request)
     * @return Category Entity für Datenbankoperationen
     * @throws NullPointerException wenn categoryDto null ist
     */
    public static Category mapToCategory(CategoryDto categoryDto){
        return new Category(
                categoryDto.id(),    // Record-Getter: automatisch generierte id() Methode
                categoryDto.name()   // Record-Getter: automatisch generierte name() Methode
        );
    }

    /**
     * Konvertiert eine Category Entity in ein CategoryDto.
     *
     * Wird typischerweise verwendet wenn:
     * - Daten aus der Datenbank für API-Responses aufbereitet werden
     * - Entities in JSON-serialisierbare Form gebracht werden
     * - Im Controller-Layer für HTTP-Responses
     *
     * @param category Die zu konvertierende Entity (aus Datenbank)
     * @return CategoryDto für API Response (wird zu JSON serialisiert)
     * @throws NullPointerException wenn category null ist
     */
    public static CategoryDto mapToCategoryDto(Category category){
        return new CategoryDto(
                category.getId(),    // Lombok-Getter: automatisch generierte getId() Methode
                category.getName()   // Lombok-Getter: automatisch generierte getName() Methode
        );
    }

    // Hinweis: In größeren Anwendungen könnte hier eine Bibliothek wie MapStruct
    // oder ModelMapper verwendet werden, um die Mapping-Logik automatisch zu generieren
    // und komplexere Transformationen zu handhaben.
}