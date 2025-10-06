package net.javaguides.expense.mapper;

import net.javaguides.expense.dto.ExpenseDto;
import net.javaguides.expense.entity.Expense;
import net.javaguides.expense.entity.Category;

/**
 * Mapper-Utility-Klasse für die Konvertierung zwischen Expense-Entity und ExpenseDto.
 *
 * <p>Diese Klasse implementiert das Mapper-Pattern zur bidirektionalen Transformation
 * zwischen Datenbank-Entities und Data Transfer Objects (DTOs). Sie dient als zentrale
 * Stelle für die Konvertierungslogik und stellt sicher, dass die Zuordnung konsistent
 * und wartbar bleibt.</p>
 *
 * <h2>Design-Prinzipien:</h2>
 * <ul>
 *   <li><b>Stateless:</b> Alle Methoden sind statisch, kein Zustand wird gespeichert</li>
 *   <li><b>Single Responsibility:</b> Ausschließlich für Konvertierungen zuständig</li>
 *   <li><b>Bidirektional:</b> Unterstützt Entity → DTO und DTO → Entity</li>
 *   <li><b>Null-Safe:</b> Behandelt null-Werte korrekt</li>
 *   <li><b>Explizit:</b> Klare, verständliche Zuordnungen ohne "Magie"</li>
 * </ul>
 *
 * <h2>Verwendungszweck:</h2>
 * <p>Mapper werden benötigt, um eine klare Trennung zwischen verschiedenen
 * Anwendungsschichten zu gewährleisten:</p>
 * <ul>
 *   <li><b>Persistence Layer:</b> Arbeitet mit JPA Entities (Expense)</li>
 *   <li><b>Service Layer:</b> Arbeitet primär mit DTOs</li>
 *   <li><b>Controller/API Layer:</b> Arbeitet ausschließlich mit DTOs</li>
 * </ul>
 *
 * <h2>Vorteile gegenüber direkter Entity-Verwendung:</h2>
 * <ul>
 *   <li>Verhindert Lazy-Loading-Probleme in der API-Schicht</li>
 *   <li>Erlaubt unterschiedliche Repräsentationen (API vs. Datenbank)</li>
 *   <li>Schützt vor ungewollten Datenbank-Updates durch detached Entities</li>
 *   <li>Ermöglicht flexible API-Versionierung ohne DB-Schema-Änderungen</li>
 *   <li>Reduziert JSON-Serialisierungs-Komplexität</li>
 * </ul>
 *
 * <h2>Alternative Mapping-Frameworks:</h2>
 * <p>Für komplexere Szenarien könnten folgende Bibliotheken erwogen werden:</p>
 * <ul>
 *   <li><b>MapStruct:</b> Compile-time Code-Generierung, typsicher, performant</li>
 *   <li><b>ModelMapper:</b> Reflection-basiert, automatisches Mapping</li>
 *   <li><b>Orika:</b> Byte-Code-Generierung, gute Performance</li>
 * </ul>
 * <p>Für dieses Projekt ist eine manuelle Mapper-Klasse ausreichend und bietet
 * maximale Kontrolle und Transparenz.</p>
 *
 * @author JavaGuides Team
 * @version 1.0
 * @since 1.0
 * @see ExpenseDto
 * @see Expense
 * @see Category
 */
public final class ExpenseMapper {

    /**
     * Privater Konstruktor verhindert Instanziierung dieser Utility-Klasse.
     *
     * <p>Da alle Methoden statisch sind, gibt es keinen Grund, eine Instanz
     * dieser Klasse zu erstellen. Der private Konstruktor erzwingt dies zur
     * Compile-Zeit.</p>
     *
     * @throws UnsupportedOperationException wenn versucht wird, eine Instanz zu erstellen
     */
    private ExpenseMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Konvertiert ein ExpenseDto in eine Expense-Entity.
     *
     * <p>Diese Methode wird typischerweise verwendet, wenn Daten aus einem
     * API-Request in die Datenbank gespeichert werden sollen.</p>
     *
     * <h3>Mapping-Details:</h3>
     * <table border="1">
     *   <tr>
     *     <th>DTO-Feld</th>
     *     <th>Entity-Feld</th>
     *     <th>Transformation</th>
     *   </tr>
     *   <tr>
     *     <td>id</td>
     *     <td>id</td>
     *     <td>Direkte Zuweisung</td>
     *   </tr>
     *   <tr>
     *     <td>title</td>
     *     <td>title</td>
     *     <td>Direkte Zuweisung</td>
     *   </tr>
     *   <tr>
     *     <td>amount</td>
     *     <td>amount</td>
     *     <td>Direkte Zuweisung (BigDecimal)</td>
     *   </tr>
     *   <tr>
     *     <td>expenseDate</td>
     *     <td>expenseDate</td>
     *     <td>Direkte Zuweisung (LocalDate)</td>
     *   </tr>
     *   <tr>
     *     <td>description</td>
     *     <td>description</td>
     *     <td>Direkte Zuweisung (kann null sein)</td>
     *   </tr>
     *   <tr>
     *     <td>categoryId</td>
     *     <td>category</td>
     *     <td>Erstellt neue Category-Instanz mit ID</td>
     *   </tr>
     * </table>
     *
     * <h3>Wichtige Hinweise:</h3>
     * <ul>
     *   <li><b>Category-Objekt:</b> Es wird nur eine Category-Instanz mit ID erstellt.
     *       Die vollständige Category muss separat aus der DB geladen werden.</li>
     *   <li><b>Lazy-Loading:</b> Die Category wird als Proxy-Objekt behandelt und bei
     *       Bedarf von Hibernate geladen.</li>
     *   <li><b>Null-ID:</b> Bei neuen Ausgaben (dto.id == null) wird die ID von der
     *       Datenbank generiert.</li>
     * </ul>
     *
     * <h3>Verwendungsbeispiel:</h3>
     * <pre>
     * ExpenseDto dto = new ExpenseDto(null, "Einkauf",
     *                                  BigDecimal.valueOf(45.99),
     *                                  LocalDate.now(), "Test", 1L);
     * Expense entity = ExpenseMapper.mapToExpense(dto);
     * expenseRepository.save(entity);
     * </pre>
     *
     * @param dto Das zu konvertierende ExpenseDto. Darf nicht {@code null} sein.
     * @return Eine neue Expense-Entity mit den Daten aus dem DTO
     * @throws NullPointerException wenn dto {@code null} ist
     * @see #mapToExpenseDto(Expense)
     * @see Expense#setCategory(Category)
     */
    public static Expense mapToExpense(ExpenseDto dto) {
        Expense entity = new Expense();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setAmount(dto.getAmount());
        entity.setExpenseDate(dto.getExpenseDate());
        entity.setDescription(dto.getDescription());

        // Erstelle eine Category-Instanz nur mit der ID für die Beziehung
        // Hibernate wird diese als Proxy-Objekt behandeln und bei Bedarf laden
        Category category = new Category();
        category.setId(dto.getCategoryId());
        entity.setCategory(category);

        return entity;
    }

    /**
     * Konvertiert eine Expense-Entity in ein ExpenseDto.
     *
     * <p>Diese Methode wird typischerweise verwendet, wenn Daten aus der Datenbank
     * für die API-Response oder Service-Verarbeitung benötigt werden.</p>
     *
     * <h3>Mapping-Details:</h3>
     * <table border="1">
     *   <tr>
     *     <th>Entity-Feld</th>
     *     <th>DTO-Feld</th>
     *     <th>Transformation</th>
     *   </tr>
     *   <tr>
     *     <td>id</td>
     *     <td>id</td>
     *     <td>Direkte Zuweisung</td>
     *   </tr>
     *   <tr>
     *     <td>title</td>
     *     <td>title</td>
     *     <td>Direkte Zuweisung</td>
     *   </tr>
     *   <tr>
     *     <td>amount</td>
     *     <td>amount</td>
     *     <td>Direkte Zuweisung (BigDecimal)</td>
     *   </tr>
     *   <tr>
     *     <td>expenseDate</td>
     *     <td>expenseDate</td>
     *     <td>Direkte Zuweisung (LocalDate)</td>
     *   </tr>
     *   <tr>
     *     <td>description</td>
     *     <td>description</td>
     *     <td>Direkte Zuweisung (kann null sein)</td>
     *   </tr>
     *   <tr>
     *     <td>category.id</td>
     *     <td>categoryId</td>
     *     <td>Extrahiert ID aus Category-Objekt</td>
     *   </tr>
     * </table>
     *
     * <h3>Wichtige Hinweise:</h3>
     * <ul>
     *   <li><b>Category-ID:</b> Nur die ID der Kategorie wird übertragen, nicht das
     *       gesamte Category-Objekt. Dies vermeidet zirkuläre Referenzen und reduziert
     *       die Payload-Größe.</li>
     *   <li><b>Lazy-Loading:</b> Falls die Category lazy geladen ist und noch nicht
     *       initialisiert wurde, könnte der Zugriff auf category.getId() eine
     *       LazyInitializationException auslösen, wenn außerhalb einer Transaktion
     *       aufgerufen.</li>
     *   <li><b>Detached Entities:</b> Das resultierende DTO ist vollständig vom
     *       Persistence Context getrennt und kann sicher außerhalb von Transaktionen
     *       verwendet werden.</li>
     * </ul>
     *
     * <h3>Verwendungsbeispiel:</h3>
     * <pre>
     * Expense entity = expenseRepository.findById(1L).orElseThrow();
     * ExpenseDto dto = ExpenseMapper.mapToExpenseDto(entity);
     * return ResponseEntity.ok(dto); // Sicher für JSON-Serialisierung
     * </pre>
     *
     * @param entity Die zu konvertierende Expense-Entity. Darf nicht {@code null} sein.
     * @return Ein neues ExpenseDto mit den Daten aus der Entity
     * @throws NullPointerException wenn entity {@code null} ist
     * @throws org.hibernate.LazyInitializationException wenn die Category lazy ist
     *         und außerhalb einer aktiven Session/Transaktion darauf zugegriffen wird
     * @see #mapToExpense(ExpenseDto)
     * @see Expense#getCategory()
     */
    public static ExpenseDto mapToExpenseDto(Expense entity) {
        return new ExpenseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getAmount(),
                entity.getExpenseDate(),
                entity.getDescription(),
                entity.getCategory().getId()  // Extrahiere nur die Category-ID
        );
    }
}