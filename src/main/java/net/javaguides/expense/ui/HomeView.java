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
 * Hauptansicht für die Kategorieverwaltung des Expense Tracker.
 *
 * Diese Klasse implementiert eine moderne und benutzerfreundliche Oberfläche
 * zur Verwaltung von Ausgabenkategorien. Sie bietet folgende Funktionalitäten:
 *
 * 
 *   Anzeige aller vorhandenen Kategorien in einem Grid
 *   Erstellen neuer Kategorien über einen Dialog
 *   Bearbeiten bestehender Kategorien
 *   Löschen von Kategorien mit Bestätigungsdialog
 *   Statistik-Dashboard mit Anzahl aktiver Kategorien
 * 
 *
 * Die Benutzeroberfläche folgt modernen Design-Prinzipien mit:
 * 
 *   Card-basiertem Layout für bessere visuelle Trennung
 *   Icons für intuitive Bedienung
 *   Responsive Design für verschiedene Bildschirmgrößen
 *   Konsistente Farbgebung nach Lumo-Theme
 * 
 *
 * @author mboudn
 * @version 2.0
 * @since 2.0
 */
@Route("") // Erreichbar unter http://localhost:9090/
@PageTitle("Expense Tracker - Kategorien")
public class HomeView extends VerticalLayout {

    /** Service für alle kategoriebezogenen Geschäftsoperationen */
    private final CategoryService categoryService;

    /** Grid-Komponente zur tabellarischen Darstellung der Kategorien */
    private final Grid<CategoryDto> grid = new Grid<>(CategoryDto.class, false);

    /** Referenz auf das Statistik-Element für dynamische Updates */
    private Span statsValue;

    /**
     * Konstruktor zur Initialisierung der HomeView.
     *
     * Konfiguriert das grundlegende Layout und initialisiert alle
     * UI-Komponenten. Das Layout wird vollflächig dargestellt und
     * das Kategorien-Grid wird mit aktuellen Daten gefüllt.
     *
     * @param categoryService Service für Kategorie-Operationen (wird von Spring injiziert)
     */
    @Autowired
    public HomeView(CategoryService categoryService) {
        this.categoryService = categoryService;

        // Layout-Konfiguration für Vollbild-Darstellung
        configurePrimaryLayout();

        // Aufbau der gesamten Benutzeroberfläche
        createLayout();

        // Initiales Laden der Kategorie-Daten
        refreshGrid();
    }

    /**
     * Konfiguriert die Grundeinstellungen des Haupt-Layouts.
     *
     * Setzt die View auf Vollbild-Modus und entfernt Standard-Abstände
     * für ein nahtloses Design.
     */
    private void configurePrimaryLayout() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("main-layout");
    }

    /**
     * Erstellt die komplette Layout-Struktur der Anwendung.
     *
     * Organisiert die UI in folgende Bereiche:
     * 
     *   Header mit App-Titel und Branding
     *   Statistik-Card mit Kategorie-Anzahl
     *   Aktions-Leiste mit Buttons
     *   Kategorie-Grid in Card-Layout
     * 
     */
    private void createLayout() {
        // Header-Bereich hinzufügen
        add(createHeader());

        // Haupt-Container für Content-Bereiche
        VerticalLayout mainContent = createMainContentContainer();

        // Alle Content-Bereiche zum Container hinzufügen
        mainContent.add(
                createStatsCard(),
                createActionsBar(),
                createGridCard()
        );

        add(mainContent);
    }

    /**
     * Erstellt den Haupt-Content-Container mit entsprechenden Styling.
     *
     * @return VerticalLayout als Container für alle Content-Bereiche
     */
    private VerticalLayout createMainContentContainer() {
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.addClassNames("main-content");
        return mainContent;
    }

    /**
     * Erstellt den Header-Bereich der Anwendung.
     *
     * Der Header enthält:
     * 
     *   App-Icon (Geld-Symbol)
     *   Haupttitel "Expense Tracker"
     *   Untertitel "Kategorien intelligent verwalten"
     * 
     *
     * Das Design nutzt die primäre Theme-Farbe als Hintergrund
     * für einen professionellen Look.
     *
     * @return Div-Element mit komplettem Header-Layout
     */
    private Div createHeader() {
        Div header = new Div();

        // Styling für primären Header-Look
        header.addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.TextColor.PRIMARY_CONTRAST,
                LumoUtility.Padding.LARGE
        );

        // Hauptlayout für Header-Inhalt
        HorizontalLayout headerContent = createHeaderContent();
        header.add(headerContent);

        return header;
    }

    /**
     * Erstellt den Inhalt des Headers mit Titel und Icon.
     *
     * @return HorizontalLayout mit komplettem Header-Inhalt
     */
    private HorizontalLayout createHeaderContent() {
        HorizontalLayout headerContent = new HorizontalLayout();
        headerContent.setWidthFull();
        headerContent.setAlignItems(FlexComponent.Alignment.CENTER);
        headerContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // Titel-Sektion mit Icon und Text
        HorizontalLayout titleSection = createTitleSection();

        // Untertitel
        Span subtitle = createSubtitle();

        // Vertikales Layout für Titel und Untertitel
        VerticalLayout headerText = new VerticalLayout(titleSection, subtitle);
        headerText.setPadding(false);
        headerText.setSpacing(false);

        headerContent.add(headerText);
        return headerContent;
    }

    /**
     * Erstellt die Titel-Sektion mit App-Icon und Haupttitel.
     *
     * @return HorizontalLayout mit Icon und Titel
     */
    private HorizontalLayout createTitleSection() {
        HorizontalLayout titleSection = new HorizontalLayout();
        titleSection.setAlignItems(FlexComponent.Alignment.CENTER);
        titleSection.setSpacing(true);

        // App-Icon (Geld-Symbol)
        Icon appIcon = new Icon(VaadinIcon.MONEY);
        appIcon.setSize("32px");
        appIcon.addClassNames(LumoUtility.TextColor.PRIMARY_CONTRAST);

        // Haupttitel
        H1 title = new H1("Expense Tracker");
        title.addClassNames(
                LumoUtility.FontSize.XXLARGE,
                LumoUtility.FontWeight.BOLD,
                LumoUtility.Margin.NONE
        );

        titleSection.add(appIcon, title);
        return titleSection;
    }

    /**
     * Erstellt den Untertitel für den Header.
     *
     * @return Span-Element mit Untertitel
     */
    private Span createSubtitle() {
        Span subtitle = new Span("Kategorien intelligent verwalten");
        subtitle.addClassNames(
                LumoUtility.FontSize.MEDIUM,
                LumoUtility.TextColor.PRIMARY_CONTRAST
        );
        return subtitle;
    }

    /**
     * Erstellt die Statistik-Karte mit Kategorie-Anzahl.
     *
     * Diese Karte zeigt die aktuelle Anzahl der Kategorien in der
     * Datenbank an. Der Wert wird automatisch aktualisiert, wenn
     * Kategorien hinzugefügt oder entfernt werden.
     *
     * Design-Features:
     * 
     *   Card-Layout mit Schatten für visuelle Trennung
     *   Chart-Icon zur Verdeutlichung der Statistik
     *   Große, hervorgehobene Zahl für den aktuellen Wert
     * 
     *
     * @return Div-Element mit der Statistik-Karte
     */
    private Div createStatsCard() {
        Div card = createStandardCard();

        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        statsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        // Statistik-Icon
        Icon statsIcon = new Icon(VaadinIcon.BAR_CHART);
        statsIcon.addClassNames(LumoUtility.TextColor.PRIMARY);
        statsIcon.setSize("24px");

        // Text-Bereich mit Titel und Wert
        VerticalLayout statsText = createStatsTextArea();

        statsLayout.add(statsIcon, statsText);
        card.add(statsLayout);

        return card;
    }

    /**
     * Erstellt eine Standard-Karte mit einheitlichem Styling.
     *
     * @return Div-Element mit Standard-Card-Styling
     */
    private Div createStandardCard() {
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Padding.MEDIUM
        );
        return card;
    }

    /**
     * Erstellt den Text-Bereich für die Statistik-Karte.
     *
     * @return VerticalLayout mit Titel und aktuellem Wert
     */
    private VerticalLayout createStatsTextArea() {
        int categoryCount = categoryService.getAllCategories().size();

        H3 statsTitle = new H3("Aktive Kategorien");
        statsTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.NONE);

        // Referenz für spätere Updates speichern
        statsValue = new Span(String.valueOf(categoryCount));
        statsValue.addClassNames(
                LumoUtility.FontSize.XXLARGE,
                LumoUtility.FontWeight.BOLD,
                LumoUtility.TextColor.PRIMARY
        );

        VerticalLayout statsText = new VerticalLayout(statsTitle, statsValue);
        statsText.setPadding(false);
        statsText.setSpacing(false);

        return statsText;
    }

    /**
     * Erstellt die Aktionsleiste mit Haupt- und Utility-Buttons.
     *
     * Die Aktionsleiste enthält:
     * 
     *   Links: "Neue Kategorie" Button (primärer Call-to-Action)
     *   Rechts: Utility-Buttons (Aktualisieren, etc.)
     * 
     *
     * Das Layout ist responsiv und passt sich an verschiedene
     * Bildschirmgrößen an.
     *
     * @return HorizontalLayout mit allen Aktions-Buttons
     */
    private HorizontalLayout createActionsBar() {
        HorizontalLayout actionsBar = new HorizontalLayout();
        actionsBar.setWidthFull();
        actionsBar.setAlignItems(FlexComponent.Alignment.CENTER);
        actionsBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // Hauptaktion: Neue Kategorie erstellen
        Button addButton = createAddButton();

        // Utility-Buttons (Aktualisieren, etc.)
        HorizontalLayout utilityButtons = createUtilityButtons();

        actionsBar.add(addButton, utilityButtons);
        return actionsBar;
    }

    /**
     * Erstellt den Hauptbutton zum Hinzufügen neuer Kategorien.
     *
     * @return Button für "Neue Kategorie" Aktion
     */
    private Button createAddButton() {
        Button addButton = new Button("Neue Kategorie", new Icon(VaadinIcon.PLUS));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        addButton.addClickListener(e -> openAddCategoryDialog());
        return addButton;
    }

    /**
     * Erstellt die Container für Utility-Buttons.
     *
     * @return HorizontalLayout mit Utility-Buttons
     */
    private HorizontalLayout createUtilityButtons() {
        HorizontalLayout utilityButtons = new HorizontalLayout();
        utilityButtons.setSpacing(true);

        // Aktualisieren-Button
        Button refreshButton = createRefreshButton();
        utilityButtons.add(refreshButton);

        return utilityButtons;
    }

    /**
     * Erstellt den Aktualisieren-Button.
     *
     * @return Button zum manuellen Aktualisieren der Daten
     */
    private Button createRefreshButton() {
        Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
        refreshButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshButton.getElement().setAttribute("title", "Aktualisieren");
        refreshButton.addClickListener(e -> handleRefreshAction());
        return refreshButton;
    }

    /**
     * Behandelt die Aktualisieren-Aktion.
     *
     * Lädt die Grid-Daten neu und zeigt eine Bestätigungsmeldung an.
     */
    private void handleRefreshAction() {
        refreshGrid();
        showNotification("Daten wurden aktualisiert", NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Erstellt die Haupt-Karte für das Kategorien-Grid.
     *
     * Diese Karte enthält:
     *
     *   Header mit Titel "Kategorien Übersicht"
     *   Das konfigurierte Grid mit allen Kategorien
     *
     * Die Karte nimmt den verfügbaren Platz vollständig ein
     * und passt sich dynamisch an den Inhalt an.
     *
     * @return Div-Element mit der Grid-Karte
     */
    private Div createGridCard() {
        Div card = createStandardCard();
        card.setSizeFull();

        // Card-Header mit Titel
        HorizontalLayout cardHeader = createGridCardHeader();

        // Grid-Konfiguration
        configureGrid();

        // Grid-Container ohne zusätzliche Abstände
        VerticalLayout gridContainer = new VerticalLayout(grid);
        gridContainer.setSizeFull();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);

        card.add(cardHeader, gridContainer);
        return card;
    }

    /**
     * Erstellt den Header für die Grid-Karte.
     *
     * @return HorizontalLayout mit Card-Header
     */
    private HorizontalLayout createGridCardHeader() {
        HorizontalLayout cardHeader = new HorizontalLayout();
        cardHeader.setWidthFull();
        cardHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        cardHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        H3 cardTitle = new H3("Kategorien Übersicht");
        cardTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        cardHeader.add(cardTitle);
        return cardHeader;
    }

    /**
     * Konfiguriert das Kategorien-Grid mit allen Spalten und Styling.
     *
     * Das Grid enthält folgende Spalten:
     * 
     *   ID: Eindeutige Kategorie-ID mit Hash-Icon
     *   Kategoriename: Name mit Tag-Icon
     *   Aktionen: Bearbeiten und Löschen Buttons
     * 
     *
     * Features:
     * 
     *   Zebrastreifen für bessere Lesbarkeit
     *   Kompakte Darstellung für mehr Inhalte
     *   Sortierbare Spalten
     *   Responsive Spaltenbreiten
     * 
     */
    private void configureGrid() {
        // Grundkonfiguration
        grid.setSizeFull();
        grid.addThemeVariants(
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COMPACT,
                GridVariant.LUMO_WRAP_CELL_CONTENT
        );

        // Spalten hinzufügen
        addIdColumn();
        addNameColumn();
        addActionsColumn();
    }

    /**
     * Fügt die ID-Spalte zum Grid hinzu.
     *
     * Die ID wird mit einem Hash-Icon und Monospace-Font dargestellt
     * für bessere technische Lesbarkeit.
     */
    private void addIdColumn() {
        grid.addColumn(new ComponentRenderer<>(this::createIdLayout))
                .setHeader("ID")
                .setWidth("100px")
                .setFlexGrow(0)
                .setSortable(true);
    }

    /**
     * Erstellt das Layout für die ID-Anzeige.
     *
     * @param category Die Kategorie, deren ID angezeigt werden soll
     * @return HorizontalLayout mit Icon und ID-Text
     */
    private HorizontalLayout createIdLayout(CategoryDto category) {
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
    }

    /**
     * Fügt die Kategoriename-Spalte zum Grid hinzu.
     *
     * Der Name wird mit einem Tag-Icon dargestellt und nimmt
     * den verfügbaren Platz flexibel ein.
     */
    private void addNameColumn() {
        grid.addColumn(new ComponentRenderer<>(this::createNameLayout))
                .setHeader("Kategoriename")
                .setSortable(true)
                .setFlexGrow(1);
    }

    /**
     * Erstellt das Layout für die Kategorienamen-Anzeige.
     *
     * @param category Die Kategorie, deren Name angezeigt werden soll
     * @return HorizontalLayout mit Icon und Name-Text
     */
    private HorizontalLayout createNameLayout(CategoryDto category) {
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
    }

    /**
     * Fügt die Aktions-Spalte zum Grid hinzu.
     *
     * Enthält Bearbeiten- und Löschen-Buttons für jede Zeile.
     */
    private void addActionsColumn() {
        grid.addColumn(new ComponentRenderer<>(this::createActionButtons))
                .setHeader("Aktionen")
                .setWidth("150px")
                .setFlexGrow(0);
    }

    /**
     * Erstellt die Aktions-Buttons für eine Kategorie-Zeile.
     *
     * Jede Zeile erhält folgende Buttons:
     * 
     *   Bearbeiten: Öffnet den Edit-Dialog
     *   Löschen: Öffnet Bestätigungsdialog
     * 
     *
     * Die Buttons sind mit Tooltips versehen und folgen
     * dem Theme-Design für konsistentes Aussehen.
     *
     * @param category Die Kategorie für diese Zeile
     * @return HorizontalLayout mit allen Aktions-Buttons
     */
    private HorizontalLayout createActionButtons(CategoryDto category) {
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        // Bearbeiten-Button
        Button editButton = createEditButton(category);

        // Löschen-Button
        Button deleteButton = createDeleteButton(category);

        actions.add(editButton, deleteButton);
        return actions;
    }

    /**
     * Erstellt den Bearbeiten-Button für eine Kategorie.
     *
     * @param category Die zu bearbeitende Kategorie
     * @return Button zum Bearbeiten der Kategorie
     */
    private Button createEditButton(CategoryDto category) {
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        editButton.getElement().setAttribute("title", "Bearbeiten");
        editButton.addClickListener(e -> openEditDialog(category));
        return editButton;
    }

    /**
     * Erstellt den Löschen-Button für eine Kategorie.
     *
     * @param category Die zu löschende Kategorie
     * @return Button zum Löschen der Kategorie
     */
    private Button createDeleteButton(CategoryDto category) {
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(
                ButtonVariant.LUMO_TERTIARY,
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_SMALL
        );
        deleteButton.getElement().setAttribute("title", "Löschen");
        deleteButton.addClickListener(e -> confirmDelete(category));
        return deleteButton;
    }

    /**
     * Öffnet den Dialog zum Erstellen einer neuen Kategorie.
     *
     * Der Dialog bietet:
     * 
     *   Eingabefeld für Kategoriename mit Placeholder-Text
     *   Tag-Icon als visueller Hinweis
     *   Speichern-Button mit Validierung
     *   Abbrechen-Button
     *   Enter-Taste zum Speichern
     * 
     *
     * Validierung:
     * 
     *   Name darf nicht leer sein
     *   Whitespace wird automatisch entfernt
     *   Fehlermeldungen bei Problemen
     * 
     */
    private void openAddCategoryDialog() {
        Dialog dialog = createStandardDialog("Neue Kategorie erstellen");

        // Eingabefeld für Kategoriename
        TextField nameField = createCategoryNameField();
        nameField.setPlaceholder("z.B. Lebensmittel, Transport, Freizeit...");
        nameField.focus();

        // Dialog-Buttons
        Button saveButton = createSaveButton();
        Button cancelButton = createCancelButton(dialog);

        // Event-Handler für Speichern
        configureSaveAction(saveButton, nameField, dialog, null);

        // Dialog zusammenbauen
        VerticalLayout content = new VerticalLayout(nameField);
        content.setPadding(false);

        dialog.add(content);
        dialog.getFooter().add(cancelButton, saveButton);

        // Enter-Taste zum Speichern
        nameField.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> saveButton.click());
        dialog.open();
    }

    /**
     * Erstellt einen Standard-Dialog mit einheitlicher Konfiguration.
     *
     * @param title Der Titel des Dialogs
     * @return Konfigurierter Dialog
     */
    private Dialog createStandardDialog(String title) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setWidth("400px");
        return dialog;
    }

    /**
     * Erstellt ein Eingabefeld für Kategorienamen.
     *
     * @return TextField mit Standard-Konfiguration
     */
    private TextField createCategoryNameField() {
        TextField nameField = new TextField("Kategoriename");
        nameField.setWidthFull();
        nameField.setPrefixComponent(new Icon(VaadinIcon.TAG));
        return nameField;
    }

    /**
     * Erstellt einen Speichern-Button.
     *
     * @return Button zum Speichern mit entsprechendem Styling
     */
    private Button createSaveButton() {
        Button saveButton = new Button("Speichern", new Icon(VaadinIcon.CHECK));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    /**
     * Erstellt einen Abbrechen-Button.
     *
     * @param dialog Der Dialog, der geschlossen werden soll
     * @return Button zum Abbrechen
     */
    private Button createCancelButton(Dialog dialog) {
        return new Button("Abbrechen", e -> dialog.close());
    }

    /**
     * Konfiguriert die Speicher-Aktion für Dialog-Buttons.
     *
     * @param saveButton Der Speichern-Button
     * @param nameField Das Eingabefeld für den Namen
     * @param dialog Der Dialog, der geschlossen werden soll
     * @param existingCategory Vorhandene Kategorie (null beim Erstellen)
     */
    private void configureSaveAction(Button saveButton, TextField nameField, Dialog dialog, CategoryDto existingCategory) {
        saveButton.addClickListener(e -> {
            String name = nameField.getValue().trim();

            if (isValidCategoryName(name)) {
                if (existingCategory == null) {
                    handleCreateCategory(name, dialog);
                } else {
                    handleUpdateCategory(existingCategory, name, dialog);
                }
            } else {
                handleInvalidCategoryName(nameField);
            }
        });
    }

    /**
     * Validiert den Kategorienamen.
     *
     * @param name Der zu validierende Name
     * @return true, wenn der Name gültig ist
     */
    private boolean isValidCategoryName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Behandelt das Erstellen einer neuen Kategorie.
     *
     * @param name Der Name der neuen Kategorie
     * @param dialog Der Dialog, der geschlossen werden soll
     */
    private void handleCreateCategory(String name, Dialog dialog) {
        try {
            categoryService.createCategory(new CategoryDto(null, name));
            showNotification("Kategorie '" + name + "' wurde erstellt!", NotificationVariant.LUMO_SUCCESS);
            refreshGrid();
            dialog.close();
        } catch (Exception ex) {
            showNotification("Fehler: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Behandelt das Aktualisieren einer bestehenden Kategorie.
     *
     * @param existingCategory Die zu aktualisierende Kategorie
     * @param newName Der neue Name der Kategorie
     * @param dialog Der Dialog, der geschlossen werden soll
     */
    private void handleUpdateCategory(CategoryDto existingCategory, String newName, Dialog dialog) {
        try {
            categoryService.updateCategory(existingCategory.id(), new CategoryDto(existingCategory.id(), newName));
            showNotification("Kategorie wurde aktualisiert!", NotificationVariant.LUMO_SUCCESS);
            refreshGrid();
            dialog.close();
        } catch (Exception ex) {
            showNotification("Fehler: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Behandelt ungültige Kategorienamen.
     *
     * @param nameField Das Eingabefeld, das den Fokus erhalten soll
     */
    private void handleInvalidCategoryName(TextField nameField) {
        showNotification("Bitte einen Namen eingeben", NotificationVariant.LUMO_CONTRAST);
        nameField.focus();
    }

    /**
     * Öffnet den Dialog zum Bearbeiten einer bestehenden Kategorie.
     *
     * Der Bearbeiten-Dialog ähnelt dem Erstellen-Dialog, aber:
     * 
     *   Eingabefeld ist mit aktuellem Namen vorausgefüllt
     *   Dialog-Titel zeigt "Kategorie bearbeiten"
     *   Speichern-Aktion führt Update statt Create durch
     * 
     *
     * Benutzerfreundlichkeit:
     * 
     *   Automatischer Fokus auf Eingabefeld
     *   Enter-Taste zum Speichern
     *   Konsistente Validierung wie beim Erstellen
     * 
     *
     * @param category Die zu bearbeitende Kategorie
     */
    private void openEditDialog(CategoryDto category) {
        Dialog dialog = createStandardDialog("Kategorie bearbeiten");

        // Eingabefeld mit vorhandenem Namen
        TextField nameField = createCategoryNameField();
        nameField.setValue(category.name());
        nameField.focus();

        // Dialog-Buttons
        Button saveButton = createSaveButton();
        Button cancelButton = createCancelButton(dialog);

        // Event-Handler für Update-Aktion
        configureSaveAction(saveButton, nameField, dialog, category);

        // Dialog zusammenbauen
        dialog.add(nameField);
        dialog.getFooter().add(cancelButton, saveButton);

        // Enter-Taste zum Speichern
        nameField.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> saveButton.click());
        dialog.open();
    }

    /**
     * Zeigt einen Bestätigungsdialog vor dem Löschen einer Kategorie.
     *
     * Sicherheitsfeatures:
     * 
     *   Explizite Bestätigung erforderlich
     *   Kategoriename wird im Dialog angezeigt
     *   Löschen-Button ist rot eingefärbt (Danger-Theme)
     *   Dialog kann abgebrochen werden
     * 
     *
     * Nach erfolgreichem Löschen:
     * 
     *   Grid wird automatisch aktualisiert
     *   Bestätigungsmeldung wird angezeigt
     *   Bei Fehlern wird Fehlermeldung angezeigt
     * 
     *
     * @param category Die zu löschende Kategorie
     */
    private void confirmDelete(CategoryDto category) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Kategorie löschen");
        dialog.setText("Möchten Sie die Kategorie '" + category.name() + "' wirklich löschen?");

        // Dialog-Konfiguration
        dialog.setCancelable(true);
        dialog.setCancelText("Abbrechen");

        // Bestätigung-Button (Danger-Styling)
        dialog.setConfirmText("Löschen");
        dialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR + " " + ButtonVariant.LUMO_PRIMARY);

        // Bestätigungs-Handler
        dialog.addConfirmListener(e -> handleCategoryDeletion(category));

        dialog.open();
    }

    /**
     * Behandelt das tatsächliche Löschen einer Kategorie.
     *
     * Diese Methode wird nur aufgerufen, wenn der Benutzer
     * das Löschen explizit bestätigt hat.
     *
     * @param category Die zu löschende Kategorie
     */
    private void handleCategoryDeletion(CategoryDto category) {
        try {
            categoryService.deleteCategory(category.id());
            showNotification("Kategorie '" + category.name() + "' wurde gelöscht", NotificationVariant.LUMO_SUCCESS);
            refreshGrid();
        } catch (Exception ex) {
            showNotification("Fehler beim Löschen: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Aktualisiert das Grid und die Statistiken mit aktuellen Daten.
     *
     * Diese Methode wird aufgerufen nach:
     * 
     *   Erstellen einer neuen Kategorie
     *   Bearbeiten einer bestehenden Kategorie
     *   Löschen einer Kategorie
     *   Manueller Aktualisierung durch Benutzer
     * 
     *
     * Aktualisierte Elemente:
     * 
     *   Grid-Inhalt mit allen Kategorien
     *   Statistik-Wert (Anzahl Kategorien)
     * 
     *
     * Fehlerbehandlung:
     * 
     *   Bei Fehlern wird eine Benachrichtigung angezeigt
     *   Grid bleibt in letztem gültigen Zustand
     * 
     */
    private void refreshGrid() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategories();
            grid.setItems(categories);

            // Statistik-Wert aktualisieren (null-safe)
            updateStatsDisplay(categories.size());

        } catch (Exception ex) {
            showNotification("Fehler beim Laden: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Aktualisiert die Anzeige des Statistik-Werts.
     *
     * Null-safe Aktualisierung der Kategorie-Anzahl
     * in der Statistik-Karte.
     *
     * @param categoryCount Die aktuelle Anzahl der Kategorien
     */
    private void updateStatsDisplay(int categoryCount) {
        if (statsValue != null) {
            statsValue.setText(String.valueOf(categoryCount));
        }
    }

    /**
     * Zeigt eine Benachrichtigung mit einheitlichem Styling an.
     *
     * Konfiguration:
     * 
     *   Anzeigedauer: 3 Sekunden
     *   Position: Oben mittig
     *   Theme-Variante bestimmt Farbe und Icon
     * 
     *
     * Verfügbare Varianten:
     * 
     *   SUCCESS: Grün für erfolgreiche Aktionen
     *   ERROR: Rot für Fehlermeldungen
     *   CONTRAST: Neutral für Informationen
     * 
     *
     * @param message Die anzuzeigende Nachricht
     * @param variant Die Theme-Variante (bestimmt Aussehen)
     */
    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(variant);
    }
}