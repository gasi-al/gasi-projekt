package org.example.budget_tracker_digitales_haushaltsbuch.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.budget_tracker_digitales_haushaltsbuch.model.Buchung;
import org.example.budget_tracker_digitales_haushaltsbuch.model.BudgetManager;

import java.time.LocalDate;

public class BudgetController {

    @FXML private DatePicker datumPicker;
    @FXML private TextField kategorieField;
    @FXML private TextField betragField;
    @FXML private ChoiceBox<String> typChoiceBox;
    @FXML private TextField kommentarField;
    @FXML private TableView<Buchung> buchungTabelle;
    @FXML private TableColumn<Buchung, LocalDate> datumColumn;
    @FXML private TableColumn<Buchung, String> kategorieColumn;
    @FXML private TableColumn<Buchung, Double> betragColumn;
    @FXML private TableColumn<Buchung, String> typColumn;
    @FXML private TableColumn<Buchung, String> kommentarColumn;
    @FXML private Label saldoLabel;

    private BudgetManager manager = new BudgetManager();
    private ObservableList<Buchung> observableBuchungen = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Dropdown
        typChoiceBox.setItems(FXCollections.observableArrayList("Einnahme", "Ausgabe"));
        typChoiceBox.setValue("Einnahme");

        // Tabelle verknüpfen
        datumColumn.setCellValueFactory(cellData -> cellData.getValue().datumProperty());
        kategorieColumn.setCellValueFactory(cellData -> cellData.getValue().kategorieProperty());
        betragColumn.setCellValueFactory(cellData -> cellData.getValue().betragProperty().asObject());
        typColumn.setCellValueFactory(cellData -> cellData.getValue().typProperty());
        kommentarColumn.setCellValueFactory(cellData -> cellData.getValue().kommentarProperty());

        // Tabelle mit Daten verbinden
        buchungTabelle.setItems(observableBuchungen);

        updateSaldoLabel();
    }

    @FXML
    public void handleHinzufuegen() {
        try {
            LocalDate datum = datumPicker.getValue();
            String kategorie = kategorieField.getText();
            String betragText = betragField.getText().replace("€", "").trim();
            double betrag = Double.parseDouble(betragText);
            String typ = typChoiceBox.getValue();
            String kommentar = kommentarField.getText();

            Buchung buchung = new Buchung(datum, kategorie, betrag, typ, kommentar);
            manager.buchungHinzufuegen(buchung);
            observableBuchungen.add(buchung);

            updateSaldoLabel();
            clearFields();
        } catch (NumberFormatException e) {
            System.out.println("Ungültiger Betrag: " + betragField.getText());
            showAlert("Ungültiger Betrag", "Bitte gib eine gültige Zahl ein, z. B. 100 oder 100.00");
        } catch (Exception e) {
            System.out.println("Fehler beim Hinzufügen: " + e.getMessage());
            showAlert("Fehler", "Ein unerwarteter Fehler ist aufgetreten.");
        }
    }

    @FXML
    public void handleLoeschen() {
        Buchung ausgewaehlt = buchungTabelle.getSelectionModel().getSelectedItem();
        if (ausgewaehlt != null) {
            manager.buchungEntfernen(ausgewaehlt); // Du brauchst ggf. eine passende Methode in BudgetManager
            observableBuchungen.remove(ausgewaehlt);
            saldoLabel.setText(String.format("Saldo: %.2f €", manager.berechneSaldo()));
        } else {
            System.out.println("Keine Buchung ausgewählt zum Löschen.");
        }
    }

    private void updateSaldoLabel() {
        if (saldoLabel != null) {
            double saldo = manager.berechneSaldo();
            saldoLabel.setText(String.format("Saldo: %.2f €", saldo));
        }
    }

    private void clearFields() {
        datumPicker.setValue(null);
        kategorieField.clear();
        betragField.clear();
        kommentarField.clear();
        typChoiceBox.setValue("Einnahme");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
