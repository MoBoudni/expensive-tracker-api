package net.javaguides.expense.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.expense.dto.ExpenseDto;
import net.javaguides.expense.entity.Category;  // <- WICHTIG: Import für Category!
import net.javaguides.expense.entity.Expense;
import net.javaguides.expense.exception.ResourceNotFoundException;
import net.javaguides.expense.repository.ExpenseRepository;
import net.javaguides.expense.service.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // Standard: Read-Only, überschreibe bei Bedarf
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    @Transactional  // Schreib-Operation: Transaktion aktivieren
    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        Expense expense = mapToExpense(expenseDto);
        Expense saved = expenseRepository.save(expense);
        return mapToExpenseDto(saved);
    }

    @Override
    public ExpenseDto getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ausgabe nicht gefunden mit ID: " + id));
        return mapToExpenseDto(expense);
    }

    @Override
    public List<ExpenseDto> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::mapToExpenseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional  // Schreib-Operation
    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ausgabe nicht gefunden mit ID: " + id));

        existing.setTitle(expenseDto.getTitle());
        existing.setAmount(expenseDto.getAmount());
        existing.setExpenseDate(expenseDto.getExpenseDate());
        existing.setDescription(expenseDto.getDescription());

        // DEIN CODE HIER EINFÜGEN:
        if (expenseDto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(expenseDto.getCategoryId());
            existing.setCategory(category);
        }

        Expense updated = expenseRepository.save(existing);
        return mapToExpenseDto(updated);
    }

    @Override
    @Transactional  // Schreib-Operation
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ausgabe nicht gefunden mit ID: " + id);
        }
        expenseRepository.deleteById(id);
    }

    // Hilfsmethoden für Mapping (falls kein Mapper vorhanden)
    private ExpenseDto mapToExpenseDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setAmount(expense.getAmount());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setDescription(expense.getDescription());
        if (expense.getCategory() != null) {
            dto.setCategoryId(expense.getCategory().getId());
        }
        return dto;
    }

    private Expense mapToExpense(ExpenseDto dto) {
        Expense expense = new Expense();
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setDescription(dto.getDescription());
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            expense.setCategory(category);
        }
        return expense;
    }
}