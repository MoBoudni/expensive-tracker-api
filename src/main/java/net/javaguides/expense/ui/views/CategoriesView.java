package net.javaguides.expense.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.ui.MainLayout;

import java.time.format.DateTimeFormatter;

@PageTitle("Kategorien | Expense Tracker")
@Route(value = "categories", layout = MainLayout.class)
public class CategoriesView extends VerticalLayout {

    private Grid<CategoryDto> grid = new Grid<>(CategoryDto.class, false);
    private Button addCategoryButton;

    public CategoriesView() {
        addClassName("categories-view");
        setSizeFull();
        configureGrid();

        addCategoryButton = new Button("Neue Kategorie");
        addCategoryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addCategoryButton.setIcon(new Icon(VaadinIcon.PLUS));
        addCategoryButton.addClickListener(e -> openCategoryForm(new CategoryDto()));

        H3 viewTitle = new H3("Kategorien verwalten");
        viewTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);

        HorizontalLayout toolbar = new HorizontalLayout(viewTitle, addCategoryButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.addClassNames(LumoUtility.Padding.MEDIUM);

        add(toolbar, grid);
    }

    private void configureGrid() {
        grid.addClassNames("categories-grid");
        grid.setSizeFull();

        grid.addColumn(CategoryDto::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(category -> category.getCreatedAt() != null ?
                        category.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : "")
                .setHeader("Erstellt am").setAutoWidth(true);

        grid.addComponentColumn(category -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> openCategoryForm(category));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(e -> deleteCategory(category));

            actions.add(editButton, deleteButton);
            return actions;
        }).setHeader("Aktionen").setAutoWidth(true);

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        // In einer echten Anwendung würden hier Daten vom Service geladen
        // grid.setItems(categoryService.getAllCategories());
    }

    private void openCategoryForm(CategoryDto category) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(category.getId() == null ? "Neue Kategorie erstellen" : "Kategorie bearbeiten");

        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Name");
        nameField.setRequired(true);
        nameField.setValue(category.getName() != null ? category.getName() : "");

        formLayout.add(nameField);

        Button saveButton = new Button("Speichern");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            // In einer echten Anwendung würden hier die Daten gespeichert
            // category.setName(nameField.getValue());
            // categoryService.saveCategory(category);

            Notification.show("Kategorie gespeichert", 3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            dialog.close();
        });

        Button cancelButton = new Button("Abbrechen", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.getFooter().add(cancelButton, saveButton);
        dialog.add(formLayout);

        dialog.open();
    }

    private void deleteCategory(CategoryDto category) {
        // In einer echten Anwendung würde hier die Kategorie gelöscht
        // categoryService.deleteCategory(category.getId());

        Notification.show("Kategorie gelöscht", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}