// src/main/java/net/javaguides/expense/entity/Category.java
package net.javaguides.expense.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Standard-Konstruktor für JPA
    public Category() {}

    // Konstruktor für Mapper
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Füge hinzu:
    @Column(name = "color", length = 7)  // Für Hex-Codes wie "#1676F3"
    private String color;

    // Getter/Setter
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}