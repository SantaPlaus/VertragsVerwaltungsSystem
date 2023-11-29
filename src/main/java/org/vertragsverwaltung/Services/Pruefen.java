package org.vertragsverwaltung.Services;

import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pruefen {
    public boolean nachValiditaetPruefen(JSONObject jsonObject){
        String vorname = (String) jsonObject.get("vorname");
        String nachname = (String) jsonObject.get("nachname");
        String addresse = (String) jsonObject.get("addresse");
        String fahrzeugHersteller = (String) jsonObject.get("fahrzeug_hersteller");
        String fahrzeugTyp = (String) jsonObject.get("fahrzeug_typ");
        int fahrzeugHoechstgeschwindigkeit = (int)(long)jsonObject.get("fahrzeug_hoechstgeschwindigkeit");
        String kennzeichen = (String) jsonObject.get("amtliches_kennzeichen");
        boolean richtigesKennzeichenFormat = !kennzeichen.matches("[A-Z]{1,3}-[A-Z]{1,2}-[0-9]{1,4}");

        try {
            String datum = (String) jsonObject.get("geburtsdatum");
            String versicherungsBeginn = (String) jsonObject.get("versicherungsbeginn");
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(datum);
            Date date2 = new SimpleDateFormat("dd.MM.yyyy").parse(versicherungsBeginn);
        } catch (NumberFormatException e) {
            System.out.println("Datum im falschen Format angegeben.");
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (vorname.equals(null)) {
            return false;
        } else if (nachname.equals(null)) {
            return false;
        } else if (addresse.equals(null)) {
            return false;
        } else if (fahrzeugHersteller.equals(null)) {
            return false;
        } else if (fahrzeugTyp.equals(null)) {
            return false;
        } else if (fahrzeugHoechstgeschwindigkeit == 0) { //nicht über 250 kmh
            return false;
        } else if (richtigesKennzeichenFormat) {
            return false;
        } else {
            return true;
        }
    }
}
