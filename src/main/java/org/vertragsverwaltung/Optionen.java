package org.vertragsverwaltung;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vertragsverwaltung.Services.Services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Optionen {

    Scanner inputScanner = new Scanner(System.in);
    String path;
    int generierteVsnr = 100000;
    int vsnr;

    public void optionsAuswahl() {

        System.out.println("Welchen Dateipfad soll ich untersuchen? ");

        path = inputScanner.next();

        Services services = new Services();
        PreisBerechnung preisBerechnung = new PreisBerechnung();

        JSONParser jsonParser = new JSONParser();
        FileReader reader;
        JSONObject jsonObject;

        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject = (JSONObject) jsonParser.parse((reader));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        String methode = (String) jsonObject.get("methode");
        String aktion = (String) jsonObject.get("aktion");

        if (methode.equals("GET") && aktion.equals("/vertraege")) {
            System.out.println(services.getVertraege());

        } else if (methode.equals("GET") && aktion.equals("/vertraege/vsnr")) {
            System.out.println(services.getVertragVSNR(jsonObject));

        } else if (methode.equals("POST") && aktion.equals("/preis")) {
            double preis = preisBerechnung.postPreis(jsonObject);
            services.preisUeberschreiben(jsonObject, preis);

        } else if (methode.equals("POST") && aktion.equals("/anlegen")) {
            services.postAnlegen(path);

        } else if (methode.equals("POST") && aktion.equals("/neu")) {
            services.postNeu(jsonObject, generierteVsnr);

        } else if (methode.equals("POST") && aktion.equals("/aenderung")) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            services.postAenderung(jsonObject);

            double preis = preisBerechnung.postPreis(jsonObject);
            services.preisUeberschreiben(jsonObject, preis);

        } else if (methode.equals("DELETE") && aktion.equals("/vertraege/Vsnr")) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            services.deleteVertraegeVSNR(jsonObject);
        } else {
            System.out.println("FEHLER");
        }
    }
}
