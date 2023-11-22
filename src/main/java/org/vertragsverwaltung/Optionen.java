package org.vertragsverwaltung;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class Optionen {

    Scanner inputScanner = new Scanner(System.in);
    String path;
    int generierteVsnr = 100000;
    int vsnr;

    public void optionsAuswahl() throws IOException, ParseException {

        System.out.println("Welchen Dateipfad soll ich untersuchen? ");
        path = inputScanner.next();
        File file = new File(path);


        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(path);
        JSONObject jsonObject = (JSONObject) jsonParser.parse((reader));

        String methode = (String) jsonObject.get("methode");
        String aktion = (String) jsonObject.get("aktion");
        /*if (!jsonObject.get("vsnr").equals(null)){
            vsnr = (int) (long) jsonObject.get("vsnr");
        }*/

        if (methode.equals("GET") && aktion.equals("/vertraege")) {
            System.out.println("/vertraege");
            System.out.println(getVertraege(reader, jsonObject));
            reader.close();

        } else if (methode.equals("GET") && aktion.equals("/vertraege/" + vsnr)) {
            System.out.println("/vertraege vsnr");
            System.out.println(getVertragVSNR(reader, jsonObject));
            reader.close();

        } else if (methode.equals("POST") && aktion.equals("/preis")) {
            postPreis(reader, jsonObject);
            System.out.println("POST /preis");
            reader.close();

        } else if (methode.equals("POST") && aktion.equals("/anlegen")) {
            System.out.println("POST /anlegen");
            postAnlegen();

        } else if (methode.equals("POST") && aktion.equals("/neu")) {
            System.out.println("POST /neu");
            postNeu(jsonObject);

        } else if (methode.equals("POST") && aktion.equals("/aenderung")) {
            reader.close();
            System.out.println("POST /aenderung");
            postAenderung(jsonObject);
            // preisBerechnen();

        } else if (methode.equals("DELETE") && aktion.equals("/vertraege/" + vsnr)) {
            reader.close();
            System.out.println("DELETE");
            // Instanzen aufrufen
        } else {
            System.out.println("FEHLER");
        }
    }



    public ArrayList<String> getVertraege(FileReader reader, JSONObject jsonObject){
        File folder = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege");

        ArrayList<String> alleVertraege = new ArrayList<>();

        File[] listOfFiles = folder.listFiles((dir, name) -> name
                .toLowerCase()
                .endsWith(".json"));

        for (File file: listOfFiles) {
            alleVertraege.add("" +"\n\nVersicherungsnummer: " + jsonObject.get("vsnr") +
            "\nPreis: " + jsonObject.get("preis") +
            "\nVersicherungsbeginn: " +  jsonObject.get("versicherungsbeginn") +
            "\nAnragsdatum: " + jsonObject.get("antragsdatum") +
            "\nAmtliches Kennzeichen: " + jsonObject.get("amtliches_kennzeichen") +
            "\nFahrzeughersteller: " + jsonObject.get("fahrzeug_hersteller") +
            "\nFahrzeugtyp: " + jsonObject.get("fahrzeug_typ") +
            "\nFahrzeug Höchstgeschwindigkeit: " + jsonObject.get("fahrzeug_hoechstgeschwindigkeit") +
            "\nWagniskennziffer: " + jsonObject.get("wagniskennziffer") +
            "\nNachname: " + jsonObject.get("nachname") +
            "\nVorname: " + jsonObject.get("vorname") +
            "\nAddresse: " + jsonObject.get("addresse") +
            "\nGeburtsdatum: " + jsonObject.get("geburtsdatum"));
        }
        return alleVertraege;
    }
    public String getVertragVSNR(FileReader reader, JSONObject jsonObject){ //kein void als return-typ
        return getVertragAlsString(jsonObject);
    }


    public void postPreis(FileReader reader, JSONObject jsonObject){
        String geburtsDatum = (String) jsonObject.get("geburtsdatum");
        int vMax = (int) (long) jsonObject.get("fahrzeug_hoechstgeschwindigkeit");
        System.out.println(geburtsDatum);
        System.out.println(vMax);
    }

    public void postAnlegen(){
        long generierterName;
        File tempFile;

        do {
            generierterName = System.currentTimeMillis() / 10000;
            tempFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystemGit\\src\\main\\resources\\vertraege\\" + generierterName + "leer.json");
        } while (tempFile.isFile());

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystemGit\\src\\main\\resources\\vertraege\\" + generierterName + "leer.json";

        JSONObject jsonLeer = new JSONObject();

        try{
            leerenVertragErstellen(jsonLeer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(jsonLeer.toJSONString());
            fileWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(getVertragAlsString(jsonLeer));
    }

    public void postNeu(JSONObject jsonObject){
        File tempFile;
        do {
            generierteVsnr++;
            System.out.println(generierteVsnr);
            tempFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystemGit\\src\\main\\resources\\vertraege\\" + generierteVsnr + ".json");
        } while (tempFile.isFile());

        path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystemGit\\src\\main\\resources\\vertraege\\" + generierteVsnr + ".json";

        datenUeberschreiben(jsonObject, generierteVsnr);

    }

    public void postAenderung(JSONObject jsonObject){
        vsnr = (int)(long) jsonObject.get("vsnr");

        path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystemGit\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        datenUeberschreiben(jsonObject, vsnr);

        // preisBerechnen();
    }

    private String getVertragAlsString(JSONObject jsonObject) {
        String einVertragAlsString = "";

        einVertragAlsString += "Versicherungsnummer: " + jsonObject.get("vsnr")
                + "\nVorname: " + jsonObject.get("vorname")
                + "\nNachname: " + jsonObject.get("nachname")
                + "\nGeburtsdatum: " + jsonObject.get("geburtsdatum")
                + "\nAddresse: " + jsonObject.get("addresse")
                + "\nFahrzeughersteller: " + jsonObject.get("fahrzeug_hersteller")
                + "\nFahrzeugtyp: " + jsonObject.get("fahrzeug_typ")
                + "\nFahrzeug Höchstgeschwindigkeit: " + jsonObject.get("fahrzeug_hoechstgeschwindigkeit")
                + "\nAmtliches Kennzeichen: " + jsonObject.get("amtliches_kennzeichen")
                + "\nAnragsdatum: " + jsonObject.get("antragsdatum")
                + "\nVersicherungsbeginn: " +  jsonObject.get("versicherungsbeginn")
                + "\nWagniskennziffer: " + jsonObject.get("wagniskennziffer")
                + "\nPreis: " + jsonObject.get("preis");
        return einVertragAlsString;
    }
    private void leerenVertragErstellen(JSONObject jsonObject) {
        jsonObject.put("methode", "POST");
        jsonObject.put("aktion", "/anlegen");
        jsonObject.put("vsnr", null);
        jsonObject.put("preis", null);
        jsonObject.put("versicherungsbeginn", null);
        jsonObject.put("antragsdatum", null);
        jsonObject.put("amtliches_kennzeichen", null);
        jsonObject.put("fahrzeug_hersteller", null);
        jsonObject.put("fahrzeug_typ", null);
        jsonObject.put("fahrzeug_hoechstgeschwindigkeit", null);
        jsonObject.put("wagniskennziffer", null);
        jsonObject.put("nachname", null);
        jsonObject.put("vorname", null);
        jsonObject.put("addresse", null);
        jsonObject.put("geburtsdatum", null);
    }
    private Object antragsDatum() {
        LocalDate datum = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = datum.format(format);
        return formattedDate;
    }
    private void datenUeberschreiben(JSONObject jsonObject, int vsnr) {
        JSONObject jsonObjectNew = new JSONObject();
        {
            jsonObjectNew.put("vsnr", vsnr);
            jsonObjectNew.put("preis", null);
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
        }
        try{
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(jsonObjectNew.toJSONString());
            fileWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(getVertragAlsString(jsonObjectNew));
    }
}
