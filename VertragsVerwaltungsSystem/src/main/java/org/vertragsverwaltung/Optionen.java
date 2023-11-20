package org.vertragsverwaltung;
import org.apache.commons.io.FilenameUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class Optionen {

    Scanner inputScanner = new Scanner(System.in);
    String path;

    public void optionsAuswahl() throws IOException, ParseException {

        System.out.println("Welchen Dateipfad soll ich untersuchen? ");
        path = inputScanner.next();
        File file = new File(path);


        // vsnr in datei drin, Name von Inputdatei sollte egal sein


        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(path);
        JSONObject jsonObject = (JSONObject) jsonParser.parse((reader));

        String methode = (String) jsonObject.get("methode");
        String aktion = (String) jsonObject.get("aktion");

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
            reader.close();
            System.out.println("POST /anlegen");

        } else if (methode.equals("POST") && aktion.equals("/neu")) {
            reader.close();
            System.out.println("POST /neu");

        } else if (methode.equals("POST") && aktion.equals("/aenderung")) {
            reader.close();
            System.out.println("POST /aenderung");

        } else if (methode.equals("DELETE") && aktion.equals("/vertraege/" + vsnr)) {
            reader.close();
            System.out.println("DELETE");
            deleteVertraegeVSNR(file);
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
            alleVertraege.add("\n\nPreis: " + jsonObject.get("preis") +
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

        String einVertragMitVsnr = n();

        einVertragMitVsnr.add("Preis: " + jsonObject.get("preis") + "\nVersicherungsbeginn: " +  jsonObject.get("versicherungsbeginn"));
        einVertragMitVsnr.add("\nVersicherungsbeginn: " +  jsonObject.get("versicherungsbeginn"));
        einVertragMitVsnr.add("\nAnragsdatum: " + jsonObject.get("antragsdatum"));
        einVertragMitVsnr.add("\nAmtliches Kennzeichen: " + jsonObject.get("amtliches_kennzeichen"));
        einVertragMitVsnr.add("\nFahrzeughersteller: " + jsonObject.get("fahrzeug_hersteller"));
        einVertragMitVsnr.add("\nFahrzeugtyp: " + jsonObject.get("fahrzeug_typ"));
        einVertragMitVsnr.add("\nFahrzeug Höchstgeschwindigkeit: " + jsonObject.get("fahrzeug_hoechstgeschwindigkeit"));
        einVertragMitVsnr.add("\nWagniskennziffer: " + jsonObject.get("wagniskennziffer"));
        einVertragMitVsnr.add("\nNachname: " + jsonObject.get("nachname"));
        einVertragMitVsnr.add("\nVorname: " + jsonObject.get("vorname"));
        einVertragMitVsnr.add("\nAddresse: " + jsonObject.get("addresse"));
        einVertragMitVsnr.add("\nGeburtsdatum: " + jsonObject.get("geburtsdatum"));

        return einVertragMitVsnr;
    }
    public void postPreis(FileReader reader, JSONObject jsonObject){
        String geburtsDatum = (String) jsonObject.get("geburtsdatum");
        int vMax = (int) (long) jsonObject.get("fahrzeug_hoechstgeschwindigkeit");
        System.out.println(geburtsDatum);
        System.out.println(vMax);
    }

    public void postAnlegen(){
        int vsnr = 100002;
        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        JSONObject json = new JSONObject();

        try{
            json.put("methode", "POST");
            json.put("aktion", "/anlegen");
            json.put("preis", null);
            json.put("versicherungsbeginn", null);
            json.put("antragsdatum", null);
            json.put("amtliches_kennzeichen", null);
            json.put("fahrzeug_hersteller", null);
            json.put("fahrzeug_typ", null);
            json.put("fahrzeug_hoechstgeschwindigkeit", null);
            json.put("wagniskennziffer", null);
            json.put("nachname", null);
            json.put("vorname", null);
            json.put("addresse", null);
            json.put("geburtsdatum", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(json.toJSONString());
            fileWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(json);


        /*try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    public void postNeu(){

    }
    public void postAenderung(){

    }
    public void deleteVertraegeVSNR(File file){
        // NICHT file.delete(), sondern nach vsnr suchen
        if (file.delete()) {
            System.out.println("File deleted successfully");
        }
        else {
            System.out.println("Failed to delete the file");
        }
    }
}
