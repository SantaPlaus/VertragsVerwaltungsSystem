package org.vertragsverwaltung;

import org.json.simple.JSONObject;

public class PreisBerechnung {

    public double postPreis(JSONObject jsonObject){
        System.out.println("POST /preis");

        String geburtsdatum = (String) jsonObject.get("geburtsdatum");
        int geburtsjahr = Integer.parseInt(geburtsdatum.substring(6));
        int vMax = (int) (long) jsonObject.get("fahrzeug_hoechstgeschwindigkeit");
        int alter;
        int sfKlasseVereinfacht;
        double rabattInProzent = 0;

        if (geburtsjahr > 2005){
            System.out.println("ZU JUNG");
            return 0;
        }

        alter = 2023 - geburtsjahr;
        sfKlasseVereinfacht = alter - 17;

        rabattInProzent = getRabattInProzent(sfKlasseVereinfacht);

        double versicherungsSummeMonatlich = (vMax / 2) * (1 - rabattInProzent);

        return versicherungsSummeMonatlich;
    }

    private double getRabattInProzent(int sfKlasseVereinfacht) {
        double rabatt;
        if (sfKlasseVereinfacht <= 3){
            rabatt = rechnungEins(sfKlasseVereinfacht);
        } else if (sfKlasseVereinfacht <= 7) {
            rabatt = rechnungZwei(sfKlasseVereinfacht);
        } else if (sfKlasseVereinfacht <= 34) {
            rabatt = rechnungDrei(sfKlasseVereinfacht);
        } else {
            rabatt = 80;
        }
        double rabattInProzent = rabatt * 0.01;
        return rabattInProzent;
    }

    private double rechnungEins(int x) {
        return 15 * x - 15;
    }
    private double rechnungZwei(int x) {
        double a = 0.41666666666667;
        double b = -7.5;
        double c = 47.0833333333334;
        double d = -55;

        return (a * x * x * x) + (b * x * x) + (c * x) + (d);
    }
    private double rechnungDrei(int x) {
        double a = 0.0012809956559;
        double b = -0.1038687940241;
        double c = 3.5108100194322;
        double d = 30.4325822470923;

        return (a * x * x * x) + (b * x * x) + (c * x) + (d);
    }
}
