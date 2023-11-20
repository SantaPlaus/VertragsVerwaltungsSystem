package org.vertragsverwaltung;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {


    public static void main(String[] args) {

        Optionen opt = new Optionen();


        //opt.postAnlegen();
        // File[] alleVertraege = opt.getVertraege();
        // System.out.println(alleVertraege);
        // System.out.println(alleVertraege.toString());

        /**
         *  Optionen:
         *      - alle Verträge anzeigen                GET /vertraege   (alle Verträge anzeigen oder alle vsnr.json anzeigen???)
         *
         *      - einen bestimmten Vertrag anzeigen     GET /vertraege/{vsnr}
         *
         *      - neuen/geänderten Vertrag empfangen
         *        und Preis zurückgeben                 POST /preis
         *
         *      - leeren Vertrag anlegen                POST /anlegen
         *
         *      - neuen Vertrag empfangen, anlegen (prüfen)
         *        und neue VSNR zurückgeben             POST /neu
         *
         *      - geänderten Vertag empfangen, alten
         *        überschreiben und mit neuem Preis
         *        zurückgeben                           POST /aenderung
         *
         *      - löscht Vertrag ohne Prüfung           DELETE /vertraege/{vsnr}
         *
         *      {„Methode“: „GET“, „Aktion“: „/vertraege“}
         *
         *
         */

        try {
            opt.optionsAuswahl();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }


}

