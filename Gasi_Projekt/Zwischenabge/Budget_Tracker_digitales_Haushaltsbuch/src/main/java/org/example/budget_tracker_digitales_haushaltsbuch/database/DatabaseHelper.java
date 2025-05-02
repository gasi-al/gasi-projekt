package org.example.budget_tracker_digitales_haushaltsbuch.database;

import org.example.budget_tracker_digitales_haushaltsbuch.model.Buchung;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String URL = "jdbc:sqlite:C:\\Users\\gasia\\Desktopbuchungen.db";

    private static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Verbindung zur Datenbank fehlgeschlagen: " + e.getMessage());
            return null;
        }
    }

    public static void initializeDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS buchung (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    datum TEXT NOT NULL,
                    kategorie TEXT NOT NULL,
                    betrag REAL NOT NULL,
                    typ TEXT NOT NULL,
                    kommentar TEXT
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Fehler beim Initialisieren der Datenbank: " + e.getMessage());
        }
    }

    public static void insertBuchung(Buchung buchung) {
        String sql = "INSERT INTO buchung (datum, kategorie, betrag, typ, kommentar) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, buchung.getDatum().toString());
            pstmt.setString(2, buchung.getKategorie());
            pstmt.setDouble(3, buchung.getBetrag());
            pstmt.setString(4, buchung.getTyp());
            pstmt.setString(5, buchung.getKommentar());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fehler beim Einfügen der Buchung: " + e.getMessage());
        }
    }

    public static List<Buchung> loadBuchungen() {
        List<Buchung> buchungen = new ArrayList<>();
        String sql = "SELECT datum, kategorie, betrag, typ, kommentar FROM buchung";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate datum = LocalDate.parse(rs.getString("datum"));
                String kategorie = rs.getString("kategorie");
                double betrag = rs.getDouble("betrag");
                String typ = rs.getString("typ");
                String kommentar = rs.getString("kommentar");

                buchungen.add(new Buchung(datum, kategorie, betrag, typ, kommentar));
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Laden der Buchungen: " + e.getMessage());
        }

        return buchungen;
    }

    public static void deleteBuchung(Buchung buchung) {
        String sql = "DELETE FROM buchung WHERE datum = ? AND kategorie = ? AND betrag = ? AND typ = ? AND kommentar = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, buchung.getDatum().toString());
            pstmt.setString(2, buchung.getKategorie());
            pstmt.setDouble(3, buchung.getBetrag());
            pstmt.setString(4, buchung.getTyp());
            pstmt.setString(5, buchung.getKommentar());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen der Buchung: " + e.getMessage());
        }
    }

    public static void updateBuchung(Buchung alt, Buchung neu) {
        String sql = """
                UPDATE buchung SET
                    datum = ?, kategorie = ?, betrag = ?, typ = ?, kommentar = ?
                WHERE
                    datum = ? AND kategorie = ? AND betrag = ? AND typ = ? AND kommentar = ?
                """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, neu.getDatum().toString());
            pstmt.setString(2, neu.getKategorie());
            pstmt.setDouble(3, neu.getBetrag());
            pstmt.setString(4, neu.getTyp());
            pstmt.setString(5, neu.getKommentar());

            pstmt.setString(6, alt.getDatum().toString());
            pstmt.setString(7, alt.getKategorie());
            pstmt.setDouble(8, alt.getBetrag());
            pstmt.setString(9, alt.getTyp());
            pstmt.setString(10, alt.getKommentar());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fehler beim Aktualisieren der Buchung: " + e.getMessage());
        }
    }
}
