package org.example.budget_tracker_digitales_haushaltsbuch.model;

import org.example.budget_tracker_digitales_haushaltsbuch.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetManager {

    private List<Buchung> buchungen = new ArrayList<>();

    // Konstruktor – lädt Daten aus der SQLite-Datenbank
    public BudgetManager() {
        DatabaseHelper.initializeDatabase();                         // Tabelle erstellen, falls nicht vorhanden
        buchungen.addAll(DatabaseHelper.loadBuchungen());            // Vorhandene Buchungen laden
    }

    // Fügt eine Buchung hinzu (Liste + Datenbank)
    public void buchungHinzufuegen(Buchung buchung) {
        buchungen.add(buchung);                                      // In interne Liste
        DatabaseHelper.insertBuchung(buchung);                       // In Datenbank speichern
    }

    // Entfernt eine Buchung (Liste + Datenbank)
    public void buchungEntfernen(Buchung buchung) {
        buchungen.remove(buchung);
        DatabaseHelper.deleteBuchung(buchung);                       // Optional: auch aus DB löschen
    }

    // Gibt alle Buchungen zurück
    public List<Buchung> getBuchungen() {
        return buchungen;
    }

    // Berechnet den Saldo aus Einnahmen und Ausgaben
    public double berechneSaldo() {
        return buchungen.stream().mapToDouble(b ->
                "Einnahme".equalsIgnoreCase(b.getTyp()) ? b.getBetrag() : -b.getBetrag()
        ).sum();
    }

    // Filtert nach Einnahme oder Ausgabe
    public List<Buchung> filternNachTyp(String typ) {
        return buchungen.stream()
                .filter(b -> b.getTyp().equalsIgnoreCase(typ))
                .collect(Collectors.toList());
    }

}
