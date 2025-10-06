package net.javaguides.expense.service;

import net.javaguides.expense.dto.CategoryDto;
import java.util.List;

/**
 * Service-Interface für Kategorien-Verwaltung.
 * Definiert die CRUD-Operationen.
 */
public interface CategoryService {

    /**
     * Erstellt eine neue Kategorie.
     * @param categoryDto Die Kategorie-Daten.
     * @return Die erstellte Kategorie.
     */
    CategoryDto createCategory(CategoryDto categoryDto);

    /**
     * Holt eine Kategorie per ID.
     * @param id Die Kategorie-ID.
     * @return Die Kategorie oder null, wenn nicht gefunden.
     */
    CategoryDto getCategoryById(Long id);

    /**
     * Holt alle Kategorien.
     * @return Liste aller Kategorien.
     */
    List<CategoryDto> getAllCategories();

    /**
     * Aktualisiert eine Kategorie.
     * @param id Die Kategorie-ID.
     * @param categoryDto Die aktualisierten Daten.
     * @return Die aktualisierte Kategorie.
     */
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    /**
     * Löscht eine Kategorie per ID.
     * @param id Die Kategorie-ID.
     */
    void deleteCategory(Long id);
}