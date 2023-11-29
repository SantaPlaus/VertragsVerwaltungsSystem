package org.vertragsverwaltung.Services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vertragsverwaltung.PreisBerechnung;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Services {

    public String getVertraege(){
        System.out.println("/vertraege");

        JSONParser jsonParser = new JSONParser();
        FileReader reader;
        JSONObject jsonObject;
        String path;

        String alleVertraege = "";

        File folder = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege");
        String[] listOfFiles = (String[])(Object) folder.listFiles((dir, name) -> name
                .endsWith(".json"));

        for (String fileName: listOfFiles) {
            path = fileName;
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

            alleVertraege += "Versicherungsnummer: " + jsonObject.get("vsnr") +
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
                    "\nGeburtsdatum: " + jsonObject.get("geburtsdatum") + "\n\n";
        }
        return alleVertraege;
    }
    public String getVertragVSNR(JSONObject jsonObject){
        System.out.println("/vertraege vsnr");

        JSONParser jsonParser = new JSONParser();
        FileReader reader;
        JSONObject JsonObjectVomVertr;

        int vsnr = (int)(long) jsonObject.get("vsnr");
        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            JsonObjectVomVertr = (JSONObject) jsonParser.parse((reader));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return getVertragAlsString(JsonObjectVomVertr);
    }

    public void postAnlegen(String path){
        System.out.println("POST /anlegen");

        long generierterName;
        File tempFile;

        do {
            generierterName = System.currentTimeMillis() / 10000;
            path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + generierterName + "leer.json";
            tempFile = new File(path);
        } while (tempFile.isFile());

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

    public void postNeu(JSONObject jsonObject, int generierteVsnr){
        System.out.println("POST /neu");

        Pruefen pruefen = new Pruefen();

        File tempFile;
        do {
            generierteVsnr++;
            System.out.println(generierteVsnr);
            tempFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + generierteVsnr + ".json");
        } while (tempFile.isFile());

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + generierteVsnr + ".json";

        boolean ueberschreiben = pruefen.nachValiditaetPruefen(jsonObject);

        if (ueberschreiben) {
            datenUeberschreiben(jsonObject, generierteVsnr, path);
        } else {
            System.out.println("Daten konnten nicht überschrieben werden, da min. ein Eintrag nicht validiert werden konnte.");
        }



    }

    public void postAenderung(JSONObject jsonObject){
        System.out.println("POST /aenderung");

        int vsnr = (int) (long) jsonObject.get("vsnr");

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        datenUeberschreiben(jsonObject, vsnr, path);
    }

    public void deleteVertraegeVSNR(JSONObject jsonObject){
        System.out.println("DELETE");

        int vsnr = (int) (long) jsonObject.get("vsnr");

        File delFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json");

        if (delFile.delete()) {
            System.out.println("File deleted successfully");
        }
        else {
            System.out.println("Failed to delete the file");
        }
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
        jsonObject.put("fahrzeug_hoechstgeschwindigkeit", 0);
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
    private void datenUeberschreiben(JSONObject jsonObject, int vsnr, String path) {
        JSONObject jsonObjectNew = new JSONObject();
        PreisBerechnung preis = new PreisBerechnung();
        {
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

    public void preisUeberschreiben(JSONObject jsonObject, double preis) {
        int vsnr = (int) (long) jsonObject.get("vsnr");

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        JSONObject jsonObjectNew = new JSONObject();
        {
            jsonObjectNew.put("vsnr", vsnr);
            jsonObjectNew.put("preis", preis);
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