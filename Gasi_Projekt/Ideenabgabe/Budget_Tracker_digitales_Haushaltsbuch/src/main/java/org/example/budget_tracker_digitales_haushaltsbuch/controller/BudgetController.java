package org.example.budget_tracker_digitales_haushaltsbuch.controller;

/**
 * Importiert alle notwendigen JavaFX-Klassen für das UI.
 * Fun Fact: JavaFX wird oft unterschätzt, ist aber mächtig genug für moderne Desktop-Apps!
 */
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Importiert die Datenklassen aus deinem eigenen Projekt.
 * Fun Fact: Gute Trennung zwischen "Model" und "Controller" folgt dem MVC-Prinzip – macht den Code wartbar!
 */
import org.example.budget_tracker_digitales_haushaltsbuch.model.Buchung;
import org.example.budget_tracker_digitales_haushaltsbuch.model.BudgetManager;

import java.time.LocalDate;

/**
 * Der Controller für das Haushaltsbuch.
 * Verknüpft die Benutzeroberfläche (FXML) mit der Logik (Java).
 * Fun Fact: Der Controller wird von JavaFX automatisch beim Start geladen – kein manuelles new() nötig!
 */
// Controller zur Steuerung der UI-Logik
public class BudgetController {

    /**
     * Diese Variablen sind direkt mit den Steuerelementen in der FXML-Datei verbunden.
     * Fun Fact: Das passiert automatisch zur Laufzeit durch den FXML-Loader – Magie durch @FXML!
     */
    // UI-Elemente aus der FXML-Datei
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

    /**
     * Diese beiden Objekte verwalten die Daten:
     * - BudgetManager: kümmert sich um die Logik
     * - observableBuchungen: verbindet Daten mit der UI-Tabelle
     * Fun Fact: ObservableList sorgt dafür, dass sich die UI automatisch aktualisiert – kein manueller Refresh nötig!
     */
    // Datenmodell und observable Liste für Tabelle
    private BudgetManager manager = new BudgetManager();
    private ObservableList<Buchung> observableBuchungen = FXCollections.observableArrayList();

    /**
     * Diese Methode wird von JavaFX automatisch nach dem Laden der FXML aufgerufen.
     * Hier wird alles initialisiert: Dropdown, Tabellenstruktur, Datenbindung.
     * Fun Fact: Du brauchst hier keinen Konstruktor – JavaFX nutzt Reflection!
     */
    // Initialisiert UI-Elemente nach dem Laden
    @FXML
    public void initialize() {
        typChoiceBox.setItems(FXCollections.observableArrayList("Einnahme", "Ausgabe"));
        typChoiceBox.setValue("Einnahme"); // Default-Wert
        // Setzt Auswahlmöglichkeiten in der ChoiceBox

        datumColumn.setCellValueFactory(cellData -> cellData.getValue().datumProperty());
        kategorieColumn.setCellValueFactory(cellData -> cellData.getValue().kategorieProperty());
        betragColumn.setCellValueFactory(cellData -> cellData.getValue().betragProperty().asObject());
        typColumn.setCellValueFactory(cellData -> cellData.getValue().typProperty());
        kommentarColumn.setCellValueFactory(cellData -> cellData.getValue().kommentarProperty());
        // Bindet Tabellen-Spalten an Eigenschaften des Buchung-Objekts

        buchungTabelle.setItems(observableBuchungen);
        // Verbindet Tabelle mit der Datenliste

        updateSaldoLabel();
        // Zeigt den aktuellen Saldo an

    }

    /**
     * Wird aufgerufen, wenn der Nutzer auf "Hinzufügen" klickt.
     * Liest die Daten aus der UI, erstellt eine neue Buchung und fügt sie hinzu.
     * Fun Fact: `.replace("€", "")` entfernt Eurozeichen, damit auch copy-paste aus anderen Quellen klappt!
     */
    // Fügt neue Buchung hinzu und aktualisiert die Anzeige
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
            /**
             * Fehlerbehandlung bei ungültiger Zahleneingabe.
             * Fun Fact: Nutzer tippen gerne mal "10€" oder "10,00" – deshalb besser vorher prüfen!
             */
            // Zeigt Warnung bei ungültigem Betrag
            System.out.println("Ungültiger Betrag: " + betragField.getText());
            showAlert("Ungültiger Betrag", "Bitte gib eine gültige Zahl ein, z. B. 100 oder 100.00");
        } catch (Exception e) {
            /**
             * Allgemeine Fehlerbehandlung – falls etwas Unerwartetes passiert.
             * Fun Fact: try-catch-Blöcke helfen, dass das Programm nicht abstürzt, sondern nützlich reagiert.
             */
            // Allgemeine Fehlermeldung bei unerwarteten Problemen
            System.out.println("Fehler beim Hinzufügen: " + e.getMessage());
            showAlert("Fehler", "Ein unerwarteter Fehler ist aufgetreten.");
        }
    }

    /**
     * Wird aufgerufen, wenn der Nutzer eine Buchung löschen will.
     * Entfernt den Eintrag aus der Tabelle und aus dem BudgetManager.
     * Fun Fact: Tabellen können leer sein – daher immer erst prüfen, ob etwas ausgewählt wurde!
     */
    // Löscht ausgewählte Buchung aus der Tabelle
    @FXML
    public void handleLoeschen() {
        Buchung ausgewaehlt = buchungTabelle.getSelectionModel().getSelectedItem();
        if (ausgewaehlt != null) {
            manager.buchungEntfernen(ausgewaehlt); // Methode muss in BudgetManager existieren
            observableBuchungen.remove(ausgewaehlt);
            saldoLabel.setText(String.format("Saldo: %.2f €", manager.berechneSaldo()));
        } else {
            // Keine Buchung ausgewählt
            System.out.println("Keine Buchung ausgewählt zum Löschen.");
        }
    }

    /**
     * Aktualisiert den angezeigten Saldo.
     * Wird nach jeder Änderung aufgerufen.
     * Fun Fact: Das String-Format "%.2f €" sorgt dafür, dass immer zwei Nachkommastellen angezeigt werden – wie beim Kontoauszug!
     */
    // Zeigt den aktuellen Kontostand an
    private void updateSaldoLabel() {
        if (saldoLabel != null) {
            double saldo = manager.berechneSaldo();
            saldoLabel.setText(String.format("Saldo: %.2f €", saldo));
        }
    }

    /**
     * Leert die Eingabefelder nach dem Hinzufügen.
     * Fun Fact: Dadurch fühlt sich das Programm "sauber" und benutzerfreundlich an – wichtig für UX!
     */
    // Leert alle Eingabefelder
    private void clearFields() {
        datumPicker.setValue(null);
        kategorieField.clear();
        betragField.clear();
        kommentarField.clear();
        typChoiceBox.setValue("Einnahme");
    }

    /**
     * Zeigt ein Warnfenster mit Titel und Nachricht.
     * Fun Fact: JavaFX-Alerts sind superpraktisch, weil man keinen extra Dialog-Controller braucht!
     */
    // Zeigt eine Warnung oder Fehlermeldung im Dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
