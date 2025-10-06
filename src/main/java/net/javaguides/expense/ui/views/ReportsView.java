package net.javaguides.expense.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import net.javaguides.expense.dto.CategoryDto;
import net.javaguides.expense.ui.MainLayout;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Die ReportsView bietet verschiedene Berichte und Visualisierungen
 * für die Analyse der Ausgaben.
 */
@PageTitle("Berichte | Expense Tracker")
@Route(value = "reports", layout = MainLayout.class)
public class ReportsView extends VerticalLayout {

    private VerticalLayout content;
    private DatePicker startDate;
    private DatePicker endDate;
    private ComboBox<CategoryDto> categoryFilter;

    public ReportsView() {
        addClassName("reports-view");
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createFilters());

        content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(false);

        Tabs tabs = createTabs();
        add(tabs, content);

        // Standardmäßig den ersten Tab anzeigen
        showMonthlyReport();
    }

    private Component createHeader() {
        H3 title = new H3("Ausgabenberichte");
        title.addClassNames(
                LumoUtility.Margin.NONE,
                LumoUtility.Margin.Bottom.MEDIUM
        );

        return title;
    }

    private Component createFilters() {
        HorizontalLayout filters = new HorizontalLayout();
        filters.setWidthFull();
        filters.addClassNames(
                LumoUtility.Padding.SMALL,
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.XSMALL,
                LumoUtility.Background.CONTRAST_5
        );

        // Datumsbereich-Filter
        startDate = new DatePicker("Von");
        startDate.setValue(LocalDate.now().withDayOfMonth(1));

        endDate = new DatePicker("Bis");
        endDate.setValue(LocalDate.now());

        // Kategorie-Filter
        categoryFilter = new ComboBox<>("Kategorie");
        categoryFilter.setItemLabelGenerator(CategoryDto::getName);
        categoryFilter.setPlaceholder("Alle Kategorien");

        // In einer echten Anwendung würden hier Kategorien vom Service geladen
        // categoryFilter.setItems(categoryService.getAllCategories());

        filters.add(startDate, endDate, categoryFilter);
        return filters;
    }

    private Tabs createTabs() {
        Tab monthlyTab = new Tab("Monatliche Übersicht");
        Tab categoryTab = new Tab("Kategorieanalyse");
        Tab trendTab = new Tab("Ausgabentrends");

        Tabs tabs = new Tabs(monthlyTab, categoryTab, trendTab);
        tabs.addClassNames(LumoUtility.Margin.Top.MEDIUM);

        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = event.getSelectedTab();

            if (selectedTab.equals(monthlyTab)) {
                showMonthlyReport();
            } else if (selectedTab.equals(categoryTab)) {
                showCategoryReport();
            } else if (selectedTab.equals(trendTab)) {
                showTrendReport();
            }
        });

        return tabs;
    }

    private void showMonthlyReport() {
        content.removeAll();

        H4 sectionTitle = new H4("Monatliche Ausgabenübersicht");
        sectionTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);

        // Monatliche Ausgaben als visuelle Darstellung mit Standard-Komponenten
        Component monthlyChart = createMonthlyBarChart();

        // Zusammenfassung der monatlichen Ausgaben
        VerticalLayout summary = createMonthlySummary();

        content.add(sectionTitle, monthlyChart, summary);
    }

    private Component createMonthlyBarChart() {
        VerticalLayout chartLayout = new VerticalLayout();
        chartLayout.setSpacing(false);
        chartLayout.setPadding(true);
        chartLayout.addClassNames(
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Background.CONTRAST_5
        );

        H4 chartTitle = new H4("Ausgaben pro Monat");
        chartTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);

        // Beispieldaten für das Jahr 2025
        String[] months = {"Januar", "Februar", "März", "April", "Mai", "Juni",
                "Juli", "August", "September", "Oktober", "November", "Dezember"};
        double[] values = {420.30, 380.50, 450.20, 410.80, 390.40, 405.60,
                415.90, 430.20, 445.70, 460.30, 0, 0};

        // Finde den maximalen Wert für die Skalierung
        double maxValue = 0;
        for (double value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        // Erstelle die Balken für jedes Monat
        VerticalLayout barsLayout = new VerticalLayout();
        barsLayout.setSpacing(false);
        barsLayout.setPadding(false);
        barsLayout.setWidthFull();

        for (int i = 0; i < months.length; i++) {
            if (values[i] > 0) {
                HorizontalLayout monthBar = new HorizontalLayout();
                monthBar.setWidthFull();
                monthBar.setSpacing(false);
                monthBar.setPadding(false);
                monthBar.setAlignItems(Alignment.CENTER);

                // Monatsname
                Span monthName = new Span(months[i]);
                monthName.setWidth("100px");
                monthName.addClassNames(LumoUtility.FontWeight.MEDIUM);

                // Balken
                ProgressBar bar = new ProgressBar();
                bar.setValue(values[i] / maxValue);
                bar.setWidthFull();
                bar.getStyle().set("--lumo-primary-color", "#1676F3");
                bar.getStyle().set("height", "20px");

                // Wert
                Span valueLabel = new Span(String.format("%.2f €", values[i]));
                valueLabel.setWidth("80px");
                valueLabel.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.TextAlignment.RIGHT);

                monthBar.add(monthName, bar, valueLabel);
                barsLayout.add(monthBar);
            }
        }

        chartLayout.add(chartTitle, barsLayout);
        return chartLayout;
    }

    private VerticalLayout createMonthlySummary() {
        VerticalLayout summary = new VerticalLayout();
        summary.addClassNames(
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.XSMALL,
                LumoUtility.Margin.Top.MEDIUM
        );

        H4 summaryTitle = new H4("Zusammenfassung");
        summaryTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.SMALL);

        // Beispieldaten für die Zusammenfassung
        HorizontalLayout summaryItems = new HorizontalLayout();
        summaryItems.setWidthFull();
        summaryItems.add(
                createSummaryItem("Höchster Monat", "März", "€450,20"),
                createSummaryItem("Niedrigster Monat", "Februar", "€380,50"),
                createSummaryItem("Durchschnitt", "", "€420,99"),
                createSummaryItem("Gesamt", "", "€3.809,90")
        );

        summary.add(summaryTitle, summaryItems);
        return summary;
    }

    private Component createSummaryItem(String label, String sublabel, String value) {
        VerticalLayout item = new VerticalLayout();
        item.setSpacing(false);
        item.setPadding(false);

        Span labelText = new Span(label);
        labelText.addClassNames(
                LumoUtility.FontWeight.BOLD,
                LumoUtility.TextColor.SECONDARY
        );

        Span sublabelText = new Span(sublabel);
        sublabelText.addClassNames(
                LumoUtility.FontSize.SMALL,
                LumoUtility.TextColor.SECONDARY
        );

        Span valueText = new Span(value);
        valueText.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.FontWeight.BOLD
        );

        item.add(labelText);
        if (!sublabel.isEmpty()) {
            item.add(sublabelText);
        }
        item.add(valueText);

        return item;
    }

    private void showCategoryReport() {
        content.removeAll();

        H4 sectionTitle = new H4("Ausgaben nach Kategorien");
        sectionTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);

        // Kategorieverteilung als visuelle Darstellung
        Component categoryPieChart = createCategoryPieChart();

        // Kategorieverteilung als Balkendiagramm
        Component categoryBarChart = createCategoryBarChart();

        content.add(sectionTitle, categoryPieChart, categoryBarChart);
    }

    private Component createCategoryPieChart() {
        VerticalLayout chartLayout = new VerticalLayout();
        chartLayout.setSpacing(false);
        chartLayout.setPadding(true);
        chartLayout.addClassNames(
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Background.CONTRAST_5
        );

        H4 chartTitle = new H4("Ausgabenverteilung nach Kategorien");
        chartTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);

        // Beispieldaten für die Kategorieverteilung
        String[] categories = {"Lebensmittel", "Transport", "Freizeit", "Gesundheit", "Haushalt", "Sonstiges"};
        double[] values = {1234.50, 870.70, 568.48, 357.75, 609.20, 187.00};
        String[] colors = {"#1676F3", "#33A0FF", "#FF6B6B", "#4CAF50", "#FFC107", "#9C27B0"};

        // Gesamtsumme berechnen
        double total = 0;
        for (double value : values) {
            total += value;
        }

        // Kreisdiagramm als Flex-Layout
        FlexLayout pieLayout = new FlexLayout();
        pieLayout.setWidthFull();
        pieLayout.getStyle().set("flex-wrap", "wrap");
        pieLayout.getStyle().set("justify-content", "center");

        // Legende erstellen
        VerticalLayout legend = new VerticalLayout();
        legend.setSpacing(false);
        legend.setPadding(false);
        legend.setWidth("40%");

        for (int i = 0; i < categories.length; i++) {
            HorizontalLayout item = new HorizontalLayout();
            item.setSpacing(true);
            item.setAlignItems(Alignment.CENTER);

            // Farbiger Indikator
            Div colorBox = new Div();
            colorBox.setHeight("15px");
            colorBox.setWidth("15px");
            colorBox.getStyle().set("background-color", colors[i]);

            // Kategoriename und Prozentsatz
            double percentage = (values[i] / total) * 100;
            Span label = new Span(categories[i] + ": " + String.format("%.1f", percentage) + "%");

            item.add(colorBox, label);
            legend.add(item);
        }

        // Kreisdiagramm als Div mit CSS
        Div pieChart = new Div();
        pieChart.setHeight("200px");
        pieChart.setWidth("200px");
        pieChart.getStyle().set("border-radius", "50%");
        pieChart.getStyle().set("background", "conic-gradient(");

        // Farbsegmente hinzufügen
        double currentAngle = 0;
        StringBuilder gradient = new StringBuilder("conic-gradient(");
        for (int i = 0; i < categories.length; i++) {
            double percentage = (values[i] / total) * 100;
            if (i > 0) {
                gradient.append(", ");
            }
            gradient.append(colors[i]).append(" ").append(String.format("%.1f", currentAngle)).append("deg");
            currentAngle += percentage * 3.6; // 3.6 = 360 / 100
            gradient.append(" ").append(String.format("%.1f", currentAngle)).append("deg");
        }
        gradient.append(")");
        pieChart.getStyle().set("background", gradient.toString());

        // Gesamtsumme in der Mitte
        Div totalDiv = new Div();
        totalDiv.setText(String.format("%.2f €", total));
        totalDiv.addClassNames(
                LumoUtility.FontWeight.BOLD,
                LumoUtility.TextAlignment.CENTER,
                LumoUtility.Margin.Top.MEDIUM
        );

        HorizontalLayout chartContent = new HorizontalLayout(pieChart, legend);
        chartContent.setWidthFull();
        chartContent.setJustifyContentMode(JustifyContentMode.CENTER);

        chartLayout.add(chartTitle, chartContent, totalDiv);
        return chartLayout;
    }

    private Component createCategoryBarChart() {
        VerticalLayout chartLayout = new VerticalLayout();
        chartLayout.setSpacing(false);
        chartLayout.setPadding(true);
        chartLayout.addClassNames(
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.Margin.Top.MEDIUM
        );

        H4 chartTitle = new H4("Ausgaben pro Kategorie");
        chartTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);

        // Beispieldaten für die Kategorien
        String[] categories = {"Lebensmittel", "Transport", "Freizeit", "Gesundheit", "Haushalt", "Sonstiges"};
        double[] values = {1234.50, 870.70, 568.48, 357.75, 609.20, 187.00};
        String[] colors = {"#1676F3", "#33A0FF", "#FF6B6B", "#4CAF50", "#FFC107", "#9C27B0"};

        // Finde den maximalen Wert für die Skalierung
        double maxValue = 0;
        for (double value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        // Erstelle die Balken für jede Kategorie
        VerticalLayout barsLayout = new VerticalLayout();
        barsLayout.setSpacing(false);
        barsLayout.setPadding(false);
        barsLayout.setWidthFull();

        for (int i = 0; i < categories.length; i++) {
            HorizontalLayout categoryBar = new HorizontalLayout();
            categoryBar.setWidthFull();
            categoryBar.setSpacing(false);
            categoryBar.setPadding(false);
            categoryBar.setAlignItems(Alignment.CENTER);

            // Kategoriename
            Span categoryName = new Span(categories[i]);
            categoryName.setWidth("120px");
            categoryName.addClassNames(LumoUtility.FontWeight.MEDIUM);

            // Balken
            ProgressBar bar = new ProgressBar();
            bar.setValue(values[i] / maxValue);
            bar.setWidthFull();
            bar.getStyle().set("--lumo-primary-color", colors[i]);
            bar.getStyle().set("height", "20px");

            // Wert
            Span valueLabel = new Span(String.format("%.2f €", values[i]));
            valueLabel.setWidth("80px");
            valueLabel.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.TextAlignment.RIGHT);

            categoryBar.add(categoryName, bar, valueLabel);
            barsLayout.add(categoryBar);
        }

        chartLayout.add(chartTitle, barsLayout);
        return chartLayout;
    }

    private void showTrendReport() {
        content.removeAll();

        H4 sectionTitle = new H4("Ausgabentrends");
        sectionTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);

        // Ausgabentrend als visuelle Darstellung
        Component trendChart = createTrendChart();

        // Vergleich mit Vormonat
        VerticalLayout comparison = createMonthlyComparison();

        content.add(sectionTitle, trendChart, comparison);
    }

    private Component createTrendChart() {
        VerticalLayout chartLayout = new VerticalLayout();
        chartLayout.setSpacing(false);
        chartLayout.setPadding(true);
        chartLayout.addClassNames(
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Background.CONTRAST_5
        );

        H4 chartTitle = new H4("Ausgabentrend über Zeit");
        chartTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);

        // Beispieldaten für zwei Jahre zum Vergleich
        String[] months = {"Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez"};
        double[] values2024 = {380.20, 360.40, 410.10, 390.70, 370.30, 385.50, 395.80, 410.10, 425.60, 440.20, 430.10, 450.30};
        double[] values2025 = {420.30, 380.50, 450.20, 410.80, 390.40, 405.60, 415.90, 430.20, 445.70, 460.30, 0, 0};

        // Finde den maximalen Wert für die Skalierung
        double maxValue = 0;
        for (double value : values2024) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        for (double value : values2025) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        // Legende
        HorizontalLayout legend = new HorizontalLayout();
        legend.setSpacing(true);

        // Legende für 2024
        HorizontalLayout legend2024 = new HorizontalLayout();
        legend2024.setSpacing(true);
        legend2024.setAlignItems(Alignment.CENTER);

        Div color2024 = new Div();
        color2024.setHeight("15px");
        color2024.setWidth("15px");
        color2024.getStyle().set("background-color", "#1676F3");

        Span label2024 = new Span("2024");
        label2024.addClassNames(LumoUtility.FontWeight.MEDIUM);

        legend2024.add(color2024, label2024);

        // Legende für 2025
        HorizontalLayout legend2025 = new HorizontalLayout();
        legend2025.setSpacing(true);
        legend2025.setAlignItems(Alignment.CENTER);

        Div color2025 = new Div();
        color2025.setHeight("15px");
        color2025.setWidth("15px");
        color2025.getStyle().set("background-color", "#FF6B6B");

        Span label2025 = new Span("2025");
        label2025.addClassNames(LumoUtility.FontWeight.MEDIUM);

        legend2025.add(color2025, label2025);

        legend.add(legend2024, legend2025);

        // Container für den Trend-Chart
        Div chartContainer = new Div();
        chartContainer.setWidthFull();
        chartContainer.setHeight("300px");
        chartContainer.getStyle().set("position", "relative");
        chartContainer.getStyle().set("margin-top", "20px");

        // X-Achse (Monate)
        HorizontalLayout xAxis = new HorizontalLayout();
        xAxis.setWidthFull();
        xAxis.setPadding(false);
        xAxis.setSpacing(false);

        for (String month : months) {
            Span monthLabel = new Span(month);
            monthLabel.getStyle().set("flex", "1");
            monthLabel.addClassNames(
                    LumoUtility.TextAlignment.CENTER,
                    LumoUtility.FontSize.SMALL,
                    LumoUtility.TextColor.SECONDARY
            );
            xAxis.add(monthLabel);
        }

        // Liniendiagramm mit CSS
        Div lineChart = new Div();
        lineChart.setWidthFull();
        lineChart.setHeight("250px");
        lineChart.getStyle().set("position", "relative");
        lineChart.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        lineChart.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");

        // Horizontale Hilfslinien
        for (int i = 1; i <= 4; i++) {
            Div gridLine = new Div();
            gridLine.setWidthFull();
            gridLine.getStyle().set("position", "absolute");
            gridLine.getStyle().set("height", "1px");
            gridLine.getStyle().set("background-color", "var(--lumo-contrast-10pct)");
            gridLine.getStyle().set("bottom", (i * 25) + "%");

            Span gridLabel = new Span(String.format("%.0f €", maxValue * i / 4));
            gridLabel.getStyle().set("position", "absolute");
            gridLabel.getStyle().set("left", "-45px");
            gridLabel.getStyle().set("top", "-10px");
            gridLabel.addClassNames(
                    LumoUtility.FontSize.SMALL,
                    LumoUtility.TextColor.SECONDARY
            );

            gridLine.add(gridLabel);
            lineChart.add(gridLine);
        }

        // Punkte und Linien für 2024
        for (int i = 0; i < months.length; i++) {
            if (values2024[i] > 0) {
                // Datenpunkt
                Div point = new Div();
                point.getStyle().set("position", "absolute");
                point.getStyle().set("width", "8px");
                point.getStyle().set("height", "8px");
                point.getStyle().set("border-radius", "50%");
                point.getStyle().set("background-color", "#1676F3");
                point.getStyle().set("left", "calc(" + (i * (100.0 / (months.length - 1))) + "% - 4px)");
                point.getStyle().set("bottom", "calc(" + (values2024[i] / maxValue * 100) + "% - 4px)");

                lineChart.add(point);

                // Linie zum nächsten Punkt (wenn nicht der letzte Punkt)
                if (i < months.length - 1 && values2024[i + 1] > 0) {
                    Div line = new Div();
                    double length = Math.sqrt(
                            Math.pow(100.0 / (months.length - 1), 2) +
                                    Math.pow((values2024[i + 1] - values2024[i]) / maxValue * 100, 2)
                    );
                    double angle = Math.atan2(
                            (values2024[i + 1] - values2024[i]) / maxValue * 100,
                            100.0 / (months.length - 1)
                    );

                    line.getStyle().set("position", "absolute");
                    line.getStyle().set("width", length + "%");
                    line.getStyle().set("height", "2px");
                    line.getStyle().set("background-color", "#1676F3");
                    line.getStyle().set("left", (i * (100.0 / (months.length - 1))) + "%");
                    line.getStyle().set("bottom", (values2024[i] / maxValue * 100) + "%");
                    line.getStyle().set("transform-origin", "left bottom");
                    line.getStyle().set("transform", "rotate(" + Math.toDegrees(angle) + "deg)");

                    lineChart.add(line);
                }
            }
        }

        // Punkte und Linien für 2025
        for (int i = 0; i < months.length; i++) {
            if (values2025[i] > 0) {
                // Datenpunkt
                Div point = new Div();
                point.getStyle().set("position", "absolute");
                point.getStyle().set("width", "8px");
                point.getStyle().set("height", "8px");
                point.getStyle().set("border-radius", "50%");
                point.getStyle().set("background-color", "#FF6B6B");
                point.getStyle().set("left", "calc(" + (i * (100.0 / (months.length - 1))) + "% - 4px)");
                point.getStyle().set("bottom", "calc(" + (values2025[i] / maxValue * 100) + "% - 4px)");

                lineChart.add(point);

                // Linie zum nächsten Punkt (wenn nicht der letzte Punkt)
                if (i < months.length - 1 && values2025[i + 1] > 0) {
                    Div line = new Div();
                    double length = Math.sqrt(
                            Math.pow(100.0 / (months.length - 1), 2) +
                                    Math.pow((values2025[i + 1] - values2025[i]) / maxValue * 100, 2)
                    );
                    double angle = Math.atan2(
                            (values2025[i + 1] - values2025[i]) / maxValue * 100,
                            100.0 / (months.length - 1)
                    );

                    line.getStyle().set("position", "absolute");
                    line.getStyle().set("width", length + "%");
                    line.getStyle().set("height", "2px");
                    line.getStyle().set("background-color", "#FF6B6B");
                    line.getStyle().set("left", (i * (100.0 / (months.length - 1))) + "%");
                    line.getStyle().set("bottom", (values2025[i] / maxValue * 100) + "%");
                    line.getStyle().set("transform-origin", "left bottom");
                    line.getStyle().set("transform", "rotate(" + Math.toDegrees(angle) + "deg)");

                    lineChart.add(line);
                }
            }
        }

        chartContainer.add(lineChart, xAxis);
        chartLayout.add(chartTitle, legend, chartContainer);
        return chartLayout;
    }

    private VerticalLayout createMonthlyComparison() {
        VerticalLayout comparison = new VerticalLayout();
        comparison.addClassNames(
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.BoxShadow.XSMALL,
                LumoUtility.Margin.Top.MEDIUM
        );

        H4 comparisonTitle = new H4("Vergleich mit Vormonat");
        comparisonTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.SMALL);

        // Aktueller Monat und Vormonat für den Vergleich
        Month currentMonth = LocalDate.now().getMonth();
        Month previousMonth = LocalDate.now().minusMonths(1).getMonth();

        String currentMonthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.GERMAN);
        String previousMonthName = previousMonth.getDisplayName(TextStyle.FULL, Locale.GERMAN);

        // Beispieldaten für den Vergleich
        HorizontalLayout comparisonItems = new HorizontalLayout();
        comparisonItems.setWidthFull();
        comparisonItems.add(
                createComparisonItem(previousMonthName, "€445,70", ""),
                createComparisonItem(currentMonthName, "€460,30", "+3,3%"),
                createComparisonItem("Differenz", "+€14,60", "")
        );

        comparison.add(comparisonTitle, comparisonItems);
        return comparison;
    }

    private Component createComparisonItem(String label, String value, String change) {
        VerticalLayout item = new VerticalLayout();
        item.setSpacing(false);
        item.setPadding(false);

        Span labelText = new Span(label);
        labelText.addClassNames(
                LumoUtility.FontWeight.BOLD,
                LumoUtility.TextColor.SECONDARY
        );

        Span valueText = new Span(value);
        valueText.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.FontWeight.BOLD
        );

        item.add(labelText, valueText);

        if (!change.isEmpty()) {
            Span changeText = new Span(change);

            // Farbe je nach Wert (positiv/negativ)
            if (change.startsWith("+")) {
                changeText.addClassNames(LumoUtility.TextColor.ERROR);
            } else if (change.startsWith("-")) {
                changeText.addClassNames(LumoUtility.TextColor.SUCCESS);
            }

            item.add(changeText);
        }

        return item;
    }
}