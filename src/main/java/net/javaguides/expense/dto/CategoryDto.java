package net.javaguides.expense.dto;

/**
 * Data Transfer Object (DTO) für Kategorie-Daten.
 *
 * Verwendet Java Record (seit Java 14) für eine kompakte, unveränderliche Datenstruktur.
 * Records generieren automatisch:
 * - Konstruktor mit allen Parametern
 * - Getter-Methoden (id(), name())
 * - equals(), hashCode() und toString() Methoden
 * - Alle Felder sind automatisch final (unveränderlich)
 *
 * DTOs dienen zur Datenübertragung zwischen verschiedenen Schichten der Anwendung:
 * - Controller <-> Service Layer
 * - API Requests/Responses (JSON Serialization/Deserialization)
 * - Trennung der internen Entity-Struktur von der externen API-Darstellung
 *
 * Vorteile der Verwendung von DTOs:
 * - Kapselung: Interne Entity-Details bleiben verborgen
 * - Flexibilität: API kann sich unabhängig von der Datenbankstruktur entwickeln
 * - Sicherheit: Verhindert Over-Posting und unerwünschte Datenexposition
 * - Performance: Nur benötigte Felder werden übertragen
 */
public record CategoryDto(
        /**
         * Eindeutige Identifikationsnummer der Kategorie.
         * Entspricht dem Primärschlüssel in der Datenbank.
         * Kann null sein bei neuen Kategorien (vor dem Speichern).
         */
        Long id,

        /**
         * Name der Kategorie (z.B. "Lebensmittel", "Transport", "Unterhaltung").
         * Darf nicht null oder leer sein.
         * Muss eindeutig sein innerhalb der Anwendung.
         */
        String name
) {
    // Record-Body kann zusätzliche Methoden oder Validierungen enthalten,
    // ist hier aber leer, da die Standard-Implementierung ausreichend ist
}