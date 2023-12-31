package org.vertragsverwaltung.Controller;

import org.json.simple.JSONObject;
import org.vertragsverwaltung.Data.FileRepository;
import org.vertragsverwaltung.Services.PreisBerechnungsService;
import org.vertragsverwaltung.Services.ValidierungsService;
import org.vertragsverwaltung.Services.VertragsService;

import java.util.Scanner;

public class VertragController {
    /**
     * Controller-Klasse für die Vertragsverwaltung
     */

    Scanner inputScanner = new Scanner(System.in);
    // Erstellen des Scanners für die Benutzereingabe

    String path;
    // Deklaration des Pfads für Vertragsdateien

    int generierteVsnr = 100000;
    // Initialisierung der generierten Vertragsnummer

    public void optionsAuswahl() {
        /**
         * Die Methode, die anhand von User-Input entscheidet, welche Aktionen ausgeführt werden
         */

        VertragsService vertragsService = new VertragsService();
        PreisBerechnungsService preisBerechnungsService = new PreisBerechnungsService();
        FileRepository fileRepository = new FileRepository();
        ValidierungsService validierungsService = new ValidierungsService();
        //Initialisierung aller Service-, sowie Data-Objekte

        JSONObject jsonObject;
        // Deklaration vom JSONObject

        String ausgabe = "Der Pfad kann nicht gelesen werden.";
        // Initialisierung des Ausgabestrings mit einer Vorgefertigten Fehlermeldung

        System.out.println("Welchen Dateipfad soll ich untersuchen? ");

        path = inputScanner.next();
        // Frage nach User-Input

        if (fileRepository.getJsonObject(path) != null) {
            // Falls das JSONObject des Dateipfades nicht "null" ist, wird die Id-Abfrage gestartet

            jsonObject = fileRepository.getJsonObject(path);
            // Das JSONObject des Dateipfades wird der variable "jsonObject" zugewiesen

            String methode = validierungsService.isMethodeValid(jsonObject);
            String aktion = validierungsService.isAktionValid(jsonObject);
            //Überprüft die Validität der values von den keys "methode" und "aktion" des JSONObjects

            // Basierend auf den entsprechenden Werte der Strings werden folgende Möglichkeiten durchgegangen und bei Erfolg ausgeführt.
            // Die jeweiligen Rückgabewerte werden dem String "ausgabe" zugewiesen
            if (methode.equals("GET") && aktion.equals("/vertraege")) {
                ausgabe = vertragsService.getVertraege();

            } else if (methode.equals("GET") && aktion.equals("/vertraege/vsnr")) {
                ausgabe = vertragsService.getVertragVSNR(jsonObject);

            } else if (methode.equals("POST") && aktion.equals("/preis")) {
                ausgabe = "" + preisBerechnungsService.postPreis(jsonObject);

            } else if (methode.equals("POST") && aktion.equals("/anlegen")) {
                ausgabe = vertragsService.leererVertrag();

            } else if (methode.equals("POST") && aktion.equals("/neu")) {
                ausgabe = vertragsService.postNeu(jsonObject, generierteVsnr);

            } else if (methode.equals("POST") && aktion.equals("/aenderung")) {
                ausgabe = vertragsService.postAenderung(jsonObject);

            } else if (methode.equals("DELETE") && aktion.equals("/vertraege/vsnr")) {
                ausgabe = vertragsService.deleteVertraegeVSNR(jsonObject);

            } else {
                ausgabe = "Methode und/oder Aktion konnten nicht zugeordnet werden.";
                // Bei Misserfolg wird diese Fehlermeldung dem String "ausgabe" zugewiesen
            }
        }
        System.out.println(ausgabe);
        // Der String "ausgabe" wird ausgegeben
    }
}
