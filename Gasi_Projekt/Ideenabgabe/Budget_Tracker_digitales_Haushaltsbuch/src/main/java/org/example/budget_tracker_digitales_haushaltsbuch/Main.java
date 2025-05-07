/* Das ist das Hauptpaket deines Programms. Es gehört zum Projekt "Budget Tracker".*/
package org.example.budget_tracker_digitales_haushaltsbuch;

/* Diese Klasse kommt aus JavaFX und sorgt dafür, dass das Programm als grafische Anwendung läuft.
   Fun Fact: JavaFX ist der offizielle Nachfolger von Swing für moderne GUI-Entwicklung in Java. */
import javafx.application.Application;

/* Damit kann man FXML-Dateien laden (dein grafisches Layout wird damit geladen).
   Fun Fact: FXML ist wie HTML für JavaFX – es trennt Logik und Design. */
import javafx.fxml.FXMLLoader;

/* Die Szene ist der sichtbare Bereich (Fensterinhalt) deiner Anwendung.
   Fun Fact: Eine Scene kann dynamisch ausgetauscht werden – du kannst so zwischen "Seiten" wechseln. */
import javafx.scene.Scene;

/* Das ist das eigentliche Fenster (wie ein Programmfenster unter Windows).
   Fun Fact: Stage = Bühne. JavaFX nutzt Theaterbegriffe: Bühne (Stage), Szene (Scene), Schauspieler (Nodes). */
import javafx.stage.Stage;

/* Hauptklasse deines Programms. Sie erbt von "Application", damit sie als JavaFX-App läuft. */
public class Main extends Application {

    /* Diese Methode wird automatisch aufgerufen, wenn das Programm startet. */
    @Override
    public void start(Stage stage) throws Exception {
        // FXML-Datei (UI-Layout) laden
        /* Lädt das FXML-Layout aus der Datei "budget_view.fxml" (dein UI-Design). */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/budget_view.fxml"));

        // Szene ins Fenster setzen und anzeigen
        /* Erstellt eine neue Szene mit dem geladenen Layout. */
        Scene scene = new Scene(loader.load());

        /* Setzt die Szene in das Fenster (Stage). */
        stage.setScene(scene);

        /* Gibt dem Fenster einen Titel. */
        stage.setTitle("Budget Tracker");

        /* Zeigt das Fenster an. */
        stage.show();
    }

    public static void main(String[] args) {
        // startet JavaFX Anwendung
        launch(args);
        /* Ruft intern die start()-Methode auf.
         Fun Fact: launch() darf nur einmal pro JVM ausgeführt werden – sonst fliegt eine Exception! */
    }
}
