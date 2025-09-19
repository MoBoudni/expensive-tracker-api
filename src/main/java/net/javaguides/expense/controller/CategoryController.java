package net.javaguides.expense.controller;

import lombok.AllArgsConstructor;
import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für die Verwaltung von Ausgaben-Kategorien.
 * Stellt HTTP-Endpunkte zur Verfügung für CRUD-Operationen (Create, Read, Update, Delete)
 * auf Kategorien über die REST API.
 *
 * Basis-URL: /api/categories
 */
@AllArgsConstructor                    // Lombok: Generiert Konstruktor mit allen Parametern (für Dependency Injection)
@RestController                        // Spring: Kombiniert @Controller und @ResponseBody - alle Methoden geben JSON/XML zurück
@RequestMapping("/api/categories")     // Spring: Definiert die Basis-URL für alle Endpunkte in diesem Controller
public class CategoryController {

    /**
     * Service-Layer für die Geschäftslogik der Kategorien.
     * Wird über Constructor Injection eingebunden (durch @AllArgsConstructor).
     */
    private CategoryService categoryService;

    /**
     * Erstellt eine neue Kategorie.
     *
     * HTTP POST /api/categories
     * Content-Type: application/json
     *
     * @param categoryDto Die zu erstellende Kategorie als JSON im Request Body
     * @return ResponseEntity mit der erstellten Kategorie und HTTP Status 201 (CREATED)
     */
    @PostMapping                       // Spring: Verarbeitet HTTP POST-Requests
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto){    // Spring: Konvertiert JSON aus Request Body zu CategoryDto

        CategoryDto category = categoryService.createCategory(categoryDto);

        // HTTP 201 CREATED mit der neu erstellten Kategorie zurückgeben
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    /**
     * Ruft eine spezifische Kategorie anhand ihrer ID ab.
     *
     * HTTP GET /api/categories/{id}
     * Beispiel: GET /api/categories/1
     *
     * @param categoryId Die ID der gesuchten Kategorie (aus der URL extrahiert)
     * @return ResponseEntity mit der gefundenen Kategorie und HTTP Status 200 (OK)
     */
    @GetMapping("{id}")                // Spring: Verarbeitet HTTP GET-Requests mit Pfadvariable
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") Long categoryId){
        // @PathVariable: Extrahiert den Wert {id} aus der URL und weist ihn categoryId zu
        CategoryDto category = categoryService.getCategoryById(categoryId);

        // HTTP 200 OK mit der gefundenen Kategorie
        return ResponseEntity.ok(category);
    }

    /**
     * Ruft alle vorhandenen Kategorien ab.
     *
     * HTTP GET /api/categories
     *
     * @return ResponseEntity mit Liste aller Kategorien und HTTP Status 200 (OK)
     */
    @GetMapping                        // Spring: Verarbeitet HTTP GET-Requests ohne Pfadvariable
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        List<CategoryDto> categories = categoryService.getAllCategories();

        // HTTP 200 OK mit der Liste aller Kategorien
        return ResponseEntity.ok(categories);
    }

    /**
     * Aktualisiert eine bestehende Kategorie.
     *
     * HTTP PUT /api/categories/{id}
     * Content-Type: application/json
     * Beispiel: PUT /api/categories/1
     *
     * @param categoryId Die ID der zu aktualisierenden Kategorie (aus URL)
     * @param categoryDto Die neuen Kategorie-Daten als JSON im Request Body
     * @return ResponseEntity mit der aktualisierten Kategorie und HTTP Status 200 (OK)
     */
    @PutMapping("{id}")                // Spring: Verarbeitet HTTP PUT-Requests für Updates
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") Long categoryId,
                                                      @RequestBody CategoryDto categoryDto){

        CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto);

        // HTTP 200 OK mit der aktualisierten Kategorie
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Löscht eine Kategorie anhand ihrer ID.
     *
     * HTTP DELETE /api/categories/{id}
     * Beispiel: DELETE /api/categories/1
     *
     * @param categoryId Die ID der zu löschenden Kategorie (aus URL)
     * @return ResponseEntity mit Erfolgsmeldung und HTTP Status 200 (OK)
     */
    @DeleteMapping("{id}")             // Spring: Verarbeitet HTTP DELETE-Requests
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId){

        categoryService.deleteCategory(categoryId);

        // HTTP 200 OK mit Bestätigungsnachricht
        return ResponseEntity.ok("Category deleted successfully.");
    }
}