package org.vertragsverwaltung.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class FileRepository {
    // Klasse mit Methoden für das Bearbeiten, Lesen und Entfernen von Dateien
    public JSONObject getJsonObject(String path) {
        /**
         * Die Methode liest ein JSONObject aus einer Datei mit dem angegebenen Pfad und gibt es zurück
         * @param path Der Dateipfad zur JSON-Datei
         * @return Das neue JSONObject oder null, wenn eine Exception auftritt
         */

        try {
            JSONParser jsonParser = new JSONParser();
            // Initialisiert den JSONParser jsonParser

            FileReader reader = new FileReader(path);
            // Öffnet den FileReader reader mit dem eingespeisten Dateipfad

            return (JSONObject) jsonParser.parse((reader));
            // Parst den Inhalt der Datei und gibt das JSONObject zurück

        } catch (IOException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return null;
            // Behandelt eine IOException, falls das Lesen der Datei nicht funktioniert und gibt null zurück
        } catch (ParseException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return null;
            // Behandelt eine ParseException, falls das Parsen der Datei nicht funktioniert und gibt null zurück
        }
    }

    public boolean writeFile(String path, JSONObject jsonObject) {
        /**
         * Schreibt das eingespeiste JSONObject in eine Datei mit dem angegebenen Pfad
         * @param path Der Dateipfad zur zuschreibenden Datei
         * @param jsonObject Das JSONObject, welches geschrieben werden soll
         * @return true, wenn das Schreiben erfolgreich war, sonst false
         */
        try {
            FileWriter fileWriter = new FileWriter(path);
            // Initialisiert den FileWriter fileWriter

            fileWriter.write(jsonObject.toJSONString());
            // Schreibt das JSONObject als String in die Datei

            fileWriter.close();
            // Schließt den fileWriter

            return true;
            // Gibt bei Erfolg true zurück

        } catch (IOException e) {
            return false;
            // Behandelt eine IOException, wenn das Schreiben nicht funktioniert und gibt false zurück
        }
    }

    public boolean deleteFile(JSONObject jsonObject) {
        /**
         * Löscht eine Datei anhand der vsnr, die im JSONObject abgerufen wird
         * @param jsonObject Das JSONObject, aus dem die vsnr entnommen wird
         * @return true, wenn das Löschen erfolgrich war, sonst false
         */

        File delFile = null;
        // Deklariert File delFile vor der abgekapselten try-catch-Abfrage

        try {
            int vsnr = (int) (long) jsonObject.get("vsnr");
            // Entnimmt den value des keys "vsnr" aus dem JSONObject

            delFile = new File(srcPath() + "\\\\main\\\\resources\\\\vertraege\\\\" + vsnr + ".json");
            // weist delFile den zu löschenden Dateipfad zu

        } catch (NullPointerException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return false;
            // Behandelt eine NullPointerException, wenn der vsnr-key nicht im JSONObject vorhanden ist und gibt false zurück

        } catch (ClassCastException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return false;
            // Behandelt eine ClassCastException, wenn die vsnr nicht vom erwarteten typ ist (Zeichenfolge)

        }
        return delFile.delete();
        // Löscht die Datei und gibt den Rückgabetyp der Methode zurück
    }

    public String getFahrzeugHersteller(JSONObject jsonObject) {
        /**
         * Extrahiert den Fahrzeughersteller aus dem angegebenen JSONObject
         * @param jsonObject Das JSONObject, aus dem der Fahrzeughersteller entnommen wird
         * @return der Fahrzeughersteller als String
         */
        return jsonObject.get("fahrzeug_hersteller").toString().toLowerCase();
    }

    public String srcPath() {
        /**
         * Extrahiert den Dateipfad des Programms bis zur src-Ebene
         * @return Der src-Pfad wird als String zurückgegeben
         */
        String basePath = System.getProperty("user.dir");
        // Erhalten des Basispfads vom Projekt

        String srcPath = Paths.get(basePath, "src").toString();
        // Erhalten des Projektpfades bis zur src-Ebende

        return srcPath.replace("\\", "\\\\");
        // Zurückgeben des Pfades als String
    }
}
