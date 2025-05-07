package org.example.budget_tracker_digitales_haushaltsbuch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Diese Klasse verwaltet alle Buchungen im Haushaltsbuch.
 * Sie übernimmt das Speichern, Filtern und Berechnen des Saldos.
 * Fun Fact: Diese Klasse entspricht dem "Model" im MVC-Muster – die Datenlogik ist hier zentral gebündelt.
 */
// Verwaltet Buchungen und rechnet mit ihnen (Model-Klasse)
public class BudgetManager {

    /**
     * Interne Liste aller Buchungen.
     * Fun Fact: Eine ArrayList ist ideal für dynamische Daten, die oft verändert werden.
     */
    // Liste zur Speicherung aller Buchungseinträge
    private List<Buchung> buchungen = new ArrayList<>();

    /**
     * Fügt eine neue Buchung zur Liste hinzu.
     * Fun Fact: Die Liste wächst automatisch – kein manuelles Größenmanagement wie in Arrays!
     */
    // Fügt eine neue Buchung hinzu
    public void buchungHinzufuegen(Buchung buchung) {
        buchungen.add(buchung);
    }

    /**
     * Gibt die vollständige Liste aller Buchungen zurück.
     * Fun Fact: Diese Methode wird z. B. von der UI genutzt, um Daten in die Tabelle zu laden.
     */
    // Gibt alle gespeicherten Buchungen zurück
    public List<Buchung> getAlleBuchungen() {
        return buchungen;
    }

    /**
     * Berechnet den aktuellen Saldo basierend auf Einnahmen und Ausgaben.
     * Einnahmen werden addiert, Ausgaben subtrahiert.
     * Fun Fact: `stream()` + `mapToDouble()` ist funktionale Programmierung – sehr elegant und kompakt!
     */
    // Berechnet den Gesamtsaldo aus allen Buchungen
    public double berechneSaldo() {
        return buchungen.stream().mapToDouble(b ->
                b.getTyp().equalsIgnoreCase("Einnahme") ? b.getBetrag() : -b.getBetrag()
        ).sum();
    }

    /**
     * Filtert die Buchungen nach einem bestimmten Typ (z. B. "Einnahme" oder "Ausgabe").
     * Fun Fact: Mit Streams ist das nur eine einzige Zeile – ohne Schleifen oder temporäre Listen!
     */
    // Gibt alle Buchungen eines bestimmten Typs zurück
    public List<Buchung> filternNachTyp(String typ) {
        return buchungen.stream()
                .filter(b -> b.getTyp().equalsIgnoreCase(typ))
                .collect(Collectors.toList());
    }

    /**
     * Entfernt eine Buchung aus der Liste.
     * Fun Fact: `remove()` sucht intern per equals() – funktioniert hier, weil `Buchung` als Objekt eindeutig ist.
     */
    // Entfernt eine Buchung aus der Liste
    public void buchungEntfernen(Buchung buchung) {
        buchungen.remove(buchung);
    }
}
