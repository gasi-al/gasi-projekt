package org.example.budget_tracker_digitales_haushaltsbuch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetManager {
    private List<Buchung> buchungen = new ArrayList<>();

    public void buchungHinzufuegen(Buchung buchung) {
        buchungen.add(buchung);
    }

    public List<Buchung> getAlleBuchungen() {
        return buchungen;
    }

    public double berechneSaldo() {
        return buchungen.stream().mapToDouble(b ->
                b.getTyp().equalsIgnoreCase("Einnahme") ? b.getBetrag() : -b.getBetrag()
        ).sum();
    }

    public List<Buchung> filternNachTyp(String typ) {
        return buchungen.stream()
                .filter(b -> b.getTyp().equalsIgnoreCase(typ))
                .collect(Collectors.toList());
    }
    public void buchungEntfernen(Buchung buchung) {
        buchungen.remove(buchung);
    }

}
