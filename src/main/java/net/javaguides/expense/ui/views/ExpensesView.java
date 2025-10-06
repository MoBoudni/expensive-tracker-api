package net.javaguides.expense.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.dto.ExpenseDto;
import net.javaguides.expense.ui.MainLayout;

import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Ausgaben | Expense Tracker")
@Route(value = "expenses", layout = MainLayout.class)
public class ExpensesView extends VerticalLayout {

    private Grid<ExpenseDto> grid = new Grid<>(ExpenseDto.class, false);
    private Button addExpenseButton;

    public ExpensesView() {
        addClassName("expenses-view");
        setSizeFull();
        configureGrid();
        addExpenseButton = new Button("Neue Ausgabe");
        addExpenseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addExpenseButton.setIcon(new Icon(VaadinIcon.PLUS));
        addExpenseButton.addClickListener(e -> openExpenseForm(new ExpenseDto()));
        H3 viewTitle = new H3("Ausgaben verwalten");
        viewTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);
        HorizontalLayout toolbar = new HorizontalLayout(viewTitle, addExpenseButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.addClassNames(LumoUtility.Padding.MEDIUM);
        add(toolbar, grid);
    }

    private void configureGrid() {
        grid.addClassNames("expenses-grid");
        grid.setSizeFull();
        grid.addColumn(ExpenseDto::getTitle).setHeader("Titel").setAutoWidth(true);
        grid.addColumn(ExpenseDto::getAmount).setHeader("Betrag (€)").setAutoWidth(true);

        // ✅ Korrektur: Verwende String-Format statt DateTimeFormatter
        grid.addColumn(new LocalDateRenderer<>(ExpenseDto::getExpenseDate, "dd.MM.yyyy"))
                .setHeader("Datum").setAutoWidth(true);

        grid.addColumn(ExpenseDto::getDescription).setHeader("Beschreibung").setAutoWidth(true);
        grid.addComponentColumn(expense -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> openExpenseForm(expense));
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(e -> deleteExpense(expense));
            actions.add(editButton, deleteButton);
            return actions;
        }).setHeader("Aktionen").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        // In einer echten Anwendung würden hier Daten vom Service geladen
        // grid.setItems(expenseService.getAllExpenses());
    }

    private void openExpenseForm(ExpenseDto expense) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(expense.getId() == null ? "Neue Ausgabe erstellen" : "Ausgabe bearbeiten");
        FormLayout formLayout = new FormLayout();
        TextField titleField = new TextField("Titel");
        titleField.setRequired(true);
        titleField.setValue(expense.getTitle() != null ? expense.getTitle() : "");
        NumberField amountField = new NumberField("Betrag (€)");
        amountField.setRequiredIndicatorVisible(true);
        amountField.setValue(expense.getAmount() != null ? expense.getAmount().doubleValue() : null);
        DatePicker datePicker = new DatePicker("Datum");
        datePicker.setRequiredIndicatorVisible(true);
        datePicker.setValue(expense.getExpenseDate());
        TextArea descriptionField = new TextArea("Beschreibung");
        descriptionField.setValue(expense.getDescription() != null ? expense.getDescription() : "");
        Select<CategoryDto> categorySelect = new Select<>();
        categorySelect.setLabel("Kategorie");
        categorySelect.setRequiredIndicatorVisible(true);
        categorySelect.setItemLabelGenerator(CategoryDto::getName);
        // In einer echten Anwendung würden hier Kategorien vom Service geladen
        // categorySelect.setItems(categoryService.getAllCategories());
        // if (expense.getCategoryId() != null) {
        //     categorySelect.setValue(categoryService.getCategoryById(expense.getCategoryId()));
        // }
        formLayout.add(titleField, amountField, datePicker, categorySelect, descriptionField);
        formLayout.setColspan(descriptionField, 2);
        Button saveButton = new Button("Speichern");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            // In einer echten Anwendung würden hier die Daten gespeichert
            // expense.setTitle(titleField.getValue());
            // expense.setAmount(BigDecimal.valueOf(amountField.getValue()));
            // expense.setExpenseDate(datePicker.getValue());
            // expense.setDescription(descriptionField.getValue());
            // expense.setCategoryId(categorySelect.getValue().getId());
            // expenseService.saveExpense(expense);
            Notification.show("Ausgabe gespeichert", 3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            dialog.close();
        });
        Button cancelButton = new Button("Abbrechen", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteExpense(ExpenseDto expense) {
        // In einer echten Anwendung würde hier die Ausgabe gelöscht
        // expenseService.deleteExpense(expense.getId());
        Notification.show("Ausgabe gelöscht", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}