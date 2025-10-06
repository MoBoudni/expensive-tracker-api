// src/main/java/net/javaguides/expense/dto/CategorySummaryDto.java
package net.javaguides.expense.dto;

import java.math.BigDecimal;

public class CategorySummaryDto {
    private Long id;
    private String name;
    private long expenseCount;
    private BigDecimal totalAmount;

    public CategorySummaryDto(Long id, String name, long expenseCount, BigDecimal totalAmount) {
        this.id = id;
        this.name = name;
        this.expenseCount = expenseCount;
        this.totalAmount = totalAmount;
    }

    // Getter
    public Long getId() { return id; }
    public String getName() { return name; }
    public long getExpenseCount() { return expenseCount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}