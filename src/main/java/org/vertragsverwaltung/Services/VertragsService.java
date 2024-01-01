package org.vertragsverwaltung.Services;


import org.json.simple.JSONObject;
import org.vertragsverwaltung.Data.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VertragsService {
    // Klasse mit Methoden, die die Befehle der JSON-Dateien ausführen

    public String getVertraege() {
        /**
         * Gibt alle vorhandenen Verträge als String zurück
         * @return String mit allen gespeicherten Verträgen
         */

        FileRepository fileRepository = new FileRepository();
        // Initialisieren vom FileRepository

        JSONObject jsonObject;
        // Deklaration vom JSONObject

        String alleVertraege = "";
        // Initialisieren vom String, dem alle Vertraege hinzigefügt werden

        String path = fileRepository.srcPath() + "\\\\main\\\\resources\\\\vertraege";
        // Das Programm sucht den Pfad, in dem die src-Datei liegt und baut sich den path-String zusammen

        Stream<Path> walk;
        // Initialisierung eines Stream<Path>, um die Dateipfade der Verträge strukturiert abzulaufen

        try {
            walk = Files.walk(Paths.get(path));
            // Sammelt alle Dateipfade der Verträge

        } catch (IOException e) {
            System.out.println("Es sind noch keine Verträge vorhanden.");
            return alleVertraege;
            // Behandelt eine IOException, wenn es keine Verträge gibt, gibt eine Fehlermeldung aus, sowie alleVertraege zurück
        }

        List<String> result = walk
                .map(x -> x.toString())
                .filter(f -> f.endsWith(".json"))
                .collect(Collectors.toList());
        // Erstellt eine Liste von allen Dateipfaden, die vorher in "walk" gespeichert wurden

        for (String fileName : result) {
            // Die For-Schleife durchläuft jeden Dateipfad

            jsonObject = fileRepository.getJsonObject(fileName);
            // Erstellt ein JSONObject für den jeweiligen Dateinamen

            alleVertraege += getVertragAlsString(jsonObject) + "\n\n";
            // Fügt jedes JSONObject als String dem finalen String hinzu
        }
        return alleVertraege;
        // gibt den String alleVertraege zurück
    }

    public String getVertragVSNR(JSONObject jsonObject) {
        /**
         * Gibt einen bestimmten Vertrag anhand der vsnr als String zurück
         * @param jsonObject Das JSONObject, aus dem die vsnr genommen wird um den auszudruckenden Vertrag zu ehralten
         * @return Der Vertrag als String
         */

        FileRepository fileRepository = new FileRepository();
        // Initialisieren vom FileRepository

        JSONObject JsonObjectVomVertr;
        // Deklaration vom JSONObject

        int vsnr;
        // Deklaration von der Integer-Variable "vsnr"

        try {
            vsnr = (int) (long) jsonObject.get("vsnr");
            // Zuweisung des values vom key "vsnr" in die Variable

        } catch (NullPointerException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return "Der Pfad kann nicht gelesen werden.";
            // Behandeln einer NullPointerException, wenn die vsnr nicht existiert und Ausgeben sowie Rückgabe von Fehlermeldungen

        } catch (ClassCastException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return "Der Pfad kann nicht gelesen werden.";
            // Behandeln einer ClassCastException, wenn der Typ der vsnr nicht korrekt ist und Ausgeben sowie Rückgabe von Fehlermeldungen
        }

        String path = fileRepository.srcPath() + "\\\\main\\\\resources\\\\vertraege\\\\" + vsnr + ".json";
        // Zusammensetzen des Dateipfades, wessen Daten ausgegeben werden sollen

        try {
            JsonObjectVomVertr = fileRepository.getJsonObject(path);
            // Erstellt ein JSONObject mithilfe des erstellten Dateipfades

        } catch (NullPointerException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return "Der Pfad kann nicht gelesen werden.";
            // Behandelt eine NullPointerException, wenn keine vsnr existiert und Ausgeben sowie Rückgabe von Fehlermeldungen
        }

        if (JsonObjectVomVertr == null) {
            return "Der Pfad kann nicht gelesen werden.";
            // Prüfen, ob das JSONObject "null" als Wert besitzt. Falls ja, wird eine Fehlermeldung zurückgegeben
        }

        return getVertragAlsString(JsonObjectVomVertr);
        // Rückgabe des Vertrages als String
    }

    public String leererVertrag(){
        /**
         * Diese Methode gibt einen leeren Vertrag zurück, um zu zeigen welche Felder in einem Neuvertrag ausgefült werden müssen
         */

        return leererVertragString();
        // Rückgabe des leeren Vertrages
    }

    public String postNeu(JSONObject jsonObject, int generierteVsnr) {
        /**
         * Erstellung eines neuen Vertrages besierend auf den Informationen im JSONObject
         * @param jsonObject Das JSONObject mit den nötigen Informationen
         * @param generierteVsnr Die generierte Versicherungsnummer
         * @return Vertrag als String oder eine Fehlermeldung
         */

        ValidierungsService validierungsService = new ValidierungsService();
        // Initialisierung vom ValidierungsService

        FileRepository fileRepository = new FileRepository();
        // Initialisierung vom FileRepository

        File tempFile;
        // Deklaration der File tempFile

        do {
            generierteVsnr++;
            // Immer +1 auf die generierte Vsnr

            tempFile = new File(fileRepository.srcPath() + "\\\\main\\\\resources\\\\vertraege\\\\" + generierteVsnr + ".json");
            // Erstellen einer neuen Datei mithilfe der generierten Vsnr

        } while (tempFile.isFile());
        // solange die Datei existiert wird diese Schleife wiederholt, um eine neue, einzigartie Vsnr und Datei zu generieren

        String path = fileRepository.srcPath() + "\\\\main\\\\resources\\\\vertraege\\\\" + generierteVsnr + ".json";
        // Gibt es die Datei noch nicht, so wird der neue Dateipfad gespeichert

        boolean isValid = validierungsService.isNeuVertragValid(jsonObject);
        // Validierung der Daten des JSONObjects

        if (isValid) {
            return datenUeberschreiben(jsonObject, generierteVsnr, path);
            // Wenn die Daten valide sind, werden diese in eine Datei geschrieben und als String zurückgegeben

        } else {
            return "Daten konnten nicht überschrieben werden, da min. ein Eintrag nicht validiert werden konnte.";
            // Wenn die Daten nicht valide sind, so wird eine Fehlermeldung zurückgegeben
        }
    }

    public String postAenderung(JSONObject jsonObject) {
        /**
         * Eine Änderung eines Vertrages anhand der gegebenen Daten im JSONObject
         * @param jsonObject die aktualisierten Vertragsdaten
         * @return Vertrag als String oder eine Fehlermeldung
         */

        ValidierungsService validierungsService = new ValidierungsService();
        // Initialisierung des ValidierungsServices

        FileRepository fileRepository = new FileRepository();
        // Initialisierung vom FileRepository

        int vsnr = (int) (long)jsonObject.get("vsnr");
        // Zuweisung des values vom key "vsnr" in die Integer-Variable vsnr

        String path = fileRepository.srcPath() + "\\\\main\\\\resources\\\\vertraege\\\\" + vsnr + ".json";
        // Die Konstruktion des Dateipfades, mithilfe der Vsnr, der zu ändernden Datei

        if (validierungsService.isAenderungVertragValid(jsonObject)) {
            return datenUeberschreiben(jsonObject, vsnr, path);
            // Falls die Validierung der zu ändernen Daten erfolgreich war, so werden die Daten überschrieben und der Vertrag wird als String zurückgegeben

        } else {
            return "Daten konnten nicht überschrieben werden, da min. ein Eintrag nicht validiert werden konnte.";
            // Bei Misserfolg bei der Validierung wird eine Fehlermeldung zurückgegeben
        }
    }

    public String deleteVertraegeVSNR(JSONObject jsonObject) {
        /**
         * Deletion eines Vertrages anhand der Vsnr ohne weitere Prüfung
         * @param jsonObject Das JSONObject, in welchem die zu löschende Vsnr steht
         * @return Eine Erfolgsnachricht oder Fehlermeldung
         */

        FileRepository fileRepository = new FileRepository();
        // Initialisierung des FileRepositories

        if (fileRepository.deleteFile(jsonObject)) {
            return "Datei erfolgreich entfernt.";
            // Wenn die Datei gelöscht wurde und die Kondition somit true ist, dann wird eine Erfolgsnachricht zurückgegeben

        } else {
            return "Datei konnte nicht entfernt werden.";
            // Konnte die Datei nicht gelöscht werden, so wird eine Fehlermeldung zurükgegeben
        }
    }

    private String getVertragAlsString(JSONObject jsonObject) {
        /**
         * Eine Methode, die dafür genutzt wird, einen Vertrag als String zurückzugeben
         * @param jsonObject Das JSONObject, wessen Daten als String ausgegeben werden sollen
         * @return Ein String mit den Vertragsdaten
         */

        String einVertragAlsString = "Versicherungsnummer: " + jsonObject.get("vsnr")
                + "\nVorname: " + jsonObject.get("vorname")
                + "\nNachname: " + jsonObject.get("nachname")
                + "\nGeburtsdatum: " + jsonObject.get("geburtsdatum")
                + "\nAddresse: " + jsonObject.get("addresse")
                + "\nFahrzeughersteller: " + jsonObject.get("fahrzeug_hersteller")
                + "\nFahrzeugtyp: " + jsonObject.get("fahrzeug_typ")
                + "\nFahrzeug Höchstgeschwindigkeit: " + jsonObject.get("fahrzeug_hoechstgeschwindigkeit")
                + "\nAmtliches Kennzeichen: " + jsonObject.get("amtliches_kennzeichen")
                + "\nAnragsdatum: " + jsonObject.get("antragsdatum")
                + "\nVersicherungsbeginn: " + jsonObject.get("versicherungsbeginn")
                + "\nWagniskennziffer: " + jsonObject.get("wagniskennziffer")
                + "\nPreis: " + jsonObject.get("preis") + "€";
        // Initialisieren eines Strings mit allen Vertragsdaten, die aus dem JSONObject stammen

        return einVertragAlsString;
        // Rückgabe des erstellten Strings
    }

    private String leererVertragString() {
        /**
         * Eine Methode, die dafür genutzt wird, einen leeren Vertrag als String zurückzugeben
         * @return Ein String mit dem leeren Vertrag
         */
        String leererVertragString = "Vorname: " + null
                + "\nNachname: " + null
                + "\nGeburtsdatum: " + null
                + "\nAddresse: " + null
                + "\nFahrzeug_hersteller: " + null
                + "\nFahrzeug_typ: " + null
                + "\nFahrzeug_hoechstgeschwindigkeit: " + 0
                + "\nAmtliches_kennzeichen: " + null
                + "\nAntragsdatum: " + null
                + "\nVersicherungsbeginn: " + null;
        // Initialisieren eines Strings mit allen Daten, die der User in der postNeu-Datei eingeben muss

        return leererVertragString;
        // Rückgabe des leeren Strings
    }

    private Object antragsDatum() {
        /**
         * Eine Methode, die dafür genutzt wird, das Antragsdatum zu bestimmen
         * @return Ein Object mit dem jetzigen Datum
         */

        LocalDate datum = LocalDate.now();
        // Initialisierung einer LocalDate-Variable mit dem jetzigem Datum

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        // Initialisierung des DateTimeFormatters mit dem dd.MM.yyyy-Format

        String formattedDate = datum.format(format);
        // Formatierung der "date" Variable mithilfe des DateTimeFormatters in das dd.MM.yyyy Format

        return formattedDate;
        // Rückgabe des Datums
    }

    private String datenUeberschreiben(JSONObject jsonObject, int vsnr, String path)  {
        /**
         * Eine Methode, welche für das (Über-)Schreiben von Daten einer JSON-Datei zuständig ist
         * @param jsonObject Das JSONObject, welches die Daten zum (Über-)Schreiben enthält
         * @param vsnr Die Vsnr des Vertrages
         * @param path Der Dateipfad für den Vertrag
         * @return Der Vertrag, der in der Methode entstanden ist oder eine Fehlermeldung
         */

        JSONObject jsonObjectNew = new JSONObject();
        // Initialisierung eines JSONObjects

        PreisBerechnungsService preis = new PreisBerechnungsService();
        // Initialisierung eines PreisBerechnungsServices

        FileRepository fileRepository = new FileRepository();
        // Initialisierung eines FileRepositories

        jsonObjectNew.put("vsnr", vsnr);
        jsonObjectNew.put("preis", preis.postPreis(jsonObject));
        jsonObjectNew.put("versicherungsbeginn", jsonObject.get("versicherungsbeginn"));
        jsonObjectNew.put("antragsdatum", "" + antragsDatum());
        jsonObjectNew.put("amtliches_kennzeichen", jsonObject.get("amtliches_kennzeichen"));
        jsonObjectNew.put("fahrzeug_hersteller", jsonObject.get("fahrzeug_hersteller"));
        jsonObjectNew.put("fahrzeug_typ", jsonObject.get("fahrzeug_typ"));
        jsonObjectNew.put("fahrzeug_hoechstgeschwindigkeit", jsonObject.get("fahrzeug_hoechstgeschwindigkeit"));
        jsonObjectNew.put("wagniskennziffer", jsonObject.get("wagniskennziffer"));
        jsonObjectNew.put("nachname", jsonObject.get("nachname"));
        jsonObjectNew.put("vorname", jsonObject.get("vorname"));
        jsonObjectNew.put("addresse", jsonObject.get("addresse"));
        jsonObjectNew.put("geburtsdatum", jsonObject.get("geburtsdatum"));
        // Hinzufügen der Vertragsdaten in das neue JSONObject

        boolean isWritten = fileRepository.writeFile(path, jsonObjectNew);
        // Zuweisung eines booleans zu der boolean-Variable isWritten. Wenn die Datei geschrieben werden konnte ist es true, sonst false

        if (isWritten) {
            return getVertragAlsString(jsonObjectNew);
            // Konnte die Datei geschrieben werden wird der Vertrag als String zurückgegeben

        } else {
            return "Vertrag kann nicht ausgegeben werden.";
            // Wenn das Datei nicht geschrieben werden konnte, wird eine Fehlermeldung ausgegeben
        }
    }
}
