package org.vertragsverwaltung.Services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

public class Validieren {
    public boolean isValid(JSONObject jsonObject) throws Exception {
        String vorname = (String) jsonObject.get("vorname");
        String nachname = (String) jsonObject.get("nachname");
        String addresse = (String) jsonObject.get("addresse");
        String fahrzeugTyp = (String) jsonObject.get("fahrzeug_typ");
        int fahrzeugHoechstgeschwindigkeit = (int) (long) jsonObject.get("fahrzeug_hoechstgeschwindigkeit");
        String kennzeichen = (String) jsonObject.get("amtliches_kennzeichen");
        boolean richtigesKennzeichenFormat = !kennzeichen.matches("[A-Z]{1,3}-[A-Z]{1,2}-[0-9]{1,4}");

        String datum = (String) jsonObject.get("geburtsdatum");
        Date geburtsdatum = new SimpleDateFormat("dd.MM.yyyy").parse(datum);
        //Date date2 = new SimpleDateFormat("dd.MM.yyyy").parse((String)(Object)versicherungsBeginn);

        LocalDate versicherungsBeginn = getVersicherungsBeginn(jsonObject);

        if (vorname.equals(null)) {
            return false;
        } else if (nachname.equals(null)) {
            return false;
        } else if (addresse.equals(null)) {
            return false;
        } else if (geburtsdatum.getYear() > (Year.now().getValue()) - 18) {
            return false;
        } else if (!checkIfFahrzeugHerstellerExists(jsonObject)) {
            return false;
        } else if (fahrzeugTyp.equals(null)) {
            return false;
        } else if (fahrzeugHoechstgeschwindigkeit == 0 || fahrzeugHoechstgeschwindigkeit > 250) {
            return false;
        } else if (richtigesKennzeichenFormat) {
            return false;
        } else if (versicherungsBeginn.isBefore(LocalDate.now())) {
            return false;
        } else {
            return true;
        }
    }

    private static LocalDate getVersicherungsBeginn(JSONObject jsonObject) {
        String versicherungsBeginnString = (String) jsonObject.get("versicherungsbeginn");
        int jahrDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(6));
        int monatDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(3, 5));
        int tagDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(0, 2));

        return LocalDate.of(jahrDesVersicherungsBeginns, monatDesVersicherungsBeginns, tagDesVersicherungsBeginns);
    }

    private boolean checkIfFahrzeugHerstellerExists(JSONObject jsonObject) throws Exception {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\fahrzeugHersteller\\fahrzeugHersteller.json");
        JSONObject fahrzeugJson = (JSONObject) jsonParser.parse((reader));

        String fahrzeugHersteller = jsonObject.get("fahrzeug_hersteller").toString().toLowerCase();

        if (fahrzeugJson.containsKey(fahrzeugHersteller)) {
            return true;
        } else {
            return false;
        }
    }
}
