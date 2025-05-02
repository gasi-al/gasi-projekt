package org.example.budget_tracker_digitales_haushaltsbuch.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Buchung {

    private final ObjectProperty<LocalDate> datum;
    private final StringProperty kategorie;
    private final DoubleProperty betrag;
    private final StringProperty typ;
    private final StringProperty kommentar;

    public Buchung(LocalDate datum, String kategorie, double betrag, String typ, String kommentar) {
        this.datum = new SimpleObjectProperty<>(datum);
        this.kategorie = new SimpleStringProperty(kategorie);
        this.betrag = new SimpleDoubleProperty(betrag);
        this.typ = new SimpleStringProperty(typ);
        this.kommentar = new SimpleStringProperty(kommentar);
    }

    // Getter
    public LocalDate getDatum() {
        return datum.get();
    }

    public String getKategorie() {
        return kategorie.get();
    }

    public double getBetrag() {
        return betrag.get();
    }

    public String getTyp() {
        return typ.get();
    }

    public String getKommentar() {
        return kommentar.get();
    }

    // Setter (wichtig für Bearbeiten-Funktion!)
    public void setDatum(LocalDate datum) {
        this.datum.set(datum);
    }

    public void setKategorie(String kategorie) {
        this.kategorie.set(kategorie);
    }

    public void setBetrag(double betrag) {
        this.betrag.set(betrag);
    }

    public void setTyp(String typ) {
        this.typ.set(typ);
    }

    public void setKommentar(String kommentar) {
        this.kommentar.set(kommentar);
    }

    // JavaFX Properties (für TableView-Bindung)
    public ObjectProperty<LocalDate> datumProperty() {
        return datum;
    }

    public StringProperty kategorieProperty() {
        return kategorie;
    }

    public DoubleProperty betragProperty() {
        return betrag;
    }

    public StringProperty typProperty() {
        return typ;
    }

    public StringProperty kommentarProperty() {
        return kommentar;
    }
}
