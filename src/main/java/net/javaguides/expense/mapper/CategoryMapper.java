// src/main/java/net/javaguides/expense/mapper/CategoryMapper.java
package net.javaguides.expense.mapper;

import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.entity.Category;

public class CategoryMapper {

    public static Category mapToCategory(CategoryDto dto) {
        Category entity = new Category(dto.getId(), dto.getName());
        entity.setCreatedAt(dto.getCreatedAt()); // optional, meist nur bei Updates relevant
        return entity;
    }

    public static CategoryDto mapToCategoryDto(Category entity) {
        return new CategoryDto(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt() // ✅ jetzt mit createdAt
        );
    }
}