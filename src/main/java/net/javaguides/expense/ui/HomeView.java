package net.javaguides.expense.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Moderne und ansprechende Benutzeroberfläche für die Kategorieverwaltung
 * mit Card-Layout, Icons und verbesserter User Experience
 */
@Route("") // Erreichbar unter http://localhost:9090/
@PageTitle("Expense Tracker - Kategorien")
public class HomeView extends VerticalLayout {

    private final CategoryService categoryService;
    private final Grid<CategoryDto> grid = new Grid<>(CategoryDto.class, false);
    private Span statsValue; // Reference für dynamische Updates

    @Autowired
    public HomeView(CategoryService categoryService) {
        this.categoryService = categoryService;

        // Layout-Konfiguration
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("main-layout");

        createLayout();
        refreshGrid();
    }

    private void createLayout() {
        add(createHeader());

        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.addClassNames("main-content");

        mainContent.add(createStatsCard());
        mainContent.add(createActionsBar());
        mainContent.add(createGridCard());

        add(mainContent);
    }

    private Div createHeader() {
        Div header = new Div();
        header.addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.Padding.LARGE
        );

        HorizontalLayout headerContent = new HorizontalLayout();
        headerContent.setWidthFull();
        headerContent.setAlignItems(FlexComponent.Alignment.CENTER);
        headerContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout titleSection = new HorizontalLayout();
        titleSection.setAlignItems(FlexComponent.Alignment.CENTER);
        titleSection.setSpacing(true);

        Icon appIcon = new Icon(VaadinIcon.MONEY);
        appIcon.setSize("32px");
        appIcon.addClassNames(LumoUtility.TextColor.PRIMARY_CONTRAST);

        H1 title = new H1("Expense Tracker");
        title.addClassNames(
                LumoUtility.FontSize.XXLARGE,
                LumoUtility.FontWeight.BOLD,
                LumoUtility.Margin.NONE
        );

        titleSection.add(appIcon, title);

        Span subtitle = new Span("Kategorien intelligent verwalten");
        subtitle.addClassNames(
                LumoUtility.FontSize.MEDIUM,
                LumoUtility.TextColor.PRIMARY_CONTRAST
        );

        VerticalLayout headerText = new VerticalLayout(titleSection, subtitle);
        headerText.setPadding(false);
        headerText.setSpacing(false);

        headerContent.add(headerText);
        header.add(headerContent);

        return header;
    }

    private Div createStatsCard() {
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Padding.MEDIUM
        );

        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        statsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        Icon statsIcon = new Icon(VaadinIcon.BAR_CHART);
        statsIcon.addClassNames(LumoUtility.TextColor.PRIMARY);
        statsIcon.setSize("24px");

        int categoryCount = categoryService.getAllCategories().size();
        H3 statsTitle = new H3("Aktive Kategorien");
        statsTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.NONE);

        statsValue = new Span(String.valueOf(categoryCount));
        statsValue.addClassNames(
                LumoUtility.FontSize.XXLARGE,
                LumoUtility.FontWeight.BOLD,
                LumoUtility.TextColor.PRIMARY
        );

        VerticalLayout statsText = new VerticalLayout(statsTitle, statsValue);
        statsText.setPadding(false);
        statsText.setSpacing(false);

        statsLayout.add(statsIcon, statsText);
        card.add(statsLayout);

        return card;
    }

    private HorizontalLayout createActionsBar() {
        HorizontalLayout actionsBar = new HorizontalLayout();
        actionsBar.setWidthFull();
        actionsBar.setAlignItems(FlexComponent.Alignment.CENTER);
        actionsBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        Button addButton = new Button("Neue Kategorie", new Icon(VaadinIcon.PLUS));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        addButton.addClickListener(e -> openAddCategoryDialog());

        HorizontalLayout utilityButtons = new HorizontalLayout();
        utilityButtons.setSpacing(true);

        Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
        refreshButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshButton.getElement().setAttribute("title", "Aktualisieren");
        refreshButton.addClickListener(e -> {
            refreshGrid();
            showNotification("Daten wurden aktualisiert", NotificationVariant.LUMO_SUCCESS);
        });

        utilityButtons.add(refreshButton);

        actionsBar.add(addButton, utilityButtons);
        return actionsBar;
    }

    private Div createGridCard() {
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Padding.MEDIUM
        );
        card.setSizeFull();

        HorizontalLayout cardHeader = new HorizontalLayout();
        cardHeader.setWidthFull();
        cardHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        cardHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        H3 cardTitle = new H3("Kategorien Übersicht");
        cardTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        cardHeader.add(cardTitle);

        configureGrid();

        VerticalLayout gridContainer = new VerticalLayout(grid);
        gridContainer.setSizeFull();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);

        card.add(cardHeader, gridContainer);
        return card;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addThemeVariants(
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COMPACT,
                GridVariant.LUMO_WRAP_CELL_CONTENT
        );

        grid.addColumn(new ComponentRenderer<>(category -> {
                    HorizontalLayout idLayout = new HorizontalLayout();
                    idLayout.setAlignItems(FlexComponent.Alignment.CENTER);
                    idLayout.setSpacing(true);

                    Icon idIcon = new Icon(VaadinIcon.HASH);
                    idIcon.setSize("16px");
                    idIcon.addClassNames(LumoUtility.TextColor.SECONDARY);

                    Span idText = new Span(category.id().toString());
                    idText.getStyle().set("font-family", "monospace");

                    idLayout.add(idIcon, idText);
                    return idLayout;
                }))
                .setHeader("ID")
                .setWidth("100px")
                .setFlexGrow(0)
                .setSortable(true);

        grid.addColumn(new ComponentRenderer<>(category -> {
                    HorizontalLayout nameLayout = new HorizontalLayout();
                    nameLayout.setAlignItems(FlexComponent.Alignment.CENTER);
                    nameLayout.setSpacing(true);

                    Icon categoryIcon = new Icon(VaadinIcon.TAG);
                    categoryIcon.setSize("18px");
                    categoryIcon.addClassNames(LumoUtility.TextColor.PRIMARY);

                    Span nameText = new Span(category.name());
                    nameText.addClassNames(LumoUtility.FontWeight.MEDIUM);

                    nameLayout.add(categoryIcon, nameText);
                    return nameLayout;
                }))
                .setHeader("Kategoriename")
                .setSortable(true)
                .setFlexGrow(1);

        grid.addColumn(new ComponentRenderer<>(this::createActionButtons))
                .setHeader("Aktionen")
                .setWidth("150px")
                .setFlexGrow(0);
    }

    private HorizontalLayout createActionButtons(CategoryDto category) {
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        editButton.getElement().setAttribute("title", "Bearbeiten");
        editButton.addClickListener(e -> openEditDialog(category));

        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(
                ButtonVariant.LUMO_TERTIARY,
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_SMALL
        );
        deleteButton.getElement().setAttribute("title", "Löschen");
        deleteButton.addClickListener(e -> confirmDelete(category));

        actions.add(editButton, deleteButton);
        return actions;
    }

    private void openAddCategoryDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Neue Kategorie erstellen");
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setWidth("400px");

        TextField nameField = new TextField("Kategoriename");
        nameField.setPlaceholder("z.B. Lebensmittel, Transport, Freizeit...");
        nameField.setWidthFull();
        nameField.focus();
        nameField.setPrefixComponent(new Icon(VaadinIcon.TAG));

        Button saveButton = new Button("Speichern", new Icon(VaadinIcon.CHECK));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            String name = nameField.getValue().trim();
            if (!name.isEmpty()) {
                try {
                    categoryService.createCategory(new CategoryDto(null, name));
                    showNotification("Kategorie '" + name + "' wurde erstellt!", NotificationVariant.LUMO_SUCCESS);
                    refreshGrid();
                    dialog.close();
                } catch (Exception ex) {
                    showNotification("Fehler: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            } else {
                showNotification("Bitte einen Namen eingeben", NotificationVariant.LUMO_CONTRAST);
                nameField.focus();
            }
        });

        Button cancelButton = new Button("Abbrechen", e -> dialog.close());

        VerticalLayout content = new VerticalLayout(nameField);
        content.setPadding(false);

        dialog.add(content);
        dialog.getFooter().add(cancelButton, saveButton);

        nameField.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> saveButton.click());
        dialog.open();
    }

    private void openEditDialog(CategoryDto category) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Kategorie bearbeiten");
        dialog.setModal(true);
        dialog.setWidth("400px");

        TextField nameField = new TextField("Kategoriename");
        nameField.setValue(category.name());
        nameField.setWidthFull();
        nameField.focus();
        // Automatische Textauswahl ist in dieser Vaadin-Version nicht verfügbar
        nameField.setPrefixComponent(new Icon(VaadinIcon.TAG));

        Button saveButton = new Button("Speichern", new Icon(VaadinIcon.CHECK));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            String newName = nameField.getValue().trim();
            if (!newName.isEmpty()) {
                try {
                    categoryService.updateCategory(category.id(), new CategoryDto(category.id(), newName));
                    showNotification("Kategorie wurde aktualisiert!", NotificationVariant.LUMO_SUCCESS);
                    refreshGrid();
                    dialog.close();
                } catch (Exception ex) {
                    showNotification("Fehler: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            }
        });

        Button cancelButton = new Button("Abbrechen", e -> dialog.close());

        dialog.add(nameField);
        dialog.getFooter().add(cancelButton, saveButton);

        nameField.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> saveButton.click());
        dialog.open();
    }

    private void confirmDelete(CategoryDto category) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Kategorie löschen");
        dialog.setText("Möchten Sie die Kategorie '" + category.name() + "' wirklich löschen?");

        dialog.setCancelable(true);
        dialog.setCancelText("Abbrechen");

        dialog.setConfirmText("Löschen");
        dialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR + " " + ButtonVariant.LUMO_PRIMARY);

        dialog.addConfirmListener(e -> {
            try {
                categoryService.deleteCategory(category.id());
                showNotification("Kategorie '" + category.name() + "' wurde gelöscht", NotificationVariant.LUMO_SUCCESS);
                refreshGrid();
            } catch (Exception ex) {
                showNotification("Fehler beim Löschen: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });

        dialog.open();
    }

    private void refreshGrid() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategories();
            grid.setItems(categories);

            if (statsValue != null) {
                statsValue.setText(String.valueOf(categories.size()));
            }
        } catch (Exception ex) {
            showNotification("Fehler beim Laden: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(variant);
    }
}
