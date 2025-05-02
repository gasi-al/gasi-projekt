package org.example.budget_tracker_digitales_haushaltsbuch.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.example.budget_tracker_digitales_haushaltsbuch.model.Buchung;
import org.example.budget_tracker_digitales_haushaltsbuch.model.BudgetManager;
import javafx.stage.FileChooser;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.budget_tracker_digitales_haushaltsbuch.database.DatabaseHelper;
import org.example.budget_tracker_digitales_haushaltsbuch.util.KategorieManager;

import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;

public class BudgetController {

    @FXML
    private DatePicker datumPicker;
    @FXML
    private ComboBox<String> kategorieComboBox;
    @FXML
    private TextField betragField;
    @FXML
    private ChoiceBox<String> typChoiceBox;
    @FXML
    private ChoiceBox<String> filterChoiceBox;
    @FXML
    private TextField kommentarField;
    @FXML
    private TableView<Buchung> buchungTabelle;
    @FXML
    private TableColumn<Buchung, LocalDate> datumColumn;
    @FXML
    private TableColumn<Buchung, String> kategorieColumn;
    @FXML
    private TableColumn<Buchung, Double> betragColumn;
    @FXML
    private TableColumn<Buchung, String> typColumn;
    @FXML
    private TableColumn<Buchung, String> kommentarColumn;
    @FXML
    private Label saldoLabel;
    @FXML
    private Label einnahmenLabel;
    @FXML
    private Label ausgabenLabel;
    @FXML
    private TextField suchfeld;


    private BudgetManager manager = new BudgetManager();
    private ObservableList<Buchung> observableBuchungen = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        typChoiceBox.setItems(FXCollections.observableArrayList("Einnahme", "Ausgabe"));
        typChoiceBox.setValue("Einnahme");

        filterChoiceBox.setItems(FXCollections.observableArrayList("Alle", "Einnahme", "Ausgabe"));
        filterChoiceBox.setValue("Alle");

        buchungTabelle.setItems(observableBuchungen);


        datumColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
        kategorieColumn.setCellValueFactory(new PropertyValueFactory<>("kategorie"));
        betragColumn.setCellValueFactory(new PropertyValueFactory<>("betrag"));
        typColumn.setCellValueFactory(new PropertyValueFactory<>("typ"));
        kommentarColumn.setCellValueFactory(new PropertyValueFactory<>("kommentar"));


        betragColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double betrag, boolean empty) {
                super.updateItem(betrag, empty);
                if (empty || betrag == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", betrag));
                }
            }
        });

        suchfeld.textProperty().addListener((observable, oldValue, newValue) -> {
            sucheBuchungen();
        });

        buchungTabelle.getSelectionModel().selectedItemProperty().addListener((obs, alt, neu) -> {
            if (neu != null) {
                datumPicker.setValue(neu.getDatum());
                kategorieComboBox.getEditor().setText(neu.getKategorie());
                betragField.setText(String.valueOf(neu.getBetrag()));
                typChoiceBox.setValue(neu.getTyp());
                kommentarField.setText(neu.getKommentar());
            }
        });


        kategorieComboBox.setEditable(true);
        kategorieComboBox.setItems(FXCollections.observableArrayList(KategorieManager.getAlleKategorien()));

        updateSummenAnzeigen();
    }

    @FXML
    public void handleHinzufuegen() {
        try {
            LocalDate datum = datumPicker.getValue();


            if (datum == null) {
                datum = LocalDate.now();
                datumPicker.setValue(datum);
            }

            String kategorie = kategorieComboBox.getEditor().getText();
            KategorieManager.addBenutzerKategorie(kategorie);
            kategorieComboBox.setItems(FXCollections.observableArrayList(KategorieManager.getAlleKategorien()));
            String betragText = betragField.getText().replace("€", "").trim();
            double betrag = Double.parseDouble(betragText);
            String typ = typChoiceBox.getValue();
            String kommentar = kommentarField.getText();

            if (kategorie.isEmpty() || kommentar.isEmpty() || typ == null) {
                showAlert("Eingabefehler", "Bitte alle Felder korrekt ausfüllen.");
                return;
            }

            Buchung buchung = new Buchung(datum, kategorie, betrag, typ, kommentar);
            manager.buchungHinzufuegen(buchung);
            observableBuchungen.add(buchung);

            updateSummenAnzeigen();
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
    public void handleFiltern() {
        try {
            String filter = filterChoiceBox.getValue();
            List<Buchung> alle = manager.getBuchungen();

            List<Buchung> gefilterteBuchungen = alle.stream()
                    .filter(b -> "Alle".equalsIgnoreCase(filter) || b.getTyp().equalsIgnoreCase(filter))
                    .collect(Collectors.toList());

            observableBuchungen.setAll(gefilterteBuchungen);
        } catch (Exception e) {
            showAlert("Fehler beim Filtern", "Bitte überprüfe die Auswahl.");
        }
    }

    @FXML
    public void handleLoeschen() {
        Buchung ausgewaehlt = buchungTabelle.getSelectionModel().getSelectedItem();
        if (ausgewaehlt != null) {
            manager.buchungEntfernen(ausgewaehlt);
            observableBuchungen.remove(ausgewaehlt);
            updateSummenAnzeigen();
        } else {
            showAlert("Löschen", "Keine Buchung ausgewählt.");
        }
    }

    @FXML
    public void handleExportieren() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buchungen als CSV speichern");
        fileChooser.setInitialFileName("buchungen.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Datum,Kategorie,Betrag,Typ,Kommentar");
                for (Buchung b : observableBuchungen) {
                    writer.printf("%s,%s,%.2f,%s,%s%n",
                            b.getDatum(), b.getKategorie(), b.getBetrag(), b.getTyp(), b.getKommentar());
                }
                showAlert("Export erfolgreich", "CSV-Datei wurde gespeichert.");
            } catch (IOException e) {
                showAlert("Fehler beim Export", "Die Datei konnte nicht gespeichert werden.");
            }
        }
    }

    @FXML
    private void sucheBuchungen() {
        String suchtext = suchfeld.getText().toLowerCase().trim();

        if (suchtext.isEmpty()) {
            observableBuchungen.setAll(manager.getBuchungen());
            return;
        }

        List<Buchung> gefiltert = manager.getBuchungen().stream()
                .filter(b -> b.getKategorie().toLowerCase().contains(suchtext) ||
                        b.getKommentar().toLowerCase().contains(suchtext))
                .collect(Collectors.toList());

        observableBuchungen.setAll(gefiltert);
    }

    @FXML
    private void handleStatistikAnzeigen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/statistics_view.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Statistik");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Fehler", "Statistikansicht konnte nicht geladen werden.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Fehler", "Ein unerwarteter Fehler ist aufgetreten.");
        }
    }

    @FXML
    private void handleBearbeiten() {
        Buchung alteBuchung = buchungTabelle.getSelectionModel().getSelectedItem();
        if (alteBuchung == null) {
            showAlert("Bearbeiten", "Bitte wähle einen Eintrag aus.");
            return;
        }

        try {
            LocalDate datum = datumPicker.getValue();
            String kategorie = kategorieComboBox.getEditor().getText();
            double betrag = Double.parseDouble(betragField.getText().replace("€", "").trim());
            String typ = typChoiceBox.getValue();
            String kommentar = kommentarField.getText();

            Buchung neueBuchung = new Buchung(datum, kategorie, betrag, typ, kommentar);

            // Werte im Modell aktualisieren
            alteBuchung.setDatum(datum);
            alteBuchung.setKategorie(kategorie);
            alteBuchung.setBetrag(betrag);
            alteBuchung.setTyp(typ);
            alteBuchung.setKommentar(kommentar);

            DatabaseHelper.updateBuchung(alteBuchung, neueBuchung);

            buchungTabelle.refresh();
            updateSummenAnzeigen();
            clearFields();
            showAlert("Bearbeiten", "Eintrag wurde erfolgreich aktualisiert.");

        } catch (Exception e) {
            showAlert("Fehler", "Bearbeiten fehlgeschlagen: " + e.getMessage());
        }
    }


    private void updateSummenAnzeigen() {
        try {
            double saldo = manager.berechneSaldo();
            double einnahmen = manager.getBuchungen().stream()
                    .filter(b -> "Einnahme".equals(b.getTyp()))
                    .mapToDouble(Buchung::getBetrag)
                    .sum();

            double ausgaben = manager.getBuchungen().stream()
                    .filter(b -> "Ausgabe".equals(b.getTyp()))
                    .mapToDouble(Buchung::getBetrag)
                    .sum();

            saldoLabel.setText(String.format("Saldo: %.2f €", saldo));
            einnahmenLabel.setText(String.format("Einnahmen: %.2f €", einnahmen));
            ausgabenLabel.setText(String.format("Ausgaben: %.2f €", ausgaben));
        } catch (Exception e) {
            showAlert("Fehler bei Summenanzeige", "Ein unerwarteter Fehler ist aufgetreten.");
        }
    }

    private void clearFields() {
        datumPicker.setValue(null);
        kategorieComboBox.getEditor().clear();
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
