package net.javaguides.expense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception für nicht gefundene Ressourcen.
 *
 * <p>Diese Exception wird geworfen, wenn eine angeforderte Ressource
 * (Expense, Category, etc.) nicht in der Datenbank gefunden werden kann.
 * Sie signalisiert einen HTTP 404 (NOT FOUND) Status.</p>
 *
 * <h2>Verwendungszweck:</h2>
 * <ul>
 *   <li>Einheitliche Exception für alle "nicht gefunden" Szenarien</li>
 *   <li>Automatische HTTP-Status-Zuordnung via @ResponseStatus</li>
 *   <li>Klare Trennung von technischen (SQLException) und Business-Exceptions</li>
 *   <li>Bessere API-Semantik und Client-Fehlermeldungen</li>
 * </ul>
 *
 * <h2>Spring Boot Exception Handling:</h2>
 * <p>Spring Boot behandelt diese Exception automatisch durch:</p>
 * <ol>
 *   <li>@ResponseStatus Annotation → HTTP 404 Response</li>
 *   <li>Exception Message → Response Body (via DefaultErrorAttributes)</li>
 *   <li>Globaler ExceptionHandler kann Format anpassen (optional)</li>
 * </ol>
 *
 * <h2>Design-Pattern:</h2>
 * <p>Diese Klasse folgt dem <b>Exception Translation Pattern</b>:</p>
 * <ul>
 *   <li>Technische Exceptions (EmptyResultDataAccessException) werden
 *       in Business-Exceptions (ResourceNotFoundException) übersetzt</li>
 *   <li>Vereinfacht Exception-Handling im Controller</li>
 *   <li>Entkoppelt Service-Layer von Persistence-Details</li>
 * </ul>
 *
 * <h2>Alternative zu @ResponseStatus:</h2>
 * <p>Für komplexere Error-Responses kann ein @ControllerAdvice verwendet werden:</p>
 * <pre>
 * {@code @ControllerAdvice}
 * public class GlobalExceptionHandler {
 *     {@code @ExceptionHandler}(ResourceNotFoundException.class)
 *     public ResponseEntity&lt;ErrorResponse&gt; handleResourceNotFound(
 *             ResourceNotFoundException ex) {
 *         ErrorResponse error = new ErrorResponse(
 *             404,
 *             ex.getMessage(),
 *             LocalDateTime.now()
 *         );
 *         return new ResponseEntity&lt;&gt;(error, HttpStatus.NOT_FOUND);
 *     }
 * }
 * </pre>
 *
 * <h2>Verwendungsbeispiele:</h2>
 *
 * <h3>Im Service:</h3>
 * <pre>
 * public ExpenseDto getExpenseById(Long id) {
 *     Expense expense = repository.findById(id)
 *         .orElseThrow(() -&gt; new ResourceNotFoundException(
 *             "Expense not found with ID: " + id
 *         ));
 *     return mapper.toDto(expense);
 * }
 * </pre>
 *
 * <h3>API Response bei Exception:</h3>
 * <pre>
 * HTTP/1.1 404 Not Found
 * Content-Type: application/json
 *
 * {
 *   "timestamp": "2025-10-03T14:30:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Expense not found with ID: 42",
 *   "path": "/api/expenses/42"
 * }
 * </pre>
 *
 * @author JavaGuides Team
 * @version 1.0
 * @since 1.0
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see org.springframework.http.HttpStatus
 * @see RuntimeException
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Serial Version UID für Serialisierung.
     *
     * <p>Erforderlich weil Exception implementiert Serializable.
     * Sollte bei Änderungen der Klassenstruktur aktualisiert werden.</p>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Erstellt eine neue ResourceNotFoundException mit einer Fehlermeldung.
     *
     * <p>Dies ist der Hauptkonstruktor für die Exception. Die Nachricht
     * sollte beschreibend sein und dem API-Consumer helfen, das Problem
     * zu verstehen.</p>
     *
     * <h3>Best Practices für Messages:</h3>
     * <ul>
     *   <li>Beschreibend: "Expense not found with ID: 42"</li>
     *   <li>Nicht: "Error" oder "Not found" (zu generisch)</li>
     *   <li>Inkludiere relevante IDs/Parameter für Debugging</li>
     *   <li>Keine technischen Stack-Trace-Details</li>
     *   <li>Keine sensiblen Informationen (Passwörter, Tokens, etc.)</li>
     * </ul>
     *
     * <h3>Verwendungsbeispiel:</h3>
     * <pre>
     * throw new ResourceNotFoundException("Category not found with ID: " + categoryId);
     * </pre>
     *
     * @param message Die Fehlermeldung, die die nicht gefundene Ressource beschreibt.
     *                Wird im HTTP Response Body zurückgegeben. Sollte nicht null sein.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Erstellt eine neue ResourceNotFoundException mit Nachricht und Ursache.
     *
     * <p>Dieser Konstruktor sollte verwendet werden, wenn die ResourceNotFoundException
     * eine andere Exception wrappen soll (Exception Chaining). Dies erhält die
     * ursprüngliche Exception für Logging und Debugging.</p>
     *
     * <h3>Wann verwenden?</h3>
     * <ul>
     *   <li>Beim Übersetzen von Persistence-Exceptions</li>
     *   <li>Wenn die ursprüngliche Exception zusätzlichen Kontext bietet</li>
     *   <li>Für detailliertes Logging im ExceptionHandler</li>
     * </ul>
     *
     * <h3>Verwendungsbeispiel:</h3>
     * <pre>
     * try {
     *     // Datenbankzugriff
     * } catch (DataAccessException e) {
     *     throw new ResourceNotFoundException(
     *         "Expense not found with ID: " + id,
     *         e  // Ursprüngliche Exception für Stack Trace
     *     );
     * }
     * </pre>
     *
     * <h3>Exception Chaining Vorteil:</h3>
     * <pre>
     * ResourceNotFoundException: Expense not found with ID: 42
     *   at ExpenseServiceImpl.getExpenseById(...)
     * Caused by: EmptyResultDataAccessException: No entity found
     *   at JpaRepository.findById(...)
     * </pre>
     *
     * @param message Die Fehlermeldung für diese Exception
     * @param cause Die ursprüngliche Exception, die zu diesem Fehler führte.
     *              Kann null sein, wird aber normalerweise gesetzt.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}