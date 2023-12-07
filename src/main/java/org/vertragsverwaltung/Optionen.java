package org.vertragsverwaltung;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vertragsverwaltung.Services.PreisBerechnung;
import org.vertragsverwaltung.Services.Services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Optionen {
    Scanner inputScanner = new Scanner(System.in);
    String path;
    int generierteVsnr = 100000;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return;
        }

        String methode = (String) jsonObject.get("methode");
        String aktion = (String) jsonObject.get("aktion");

        if (methode.equals("GET") && aktion.equals("/vertraege")) {
            try {
                System.out.println(services.getVertraege());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        } else if (methode.equals("GET") && aktion.equals("/vertraege/vsnr")) {
            try {
                System.out.println(services.getVertragVSNR(jsonObject));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if (methode.equals("POST") && aktion.equals("/preis")) {
            try {
                System.out.println(preisBerechnung.postPreis(jsonObject) + "€");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if (methode.equals("POST") && aktion.equals("/anlegen")) {

            System.out.println(services.leererVertrag());

        } else if (methode.equals("POST") && aktion.equals("/neu")) {
            try {
                services.postNeu(jsonObject, generierteVsnr);
            } catch (NumberFormatException e) {
                System.out.println("Datum im falschen Format angegeben.");
                throw new RuntimeException(e);
            } catch (java.text.ParseException e) {
                System.out.println("Datum im falschen Format angegeben.");
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if (methode.equals("POST") && aktion.equals("/aenderung")) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                services.postAenderung(jsonObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if (methode.equals("DELETE") && aktion.equals("/vertraege/vsnr")) {
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
