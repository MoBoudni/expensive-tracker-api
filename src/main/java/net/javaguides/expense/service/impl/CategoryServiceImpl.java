package net.javaguides.expense.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.entity.Category;
import net.javaguides.expense.exception.ResourceNotFoundException;
import net.javaguides.expense.repository.CategoryRepository;
import net.javaguides.expense.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementierung des CategoryService-Interfaces.
 * Enthält die Geschäftslogik für Kategorien (CRUD).
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    /**
     * Repository für Datenbankzugriffe.
     */
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            // DTO zu Entity mappen (manuell)
            Category category = mapToCategory(categoryDto);
            Category saved = categoryRepository.save(category);
            return mapToCategoryDto(saved);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Erstellen der Kategorie: " + e.getMessage(), e);
        }
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie nicht gefunden mit ID: " + id));
        return mapToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> mapToCategoryDto(category))  // <- HIER: Lambda statt Methoden-Referenz (behebt den Fehler in Zeile 57!)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie nicht gefunden mit ID: " + id));

        // Felder aktualisieren (inkl. color, falls vorhanden)
        existing.setName(categoryDto.getName());
        if (categoryDto.getColor() != null) {
            existing.setColor(categoryDto.getColor());  // Annahme: Entity hat setColor()
        }

        Category updated = categoryRepository.save(existing);
        return mapToCategoryDto(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Kategorie nicht gefunden mit ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // Statische Hilfsmethoden für Mapping (manuell, ohne extra Mapper-Klasse)
    /**
     * Mappt eine Category-Entity zu einem CategoryDto.
     * @param category Die Entity.
     * @return Das DTO.
     */
    private static CategoryDto mapToCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        if (category.getColor() != null) {
            dto.setColor(category.getColor());
        }
        return dto;
    }

    /**
     * Mappt ein CategoryDto zu einer Category-Entity.
     * @param dto Das DTO.
     * @return Die Entity.
     */
    private static Category mapToCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        if (dto.getColor() != null) {
            category.setColor(dto.getColor());
        }
        return category;
    }
}