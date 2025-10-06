package net.javaguides.expense.ui.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.javaguides.expense.dto.ExpenseDto;
import net.javaguides.expense.service.ExpenseService;
import net.javaguides.expense.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Dashboard | Expense Tracker")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    private final ExpenseService expenseService;

    public HomeView(ExpenseService expenseService) {
        this.expenseService = expenseService;
        setSizeFull();
        addClassName("home-view");

        add(createHeader());
        add(createSummaryCards());
        add(createRecentExpensesGrid());
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxShadow.SMALL);

        H2 title = new H2("Expense Tracker Dashboard");
        title.addClassNames(LumoUtility.FontSize.XXLARGE, LumoUtility.Margin.NONE);

        Span date = new Span(LocalDate.now().format(DateTimeFormatter.ofPattern("dd. MMMM yyyy")));
        date.addClassNames(LumoUtility.TextColor.SECONDARY);

        header.add(title, date);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return header;
    }

    private HorizontalLayout createSummaryCards() {
        // Berechne Werte dynamisch aus Service
        double totalExpenses = 0;
        double monthlyExpenses = 0;
        int categoriesCount = 0; // Angenommen, von CategoryService

        try {
            List<ExpenseDto> expenses = expenseService.getAllExpenses();
            totalExpenses = expenses.stream().mapToDouble(e -> e.getAmount().doubleValue()).sum();
            monthlyExpenses = expenses.stream()
                    .filter(e -> e.getExpenseDate().getMonth() == LocalDate.now().getMonth())
                    .mapToDouble(e -> e.getAmount().doubleValue()).sum();
            // categoriesCount = categoryService.getAllCategories().size();
        } catch (Exception e) {
            Notification.show("Fehler beim Laden der Statistiken: " + e.getMessage(), 5000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        HorizontalLayout cards = new HorizontalLayout(
                createCard("Gesamtausgaben", "€" + totalExpenses, VaadinIcon.EURO),
                createCard("Ausgaben diesen Monat", "€" + monthlyExpenses, VaadinIcon.CALENDAR),
                createCard("Kategorien", String.valueOf(categoriesCount), VaadinIcon.TAGS)
        );
        cards.setWidthFull();
        return cards;
    }

    private VerticalLayout createCard(String title, String value, VaadinIcon icon) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BorderRadius.MEDIUM, LumoUtility.BoxShadow.SMALL);

        Span iconSpan = new Span(icon.create());
        iconSpan.addClassNames(LumoUtility.TextColor.PRIMARY);

        H3 valueText = new H3(value);
        Span titleText = new Span(title);
        titleText.addClassNames(LumoUtility.TextColor.SECONDARY);

        card.add(iconSpan, valueText, titleText);
        return card;
    }

    private Grid<ExpenseDto> createRecentExpensesGrid() {
        Grid<ExpenseDto> grid = new Grid<>(ExpenseDto.class, false);
        grid.addColumn(ExpenseDto::getTitle).setHeader("Titel");
        grid.addColumn(e -> "€" + e.getAmount()).setHeader("Betrag");
        grid.addColumn(e -> e.getExpenseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).setHeader("Datum");

        try {
            grid.setItems(expenseService.getAllExpenses().stream().limit(5).collect(Collectors.toList()));
        } catch (Exception e) {
            Notification.show("Fehler beim Laden der Ausgaben: " + e.getMessage(), 5000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        return grid;
    }
}