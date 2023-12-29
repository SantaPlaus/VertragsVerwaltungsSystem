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

    public String getVertraege() {

        FileRepository fileRepository = new FileRepository();
        JSONObject jsonObject;

        String alleVertraege = "";

        Stream<Path> walk;
        try {
            walk = Files.walk(Paths.get("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".json")).collect(Collectors.toList());

        for (String fileName : result) {
            jsonObject = fileRepository.getJsonObject(fileName);

            alleVertraege += getVertragAlsString(jsonObject) + "\n\n";
        }
        return alleVertraege;
    }

    public String getVertragVSNR(JSONObject jsonObject) {

        FileRepository fileRepository = new FileRepository();
        JSONObject JsonObjectVomVertr;
        int vsnr;

        try {
            vsnr = (int) (long) jsonObject.get("vsnr");
        } catch (NullPointerException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return "Der Pfad kann nicht gelesen werden.";
        } catch (ClassCastException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return "Der Pfad kann nicht gelesen werden.";
        }

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        try {
            JsonObjectVomVertr = fileRepository.getJsonObject(path);
        } catch (NullPointerException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return "Der Pfad kann nicht gelesen werden.";
        }

        return getVertragAlsString(JsonObjectVomVertr);
    }

    public String leererVertrag(){

        return leererVertragString();
    }

    public String postNeu(JSONObject jsonObject, int generierteVsnr) {

        ValidierungsService validierungsService = new ValidierungsService();

        File tempFile;
        do {
            generierteVsnr++;
            tempFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + generierteVsnr + ".json");
        } while (tempFile.isFile());

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + generierteVsnr + ".json";

        boolean ueberschreiben = validierungsService.isNeuVertragValid(jsonObject);

        if (ueberschreiben) {
            return datenUeberschreiben(jsonObject, generierteVsnr, path);
        } else {
            return "Daten konnten nicht überschrieben werden, da min. ein Eintrag nicht validiert werden konnte.";
        }
    }

    public String postAenderung(JSONObject jsonObject) {

        ValidierungsService validierungsService = new ValidierungsService();

        int vsnr = (int) (long)jsonObject.get("vsnr");

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json";

        if (validierungsService.isAenderungVertragValid(jsonObject)) {
            return datenUeberschreiben(jsonObject, vsnr, path);
        } else {
            return "Daten konnten nicht überschrieben werden, da min. ein Eintrag nicht validiert werden konnte.";
        }
    }

    public String deleteVertraegeVSNR(JSONObject jsonObject) {

        FileRepository fileRepository = new FileRepository();

        if (fileRepository.deleteFile(jsonObject)) {
            return "Datei erfolgreich entfernt.";
        } else {
            return "Datei konnte nicht entfernt werden.";
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

    private String datenUeberschreiben(JSONObject jsonObject, int vsnr, String path)  {

        JSONObject jsonObjectNew = new JSONObject();
        PreisBerechnungsService preis = new PreisBerechnungsService();
        FileRepository fileRepository = new FileRepository();

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

        boolean isWritten = fileRepository.writeFile(path, jsonObjectNew);

        if (isWritten) {
            return getVertragAlsString(jsonObjectNew);
        } else {
            return "Vertrag kann nicht ausgegeben werden.";
        }
    }
}
