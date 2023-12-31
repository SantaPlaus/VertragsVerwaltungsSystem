package org.vertragsverwaltung.Services;

import org.json.simple.JSONObject;
import org.vertragsverwaltung.Data.FileRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

public class ValidierungsService {
    // Klasse mit Methoden, die der Validierung sämtlicher Daten dienen
    public boolean isPreisValid(JSONObject jsonObject){
        /**
         * Validiert die eingegebenen Daten im JSONObject für die Preisberechnung
         * @param jsonObject Das JSONObject mit den Daten, die validiert werden
         * @return true, wenn alles validiert wurde, sonst false
         */

        try {
            if (!checkIfFahrzeugHerstellerExists(jsonObject)) {
                return false;
                // Kontrolliert, ob der Fahrzeughersteller existiert, falls nicht wird false zurückgegeben
            }

            int fahrzeugHoechstgeschwindigkeit = (int) (long) jsonObject.get("fahrzeug_hoechstgeschwindigkeit");
            // Der Integer-Variable wird die Höchstgeschwindigkeit des Autos zugewiesen

            if (fahrzeugHoechstgeschwindigkeit == 0 || fahrzeugHoechstgeschwindigkeit > 250) {
                return false;
                // Überprüft, ob die Geschwindigkeit Null oder über 250 liegt, wenn ja, dann wird false zurückgegeben
            }

            String datum = (String) jsonObject.get("geburtsdatum");
            // Weist der String-Variable datum den value vom key geburtsdatum des JSONObjects zu

            Date geburtsdatum;
            // deklariert die Date-Variable geburtsdatum

            try {
                geburtsdatum = new SimpleDateFormat("dd.MM.yyyy").parse(datum);
                // weist dem geburtsdatum das Datum im obrigen, geparsten Format zu

            } catch (ParseException e) {
                System.out.println("Datum im falschen Format angegeben. (DD/MM/YYYY)");
                return false;
                // Behandelt eine ParseException, falls die Werte nicht übergeben werden konnten, gibt eine Fehlermedlung aus, sowie false zurück

            }
            if (geburtsdatum.getYear() > (Year.now().getValue()) - 18) {
                return false;
                // Überprüft, ob die Person älter als 18 ist, sonst wird false zurückgegeben

            }
        } catch (NullPointerException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return false;
            // Behandelt eine NullPointerException, gibt eine Fehlermeldung aus, sowie false zurück

        } catch (ClassCastException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return false;
            // Behandelt eine ClassCastException, gibt eine Fehlermeldung aus, sowie false zurück

        }
        return true;
        // Gibt true zurück, da die Validierung erfolgreich abgelaufen ist
    }

    public boolean isVersicherungsbeginnValid(JSONObject jsonObject) {
        /**
         * Überprüft, ob der Versicherungsbeginn des JSONObjects in der Vergangenheit liegt
         * @param jsonObject Das JSONObject mit den Daten, die validiert werden
         * @return true, wenn der Versicherungsbeginn validiert wurde, sonst false
         */

        LocalDate versicherungsBeginn = null;
        // Deklariert LocalDate versicherungsBeginn vor der abgekapselten try-catch-Abfrage
        try {
            versicherungsBeginn = getVersicherungsBeginn(jsonObject);
            // Übergibt den Versicherungsbeginn des JSONObjects an die Variable

        } catch (Exception e) {
            System.out.println("Datum im falschen Format angegeben. (DD/MM/YYYY)");
            return false;
            // Behandelt eine Exception, wenn das Datum im falschen Format angegeben wurde, gibt eine Fehlermeldung aus, sowie false zurück
        }
        if (versicherungsBeginn.isBefore(LocalDate.now())) {
            System.out.println("Der Versicherungsbeginn darf nicht in der Vergangenheit liegen.");
            return false;
            // Überprüft, ob der Beginn vor dem jetzigen Datum liegt. Ist dies der Fall, wird false zurückgegeben
        }
        return true;
        // Wurde alles validiert, so wird true zurückgegeben
    }

    public boolean isAenderungVertragValid(JSONObject jsonObject) {
        /**
         * Überprüft, ob alle Vertragsänderungen vom JSONObject valide sind
         * @param jsonObject Das JSONObject mit den Daten, die validiert werden
         * @return true, wenn alle Änderungen validiert wurden, sonst false
         */

        String vorname = null;
        String nachname = null;
        String addresse = null;
        String fahrzeugTyp = null;
        // Deklaration sämtlicher Variablen, die in der try-catch-Abfrage genutzt werden
        boolean richtigesKennzeichenFormat = false;
        // Initialisierung der Boolean-Variable richtigesKennzeichenFormat

        try {
            vorname = (String) jsonObject.get("vorname");
            // Zuweisung der value vom key "vorname"

            nachname = (String) jsonObject.get("nachname");
            // Zuweisung der value vom key "nachname"

            addresse = (String) jsonObject.get("addresse");
            // Zuweisung der value vom key "addresse"

            fahrzeugTyp = (String) jsonObject.get("fahrzeug_typ");
            // Zuweisung der value vom key "fahrzeug_typ"

            String kennzeichen = (String) jsonObject.get("amtliches_kennzeichen");
            // Zuweisung der value vom key "amtliches_kennzeichen"

            richtigesKennzeichenFormat = !kennzeichen.matches("[A-Z]{1,3}-[A-Z]{1,2}-[0-9]{1,4}");
            // Zuweisung eines booleans, abhängig ob das Kennzeichen dem obrigen, deutschen Format übereinstimmt

        } catch (NullPointerException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return false;
            // Behandelt eine NullPointerException, wenn einer der obrigen Werte nicht existiert, gibt eine Fehlermeldung aus, sowie false zurück
        } catch (ClassCastException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return false;
            // Behandelt eine ClassCastException, wenn ein Wert in der Datei das falsche Format hat, gibt eine Fehlermeldung aus, sowie false zurück
        }

        if (isPreisValid(jsonObject) == false) {
            return false;
            // Überprüft, ob die eingegebenen Daten im Bezug auf die Preisberechnung valide sind, falls nicht wird false zurückgegeben
        }

        if (vorname == null) {
            return false;
        } else if (nachname.equals(null)) {
            return false;
        } else if (addresse.equals(null)) {
            return false;
        } else if (fahrzeugTyp.equals(null)) {
            return false;
            // Überprüft, ob die Werte der jeweiligen Variablen "null" ist, falls ja wird false zurückgegeben
        } else if (richtigesKennzeichenFormat) {
            return false;
            // Überprüft, ob das richtige Kennzeichenformat angegeben wurde
        } else {
            return true;
            // Wurde alles validiert, wird true ausgegeben
        }
    }

    public boolean isNeuVertragValid(JSONObject jsonObject) {
        /**
         * Validiert die Daten aus dem JSONObject bezogen auf einen Neuvertrag
         * @param jsonObject Das JSONObject mit den Daten, die validiert werden
         * @return true, wenn alle Änderungen validiert wurden, sonst false
         */

        if (isAenderungVertragValid(jsonObject) && isVersicherungsbeginnValid(jsonObject)) {
            return true;
            // prüft, die Daten mit den beiden obrigen Methoden nach validität. Bei Erfolg wird true zurückgegeben
        }
        return false;
        // Bei Misserfolg wird false ausgegeben
    }

    private static LocalDate getVersicherungsBeginn(JSONObject jsonObject) {
        /**
         * Extrahiert den Versicherungsbeginn aus dem gegebenen JSONObject
         * @param jsonObject Das JSONObject mit den Daten für den Versicherungsbeginn
         * @return Das Datum des Versicherungsbeginns
         */

        String versicherungsBeginnString = (String) jsonObject.get("versicherungsbeginn");
        // Weist dem String den value vom key "versicherungsbeginn" zu

        int jahrDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(6));
        // Weist der Integer-Variable das Jahr des Versicherungsbeginns zu

        int monatDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(3, 5));
        // Weist der Integer-Variable den Monat des Versicherungsbeginns zu

        int tagDesVersicherungsBeginns = Integer.parseInt(versicherungsBeginnString.substring(0, 2));
        // Weist der Integer-Variable den Tag des Versicherungsbeginns zu

        return LocalDate.of(jahrDesVersicherungsBeginns, monatDesVersicherungsBeginns, tagDesVersicherungsBeginns);
        // Erstellt das LocalDate für das Datum des Verisicherungsbeginns und gibt dieses zurück
    }

    private boolean checkIfFahrzeugHerstellerExists(JSONObject jsonObject) {
        /**
         * Überprüft, ob der Fahrzeughersteller aus dem JSONObject bei der Versicherung existiert
         * @param Das JSONObject, aus welchem der Hersteller geprüft werden soll
         * @raturn true, wenn der Hersteller existiert, andernfalls false
         */

        FileRepository fileRepository = new FileRepository();
        // Initialisiert ein FileRepository

        String path = fileRepository.srcPath() + "\\\\main\\\\resources\\\\fahrzeugHersteller\\\\fahrzeugHersteller.json";
        // Pfad zur Datei mit der Liste der Fahrzeughersteller

        JSONObject fahrzeugJson = fileRepository.getJsonObject(path);
        // Liest die Daten der Datei aus dem Pfad ein

        String fahrzeugHersteller = fileRepository.getFahrzeugHersteller(jsonObject);
        // Extrahiert den value vom key fahzeughersteller aus dem JSONObject

        if (fahrzeugJson.containsKey(fahrzeugHersteller)) {
            return true;
            // Überprüft, ob der Fahrzeughersteller aus dem JSONObject in der Liste enthalten ist. Bei Erfolg wird true zurückgegeben
        } else {
            return false;
            // Bei Misserfolg wird false zurückgegeben
        }
    }

    public String isMethodeValid(JSONObject jsonObject) {
        /**
         * Überprüfen, ob der value vom key methode valide ist
         * @param jsonObject Das JSONObject, welches überprüft werden soll
         * @return den value des JSONObjects oder ein leerer String
         */

        try {
            return (String) jsonObject.get("methode");
            // Rückgabe des values vom key methode
        } catch (Exception e) {
            return "";
            // Behandeln einer Exception und zurückgeben eines leeren Strings
        }
    }

    public String isAktionValid(JSONObject jsonObject) {
        /**
         * Überprüfen, ob der value vom key aktion valide ist
         * @param jsonObject Das JSONObject, welches überprüft werden soll
         * @return den value des JSONObjects oder ein leerer String
         */

        try {
            return (String) jsonObject.get("aktion");
            // Rückgabe des values vom key aktion
        } catch (Exception e) {
            return "";
            // Behandeln einer Exception und zurückgeben eines leeren Strings
        }
    }
}
