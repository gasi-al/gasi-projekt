package org.example.budget_tracker_digitales_haushaltsbuch.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Modellklasse für eine einzelne Buchung.
 * Jede Buchung besteht aus Datum, Kategorie, Betrag, Typ und Kommentar.
 * Fun Fact: Diese Klasse ist ein klassisches JavaFX-Model mit sogenannten „Properties“ – ideal für Datenbindung!
 */
// Repräsentiert einen Buchungseintrag mit JavaFX-Properties
public class Buchung {

    /**
     * JavaFX Properties ermöglichen automatische Aktualisierung in der UI.
     * Fun Fact: Properties sind "observable" – Änderungen lösen UI-Updates aus, ohne dass du extra etwas tust.
     */
    // Datenfelder als JavaFX Properties für Bindung an UI-Elemente
    private final ObjectProperty<LocalDate> datum;
    private final StringProperty kategorie;
    private final DoubleProperty betrag;
    private final StringProperty typ;
    private final StringProperty kommentar;

    /**
     * Konstruktor zum Erstellen einer neuen Buchung.
     * Alle Felder werden direkt in ihre jeweiligen Property-Objekte verpackt.
     * Fun Fact: SimpleXxxProperty ist die einfachste Form der JavaFX-Properties – perfekt für Einsteiger.
     */
    // Konstruktor zur Initialisierung aller Felder
    public Buchung(LocalDate datum, String kategorie, double betrag, String typ, String kommentar) {
        this.datum = new SimpleObjectProperty<>(datum);
        this.kategorie = new SimpleStringProperty(kategorie);
        this.betrag = new SimpleDoubleProperty(betrag);
        this.typ = new SimpleStringProperty(typ);
        this.kommentar = new SimpleStringProperty(kommentar);
    }

    /**
     * Getter-Methoden für den direkten Zugriff auf Werte.
     * Fun Fact: Die meisten JavaFX-Komponenten greifen auf Werte UND Properties zu – daher brauchst du beides!
     */
    // Getter für das Datum
    public LocalDate getDatum() {
        return datum.get();
    }

    // Getter für die Kategorie
    public String getKategorie() {
        return kategorie.get();
    }

    // Getter für den Betrag
    public double getBetrag() {
        return betrag.get();
    }

    // Getter für den Typ (Einnahme/Ausgabe)
    public String getTyp() {
        return typ.get();
    }

    // Getter für den Kommentar
    public String getKommentar() {
        return kommentar.get();
    }

    /**
     * Property-Methoden zur Verwendung in JavaFX-Bindungen (z. B. TableView).
     * Fun Fact: Nur Properties können live mit UI-Elementen synchronisiert werden – deshalb brauchst du diese Methoden.
     */
    // Property-Zugriff für das Datum
    public ObjectProperty<LocalDate> datumProperty() {
        return datum;
    }

    // Property-Zugriff für die Kategorie
    public StringProperty kategorieProperty() {
        return kategorie;
    }

    // Property-Zugriff für den Betrag
    public DoubleProperty betragProperty() {
        return betrag;
    }

    // Property-Zugriff für den Typ
    public StringProperty typProperty() {
        return typ;
    }

    // Property-Zugriff für den Kommentar
    public StringProperty kommentarProperty() {
        return kommentar;
    }
}
