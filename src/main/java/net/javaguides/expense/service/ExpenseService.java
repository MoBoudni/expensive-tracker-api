package net.javaguides.expense.service;

import net.javaguides.expense.dto.ExpenseDto;
import java.util.List;

/**
 * Service-Interface für Ausgaben-Verwaltung.
 *
 * <p>Dieses Interface definiert die CRUD-Operationen (Create, Read, Update, Delete)
 * für Ausgaben-Entities. Es dient als Vertrag für die Implementierung und ermöglicht
 * Dependency Injection in Controllern und Views.</p>
 *
 * <h2>Verwendung:</h2>
 * <ul>
 *   <li>In Controllern: {@code @Autowired private ExpenseService expenseService;}</li>
 *   <li>Implementierung: {@code public class ExpenseServiceImpl implements ExpenseService}</li>
 * </ul>
 *
 * @author Dein Name / xAI-Assistent
 * @version 1.0
 * @since 1.0
 * @see ExpenseDto
 * @see ExpenseServiceImpl
 */
public interface ExpenseService {

    /**
     * Erstellt eine neue Ausgabe im System.
     *
     * <p>Validiert die Eingabedaten und speichert sie in der Datenbank.
     * Die ID wird automatisch generiert.</p>
     *
     * @param expenseDto Die zu erstellende Ausgabe als DTO.
     *                   Muss alle required Felder enthalten (title, amount, expenseDate, categoryId).
     * @return Die erstellte Ausgabe als DTO (inkl. generierter ID).
     * @throws IllegalArgumentException wenn Validierung fehlschlägt.
     * @throws RuntimeException bei internen Fehlern (z. B. DB-Verbindung).
     */
    ExpenseDto createExpense(ExpenseDto expenseDto);

    /**
     * Ruft eine spezifische Ausgabe anhand ihrer ID ab.
     *
     * <p>Sucht in der Datenbank nach der Ausgabe. Wenn nicht gefunden,
     * wird eine ResourceNotFoundException geworfen.</p>
     *
     * @param id Die eindeutige ID der gesuchten Ausgabe (muss > 0 sein).
     * @return Die gefundene Ausgabe als DTO.
     * @throws ResourceNotFoundException wenn keine Ausgabe mit der ID existiert.
     */
    ExpenseDto getExpenseById(Long id);

    /**
     * Ruft alle im System registrierten Ausgaben ab.
     *
     * <p>Gibt eine Liste aller Ausgaben zurück, sortiert nach Erstellungsdatum
     * (neueste zuerst). Bei leerer Datenbank wird eine leere Liste zurückgegeben.</p>
     *
     * @return Eine Liste aller Ausgaben als DTOs (nie null, kann leer sein).
     */
    List<ExpenseDto> getAllExpenses();

    /**
     * Aktualisiert eine existierende Ausgabe vollständig.
     *
     * <p>Überschreibt alle Felder der Ausgabe mit den neuen Werten aus dem DTO.
     * Die ID im Pfad muss mit einer existierenden Ausgabe übereinstimmen.</p>
     *
     * @param id Die ID der zu aktualisierenden Ausgabe.
     * @param expenseDto Die neuen Ausgabedaten (muss required Felder enthalten).
     * @return Die aktualisierte Ausgabe als DTO.
     * @throws ResourceNotFoundException wenn keine Ausgabe mit der ID existiert.
     * @throws IllegalArgumentException wenn Validierung fehlschlägt.
     */
    ExpenseDto updateExpense(Long id, ExpenseDto expenseDto);

    /**
     * Löscht eine Ausgabe permanent aus dem System.
     *
     * <p>Entfernt die Ausgabe mit der angegebenen ID aus der Datenbank.
     * Diese Operation kann nicht rückgängig gemacht werden.
     * Referenzierte Kategorien bleiben erhalten.</p>
     *
     * @param id Die ID der zu löschenden Ausgabe.
     * @throws ResourceNotFoundException wenn keine Ausgabe mit der ID existiert.
     */
    void deleteExpense(Long id);
}