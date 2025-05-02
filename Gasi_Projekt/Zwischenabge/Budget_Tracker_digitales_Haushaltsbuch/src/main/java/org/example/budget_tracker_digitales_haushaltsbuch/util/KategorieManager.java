package org.example.budget_tracker_digitales_haushaltsbuch.util;

import com.google.gson.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class KategorieManager {

    private static final String DATEI_PFAD = "kategorien.json";
    private static final List<String> defaultKategorien = List.of("Essen", "Miete", "Transport", "Freizeit", "Gesundheit");
    private static List<String> customKategorien = new ArrayList<>();

    static {
        ladeKategorien();
    }

    public static List<String> getAlleKategorien() {
        List<String> alle = new ArrayList<>(defaultKategorien);
        alle.addAll(customKategorien);
        return alle;
    }

    public static void addBenutzerKategorie(String neueKategorie) {
        if (neueKategorie != null && !neueKategorie.trim().isEmpty()
                && !defaultKategorien.contains(neueKategorie) && !customKategorien.contains(neueKategorie)) {
            customKategorien.add(neueKategorie);
            speichereKategorien();
        }
    }

    private static void ladeKategorien() {
        try {
            if (!Files.exists(Path.of(DATEI_PFAD))) {
                speichereKategorien(); // Datei initial erstellen
                return;
            }

            Reader reader = Files.newBufferedReader(Path.of(DATEI_PFAD));
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray benutzerdef = json.getAsJsonArray("custom");

            customKategorien.clear();
            for (JsonElement e : benutzerdef) {
                customKategorien.add(e.getAsString());
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Kategorien: " + e.getMessage());
        }
    }

    private static void speichereKategorien() {
        try (Writer writer = Files.newBufferedWriter(Path.of(DATEI_PFAD))) {
            JsonObject json = new JsonObject();
            json.add("default", new Gson().toJsonTree(defaultKategorien));
            json.add("custom", new Gson().toJsonTree(customKategorien));

            new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Kategorien: " + e.getMessage());
        }
    }
}
