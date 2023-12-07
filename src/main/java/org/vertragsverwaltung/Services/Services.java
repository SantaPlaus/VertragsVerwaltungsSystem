package org.vertragsverwaltung.Services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Services {

    public String getVertraege() throws IOException, ParseException {

        JSONParser jsonParser = new JSONParser();
        FileReader reader;
        JSONObject jsonObject;
        String path;

        String alleVertraege = "";

        Stream<Path> walk = Files.walk(Paths.get("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege"));

        List<String> result = walk.map(x -> x.toString())
                .filter(f -> f.endsWith(".json")).collect(Collectors.toList());

        for (String fileName : result) {
            reader = new FileReader(fileName);
            jsonObject = (JSONObject) jsonParser.parse((reader));

            alleVertraege += getVertragAlsString(jsonObject) + "\n\n";
        }
        return alleVertraege;
    }

    public String getVertragVSNR(JSONObject jsonObject) throws Exception {

        JSONParser jsonParser = new JSONParser();
        FileReader reader;
        JSONObject JsonObjectVomVertr;

        int vsnr = (int) (long) jsonObject.get("vsnr");
        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        reader = new FileReader(path);

        JsonObjectVomVertr = (JSONObject) jsonParser.parse((reader));

        return getVertragAlsString(JsonObjectVomVertr);
    }

    public String leererVertrag(){

        return leererVertragString();
    }

    public void postNeu(JSONObject jsonObject, int generierteVsnr) throws Exception {

        Validieren validieren = new Validieren();

        File tempFile;
        do {
            generierteVsnr++;
            tempFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + generierteVsnr + ".json");
        } while (tempFile.isFile());

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + generierteVsnr + ".json";

        boolean ueberschreiben = validieren.isValid(jsonObject);

        if (ueberschreiben) {
            datenUeberschreiben(jsonObject, generierteVsnr, path);
        } else {
            System.out.println("Daten konnten nicht überschrieben werden, da min. ein Eintrag nicht validiert werden konnte.");
        }
    }

    public void postAenderung(JSONObject jsonObject) throws Exception {

        int vsnr = (int) (long)jsonObject.get("vsnr");

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        datenUeberschreiben(jsonObject, vsnr, path);
    }

    public void deleteVertraegeVSNR(JSONObject jsonObject) {

        int vsnr = (int) (long)jsonObject.get("vsnr");

        File delFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json");

        if (delFile.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }
    }

    private String getVertragAlsString(JSONObject jsonObject) {

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
        return einVertragAlsString;
    }

    private String leererVertragString() {

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
        return leererVertragString;
    }

    private Object antragsDatum() {

        LocalDate datum = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = datum.format(format);
        return formattedDate;
    }

    private void datenUeberschreiben(JSONObject jsonObject, int vsnr, String path) throws Exception {

        JSONObject jsonObjectNew = new JSONObject();
        PreisBerechnung preis = new PreisBerechnung();

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

        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObjectNew.toJSONString());
        fileWriter.close();

        System.out.println(getVertragAlsString(jsonObjectNew));
    }

    public String preisUeberschreiben(JSONObject jsonObject, double preis) throws IOException {

        int vsnr = (int) (long) jsonObject.get("vsnr");

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        JSONObject jsonObjectNew = new JSONObject();

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

        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObjectNew.toJSONString());
        fileWriter.close();

        return getVertragAlsString(jsonObjectNew);
    }
}