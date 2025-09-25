package net.javaguides.expense.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // 🔹 Standard-Konstruktor für JPA
    public Category() {
    }

    // 🔹 Konstruktor für Mapper/DTO
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // 🔹 Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
