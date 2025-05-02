package org.example.budget_tracker_digitales_haushaltsbuch.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import org.example.budget_tracker_digitales_haushaltsbuch.model.Buchung;
import org.example.budget_tracker_digitales_haushaltsbuch.model.BudgetManager;

public class StatistikController {

    @FXML
    private PieChart pieChart;

    private BudgetManager manager = new BudgetManager();

    @FXML
    public void initialize() {
        double einnahmen = manager.getBuchungen().stream()
                .filter(b -> "Einnahme".equalsIgnoreCase(b.getTyp()))
                .mapToDouble(Buchung::getBetrag)
                .sum();

        double ausgaben = manager.getBuchungen().stream()
                .filter(b -> "Ausgabe".equalsIgnoreCase(b.getTyp()))
                .mapToDouble(Buchung::getBetrag)
                .sum();

        pieChart.getData().add(new PieChart.Data("Einnahmen", einnahmen));
        pieChart.getData().add(new PieChart.Data("Ausgaben", ausgaben));
    }
}
