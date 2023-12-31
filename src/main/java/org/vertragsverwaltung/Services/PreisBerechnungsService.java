package org.vertragsverwaltung.Services;

import org.json.simple.JSONObject;
import org.vertragsverwaltung.Data.FileRepository;

import java.time.Year;

public class PreisBerechnungsService {
    // Klasse mit Methoden, die der Preisberechnung dienen
    public String postPreis(JSONObject jsonObject) {
        /**
         * Berechnet die monatliche Versicherungssumme basierend auf den Daten in JSONObject
         * @param jsonObject Das JSONObject mit den Daten, die für die Berechnung benötigt werden
         * @return Der Preis als String oder eine Fehlermeldung
         */

        ValidierungsService validierungsService = new ValidierungsService();
        // Initialisierung vom ValidierungsService

        if (validierungsService.isPreisValid(jsonObject)) {
            // Die If-Abfrage überprüft, ob die eingegebenen Daten valide sind

            String geburtsdatum = (String) jsonObject.get("geburtsdatum");
            // Extrahiert das Geburtsdatum aus dem JSONObject

            int geburtsjahr = Integer.parseInt(geburtsdatum.substring(6));
            // Verkürzt das Geburtsdatum auf das Jahr für die Berechnung

            int vMax = (int) (long) jsonObject.get("fahrzeug_hoechstgeschwindigkeit");
            // Weist der Integer-Variable vMax den value vom key fahrzeug_hoechstgeschwindigkeit zu

            int alter;
            int sfKlasseVereinfacht;
            // Deklaration von den beiden Integer-Variablen

            double rabattInProzent = 0;
            // Initialisierung von der Double-Variable

            alter = Year.now().getValue() - geburtsjahr;
            // Vereinfachte Berechnung des Alters

            sfKlasseVereinfacht = alter - 17;
            // Vereinfachte Herleitung der Schadenfreiheitsklasse

            rabattInProzent = getRabattInProzent(sfKlasseVereinfacht);
            // Berechnung des Rabattes in Prozent

            double herstellerBedingteMehrkosten = getHerstellerBedingteMehrkosten(jsonObject);
            // Berechnung der Herstellerbedingten Mehrkosten

            double versicherungsSummeMonatlich = (vMax) * herstellerBedingteMehrkosten * (1 - rabattInProzent);
            // Berechnung der monatlichen Versicherungssumme

            return "" + Math.round(versicherungsSummeMonatlich * 100) / 100.00;
            // Rückgabe der Versicherungssumme
        }
        return "Eingabe nicht Valide.";
        // Rückgabe der Fehlermeldung, falls die If-Abfrage false ergibt
    }


    private double getRabattInProzent(int sfKlasseVereinfacht) {
        /**
         * Ermittelt den Rabatt in Prozent auf Basis der vereinfachten Schadenfreiheitsklasse
         * @param sfKlasseVereinfacht SF-Klasse, die entscheidet welche Rechnung ausgewählt wird
         * @retunr rabattInProzent als double
         */

        double rabatt;
        // Deklaration der double-Variable "rabatt"

        if (sfKlasseVereinfacht <= 3) {
            rabatt = rechnungBisSFKlasseDrei(sfKlasseVereinfacht);
        } else if (sfKlasseVereinfacht <= 7) {
            rabatt = rechnungBisSFKlasseSieben(sfKlasseVereinfacht);
        } else if (sfKlasseVereinfacht <= 34) {
            rabatt = rechnungBisSFKlasseVierUndDreissig(sfKlasseVereinfacht);
        } else {
            rabatt = 80;
            // Wenn die Abfrage durchgelaufen ist und SF-Klasse höher als 34 ist, ist der Rabatt bei 80
        }
        double rabattInProzent = rabatt * 0.01;
        return rabattInProzent;
        // Rückgabe des Rabatts in Prozent
    }

    private double rechnungBisSFKlasseDrei(int x) {
        return 15 * x - 15;
        // Berechnung und Rückgabe des Preises bis SF-Klasse drei
    }

    private double rechnungBisSFKlasseSieben(int x) {
        double a = 0.41666666666667;
        double b = -7.5;
        double c = 47.0833333333334;
        double d = -55;

        return (a * x * x * x) + (b * x * x) + (c * x) + (d);
        // Berechnung und Rückgabe des Preises ab SF-Klasse vier bis SF-Klasse 7

    }

    private double rechnungBisSFKlasseVierUndDreissig(int x) {
        double a = 0.0012809956559;
        double b = -0.1038687940241;
        double c = 3.5108100194322;
        double d = 30.4325822470923;

        return (a * x * x * x) + (b * x * x) + (c * x) + (d);
        // Berechnung und Rückgabe des Preises ab SF-Klasse acht bis SF-Klasse 34
    }

    private double getHerstellerBedingteMehrkosten(JSONObject jsonObject) {
        /**
         * Ermittelt die Mehrkosten, welche je nach Hersteller unterschiedlich sind
         * @param jsonObject Das JSONObject, wessen Hersteller nach Mehrkosten überprüft
         * @return Der value vom key des Herstellers
         */

        FileRepository fileRepository = new FileRepository();
        // Initialisieren vom FileRepository

        String path = fileRepository.srcPath() + "\\\\main\\\\resources\\\\fahrzeugHersteller\\\\fahrzeugHersteller.json";
        // Dateipfad zur JSON-Datei mit den Herstellern

        JSONObject fahrzeugJson = fileRepository.getJsonObject(path);
        // Zuweisung vom JSONObject der JSON-Datei

        String fahrzeugHersteller = fileRepository.getFahrzeugHersteller(jsonObject);
        // Zuweisung vom key des Herstellers in die String-Variable

        return (double) (long) fahrzeugJson.get(fahrzeugHersteller);
        // Rückgabe des values vom key fahrzeugHersteller
    }
}
