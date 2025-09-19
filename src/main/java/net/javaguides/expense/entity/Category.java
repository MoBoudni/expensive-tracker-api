package net.javaguides.expense.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Entity-Klasse für Ausgaben-Kategorien.
 * Repräsentiert eine Kategorie in der Ausgabenverwaltung (z.B. "Lebensmittel", "Transport", etc.)
 * Diese Klasse wird von JPA/Hibernate verwendet, um die Datenbank-Tabelle "categories" zu verwalten.
 */
@Setter                       // Lombok: Generiert automatisch Setter-Methoden für alle Felder
@Getter                       // Lombok: Generiert automatisch Getter-Methoden für alle Felder
@NoArgsConstructor            // Lombok: Generiert automatisch einen parameterlosen Konstruktor
@AllArgsConstructor           // Lombok: Generiert automatisch einen Konstruktor mit allen Parametern
@Entity                       // JPA: Markiert diese Klasse als Datenbank-Entity
@Table(name = "categories")   // JPA: Definiert den Namen der Datenbank-Tabelle
public class Category {

    /**
     * Eindeutige ID der Kategorie (Primärschlüssel)
     * Wird automatisch von der Datenbank generiert
     */
    @Id                                                  // JPA: Markiert dieses Feld als Primärschlüssel
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // JPA: Auto-Increment-Strategie für MySQL
    private Long id;

    /**
     * Name der Kategorie (z.B. "Lebensmittel", "Transport")
     * Darf nicht null sein und muss eindeutig in der Datenbank sein
     */
    @Column(nullable = false, unique = true)             // JPA: Spalten-Definition - nicht null und eindeutig
    private String name;
}
