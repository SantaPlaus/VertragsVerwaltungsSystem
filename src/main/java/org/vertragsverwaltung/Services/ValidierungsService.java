package org.vertragsverwaltung.Services;

import org.json.simple.JSONObject;
import org.vertragsverwaltung.Data.FileRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

public class ValidierungsService {
    public boolean isNeuVertragValid(JSONObject jsonObject) {
        if (isAenderungVertragValid(jsonObject) && isVersicherungsbeginnValid(jsonObject)) {
            return true;
        }
        return false;
    }

    private boolean isVersicherungsbeginnValid(JSONObject jsonObject) throws NullPointerException{
        LocalDate versicherungsBeginn = null;
        try {
            versicherungsBeginn = getVersicherungsBeginn(jsonObject);
        } catch (Exception e) {
            System.out.println("Datum im falschen Format angegeben. (DD/MM/YYYY)");
            return false;
        }
        if (versicherungsBeginn.isBefore(LocalDate.now())) {
            System.out.println("Der Versicherungsbeginn darf nicht in der Vergangenheit liegen.");
            return false;
        }
        return true;
    }

    public boolean isAenderungVertragValid(JSONObject jsonObject) throws NullPointerException{

        String vorname = null;
        String nachname = null;
        String addresse = null;
        String fahrzeugTyp = null;
        boolean richtigesKennzeichenFormat = false;
        try {
            vorname = (String) jsonObject.get("vorname");
            nachname = (String) jsonObject.get("nachname");
            addresse = (String) jsonObject.get("addresse");
            fahrzeugTyp = (String) jsonObject.get("fahrzeug_typ");
            String kennzeichen = (String) jsonObject.get("amtliches_kennzeichen");
            richtigesKennzeichenFormat = !kennzeichen.matches("[A-Z]{1,3}-[A-Z]{1,2}-[0-9]{1,4}");
        } catch (NullPointerException e) {
            System.out.println("NullPointer");
        } catch (ClassCastException e) {
            System.out.println("ClassCast");
        }


        if (isPreisValid(jsonObject) == false) {
            return false;
        }

         if (vorname == null) {
            return false;
        } else if (nachname.equals(null)) {
            return false;
        } else if (addresse.equals(null)) {
            return false;
        } else if (fahrzeugTyp.equals(null)) {
            return false;
        } else if (richtigesKennzeichenFormat) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isPreisValid(JSONObject jsonObject){

        try {
            if (!checkIfFahrzeugHerstellerExists(jsonObject)) {
                return false;
            }

            int fahrzeugHoechstgeschwindigkeit = (int) (long) jsonObject.get("fahrzeug_hoechstgeschwindigkeit");

            if (fahrzeugHoechstgeschwindigkeit == 0 || fahrzeugHoechstgeschwindigkeit > 250) {
                return false;
            }

            String datum = (String) jsonObject.get("geburtsdatum");
            Date geburtsdatum;
            try {
                geburtsdatum = new SimpleDateFormat("dd.MM.yyyy").parse(datum);
            } catch (ParseException e) {
                System.out.println("Datum im falschen Format angegeben. (DD/MM/YYYY)");
                return false;
            }
            if (geburtsdatum.getYear() > (Year.now().getValue()) - 18) {
                return false;
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointer");
            return false;
        } catch (ClassCastException e) {
            System.out.println("ClassCast");
            return false;
        }
        return true;
    }


    private static LocalDate getVersicherungsBeginn(JSONObject jsonObject) {
        String versicherungsBeginnString = (String) jsonObject.get("versicherungsbeginn");
        int jahrDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(6));
        int monatDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(3, 5));
        int tagDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(0, 2));

        return LocalDate.of(jahrDesVersicherungsBeginns, monatDesVersicherungsBeginns, tagDesVersicherungsBeginns);
    }

    private boolean checkIfFahrzeugHerstellerExists(JSONObject jsonObject) {

        FileRepository fileRepository = new FileRepository();

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\fahrzeugHersteller\\fahrzeugHersteller.json";

        JSONObject fahrzeugJson = fileRepository.getJsonObject(path);

        String fahrzeugHersteller = fileRepository.getFahrzeugHersteller(jsonObject);

        if (fahrzeugJson.containsKey(fahrzeugHersteller)) {
            return true;
        } else {
            return false;
        }
    }

    public String isMethodeValid(JSONObject jsonObject) {
        try {
            return (String) jsonObject.get("methode");
        } catch (Exception e) {
            return "";
        }
    }

    public String isAktionValid(JSONObject jsonObject) {
        try {
            return (String) jsonObject.get("aktion");
        } catch (Exception e) {
            return "";
        }
    }
}
