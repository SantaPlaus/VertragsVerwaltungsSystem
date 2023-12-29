package org.vertragsverwaltung.Controller;

import org.json.simple.JSONObject;
import org.vertragsverwaltung.Data.FileRepository;
import org.vertragsverwaltung.Services.PreisBerechnungsService;
import org.vertragsverwaltung.Services.ValidierungsService;
import org.vertragsverwaltung.Services.VertragsService;

import java.util.Scanner;

public class VertragController {
    Scanner inputScanner = new Scanner(System.in);
    String path;
    int generierteVsnr = 100000;

    public void optionsAuswahl() {

        System.out.println("Welchen Dateipfad soll ich untersuchen? ");

        path = inputScanner.next();

        VertragsService vertragsService = new VertragsService();
        PreisBerechnungsService preisBerechnungsService = new PreisBerechnungsService();
        FileRepository fileRepository = new FileRepository();
        ValidierungsService validierungsService = new ValidierungsService();

        JSONObject jsonObject;
        String ausgabe = "Der Pfad kann nicht gelesen werden.";


        if (fileRepository.getJsonObject(path) != null) {
            jsonObject = fileRepository.getJsonObject(path);

            String methode = validierungsService.isMethodeValid(jsonObject);
            String aktion = validierungsService.isAktionValid(jsonObject);


            if (methode.equals("GET") && aktion.equals("/vertraege")) {
                ausgabe = vertragsService.getVertraege();

            } else if (methode.equals("GET") && aktion.equals("/vertraege/vsnr")) {
                ausgabe = vertragsService.getVertragVSNR(jsonObject);

            } else if (methode.equals("POST") && aktion.equals("/preis")) {
                ausgabe = "" + preisBerechnungsService.postPreis(jsonObject) + "€";

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
            }
        }
        System.out.println(ausgabe);
    }
}
