// src/main/java/net/javaguides/expense/dto/CategoryDto.java
package net.javaguides.expense.dto;

import java.time.LocalDateTime;

public class CategoryDto {
    private Long id;
    private String name;
    private String color; // Neu: Hex-Code, z.B. "#1676F3"
    private LocalDateTime createdAt; // ✅ hinzugefügt

    // 🔸 Parameterloser Konstruktor (wichtig für Vaadin & Jackson)
    public CategoryDto() {}

    // 🔸 Konstruktor mit Parametern
    public CategoryDto(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    // 🔸 Getter
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // 🔸 Setter
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setColor(String color) { this.color = color; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}