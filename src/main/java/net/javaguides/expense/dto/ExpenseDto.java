package net.javaguides.expense.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) für Ausgaben-Daten.
 *
 * <p>Diese Klasse dient als Datencontainer für die Übertragung von Ausgabeinformationen
 * zwischen verschiedenen Schichten der Anwendung:</p>
 * <ul>
 *   <li>Controller ↔ Service Layer</li>
 *   <li>API Requests/Responses (JSON Serialization/Deserialization)</li>
 *   <li>Client ↔ Server Kommunikation</li>
 * </ul>
 *
 * <h2>Design-Prinzipien:</h2>
 * <ul>
 *   <li><b>Trennung von Concerns:</b> Isoliert API-Struktur von interner Entity-Struktur</li>
 *   <li><b>Kapselung:</b> Verhindert direkte Exposition von Entity-Details</li>
 *   <li><b>Validierung:</b> Bean Validation Constraints für Datenintegrität</li>
 *   <li><b>Flexibilität:</b> API kann sich unabhängig von der Datenbank entwickeln</li>
 *   <li><b>Sicherheit:</b> Verhindert Over-Posting und Mass Assignment Vulnerabilities</li>
 * </ul>
 *
 * <h2>JSON Beispiel:</h2>
 * <pre>
 * {
 *   "id": 1,
 *   "title": "Wocheneinkauf Supermarkt",
 *   "amount": 89.50,
 *   "expenseDate": "2025-10-03",
 *   "description": "Lebensmittel und Haushaltswaren",
 *   "categoryId": 1
 * }
 * </pre>
 *
 * @author JavaGuides Team
 * @version 1.0
 * @since 1.0
 * @see net.javaguides.expense.entity.Expense
 * @see net.javaguides.expense.mapper.ExpenseMapper
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {

    /**
     * Eindeutige Identifikationsnummer der Ausgabe.
     *
     * <p>Entspricht dem Primärschlüssel in der Datenbank-Tabelle {@code expenses}.
     * Dieser Wert wird automatisch von der Datenbank generiert (Auto-Increment).</p>
     *
     * <h3>Verwendung:</h3>
     * <ul>
     *   <li><b>null:</b> Bei neuen Ausgaben (vor dem Speichern)</li>
     *   <li><b>Nicht-null:</b> Bei existierenden Ausgaben (nach dem Speichern/Laden)</li>
     * </ul>
     *
     * @see net.javaguides.expense.entity.Expense#id
     */
    private Long id;

    /**
     * Titel/Bezeichnung der Ausgabe.
     *
     * <p>Kurze, prägnante Beschreibung der Ausgabe. Sollte aussagekräftig genug sein,
     * um die Ausgabe auf einen Blick zu identifizieren.</p>
     *
     * <h3>Validierungsregeln:</h3>
     * <ul>
     *   <li>Darf nicht null oder leer sein</li>
     *   <li>Maximale Länge: 150 Zeichen</li>
     *   <li>Whitespace-Only Strings sind nicht erlaubt</li>
     * </ul>
     *
     * <h3>Beispiele:</h3>
     * <ul>
     *   <li>"Einkauf Lidl"</li>
     *   <li>"Tankstelle Shell A3"</li>
     *   <li>"Kinobesuch Avatar 2"</li>
     * </ul>
     *
     * @see jakarta.validation.constraints.NotBlank
     * @see jakarta.validation.constraints.Size
     */
    @NotBlank(message = "Titel darf nicht leer sein")
    @Size(max = 150, message = "Titel darf maximal 150 Zeichen lang sein")
    private String title;

    /**
     * Betrag der Ausgabe in Euro.
     *
     * <p>Monetärer Wert der Ausgabe mit zwei Dezimalstellen Genauigkeit.
     * Verwendet {@link BigDecimal} für präzise Geldberechnungen ohne Rundungsfehler.</p>
     *
     * <h3>Validierungsregeln:</h3>
     * <ul>
     *   <li>Darf nicht null sein</li>
     *   <li>Muss positiv sein (größer als 0)</li>
     *   <li>Maximal 8 Vorkommastellen, 2 Nachkommastellen</li>
     * </ul>
     *
     * <h3>Datenbankformat:</h3>
     * <code>DECIMAL(10,2)</code> - Erlaubt Werte bis 99.999.999,99 €
     *
     * <h3>Warum BigDecimal?</h3>
     * <ul>
     *   <li>Keine Rundungsfehler bei Geldberechnungen</li>
     *   <li>Exakte Dezimaldarstellung</li>
     *   <li>Standard für Finanzanwendungen</li>
     * </ul>
     *
     * @see java.math.BigDecimal
     * @see jakarta.validation.constraints.NotNull
     * @see jakarta.validation.constraints.Positive
     * @see jakarta.validation.constraints.Digits
     */
    @NotNull(message = "Betrag darf nicht null sein")
    @Positive(message = "Betrag muss größer als 0 sein")
    @Digits(integer = 8, fraction = 2, message = "Betrag darf maximal 8 Vorkomma- und 2 Nachkommastellen haben")
    private BigDecimal amount;

    /**
     * Datum der Ausgabe.
     *
     * <p>Repräsentiert den Tag, an dem die Ausgabe tatsächlich getätigt wurde.
     * Verwendet {@link LocalDate} aus der Java 8+ Time API für zeitzonenunabhängige Datumsangaben.</p>
     *
     * <h3>Validierungsregeln:</h3>
     * <ul>
     *   <li>Darf nicht null sein</li>
     *   <li>Darf nicht in der Zukunft liegen</li>
     * </ul>
     *
     * <h3>JSON-Format:</h3>
     * <code>yyyy-MM-dd</code> (ISO 8601) - Beispiel: "2025-10-03"
     *
     * <h3>Warum LocalDate?</h3>
     * <ul>
     *   <li>Keine Zeitzone nötig für Ausgabedaten</li>
     *   <li>Immutable und Thread-Safe</li>
     *   <li>Teil der modernen Java Time API</li>
     * </ul>
     *
     * @see java.time.LocalDate
     * @see com.fasterxml.jackson.annotation.JsonFormat
     * @see jakarta.validation.constraints.NotNull
     * @see jakarta.validation.constraints.PastOrPresent
     */
    @NotNull(message = "Ausgabedatum darf nicht null sein")
    @PastOrPresent(message = "Ausgabedatum darf nicht in der Zukunft liegen")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expenseDate;

    /**
     * Optionale Beschreibung/Notiz zur Ausgabe.
     *
     * <p>Detaillierte Freitext-Beschreibung für zusätzliche Informationen zur Ausgabe.
     * Kann verwendet werden für Notizen, Begründungen oder weitere Details.</p>
     *
     * <h3>Eigenschaften:</h3>
     * <ul>
     *   <li>Optional - kann null oder leer sein</li>
     *   <li>Maximale Länge: 500 Zeichen</li>
     *   <li>Unterstützt Mehrzeiligkeit</li>
     * </ul>
     *
     * <h3>Verwendungszwecke:</h3>
     * <ul>
     *   <li>Zusätzliche Details zur Ausgabe</li>
     *   <li>Begründung für ungewöhnlich hohe Beträge</li>
     *   <li>Erinnerungshilfen</li>
     *   <li>Kontextinformationen</li>
     * </ul>
     *
     * <h3>Beispiel:</h3>
     * <code>"Wocheneinkauf mit Getränken und Reinigungsmitteln. Sonderangebot genutzt."</code>
     *
     * @see jakarta.validation.constraints.Size
     */
    @Size(max = 500, message = "Beschreibung darf maximal 500 Zeichen lang sein")
    private String description;

    /**
     * Fremdschlüssel zur zugehörigen Kategorie.
     *
     * <p>Referenziert die ID einer Kategorie aus der {@code categories} Tabelle.
     * Jede Ausgabe muss genau einer Kategorie zugeordnet sein.</p>
     *
     * <h3>Validierungsregeln:</h3>
     * <ul>
     *   <li>Darf nicht null sein</li>
     *   <li>Muss eine existierende Kategorie-ID sein</li>
     * </ul>
     *
     * <h3>Beziehung:</h3>
     * <ul>
     *   <li><b>Typ:</b> Many-to-One (viele Ausgaben → eine Kategorie)</li>
     *   <li><b>Referenz:</b> categories.id</li>
     *   <li><b>Constraint:</b> FOREIGN KEY mit ON DELETE RESTRICT</li>
     * </ul>
     *
     * <h3>Kategoriebeispiele:</h3>
     * <ul>
     *   <li>1 = Lebensmittel</li>
     *   <li>2 = Transport</li>
     *   <li>3 = Freizeit</li>
     *   <li>4 = Gesundheit</li>
     * </ul>
     *
     * <h3>Wichtig:</h3>
     * <p>Die Kategorie kann nicht gelöscht werden, solange Ausgaben darauf referenzieren.
     * Dies wird durch {@code ON DELETE RESTRICT} in der Datenbank erzwungen.</p>
     *
     * @see net.javaguides.expense.entity.Category
     * @see net.javaguides.expense.dto.CategoryDto
     * @see jakarta.validation.constraints.NotNull
     */
    @NotNull(message = "Kategorie-ID darf nicht null sein")
    private Long categoryId;
}