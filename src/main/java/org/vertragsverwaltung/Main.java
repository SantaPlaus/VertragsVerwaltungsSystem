package org.vertragsverwaltung;


public class Main {

    public static void main(String[] args) {

        Optionen opt = new Optionen();
        opt.optionsAuswahl();

        /**
         *  Optionen:
         *      - alle Verträge anzeigen                GET /vertraege              X
         *
         *      - einen bestimmten Vertrag anzeigen     GET /vertraege/{vsnr}       ✔️
         *
         *      - neuen/geänderten Vertrag empfangen
         *        und Preis zurückgeben                 POST /preis                 ✔️  // 2 Nachkommastellen (runden) & €-Zeichen
         *
         *      - leeren Vertrag anlegen                POST /anlegen               ✔️
         *
         *      - neuen Vertrag empfangen, anlegen (prüfen)
         *        und neue VSNR zurückgeben             POST /neu
         *
         *      - geänderten Vertag empfangen, alten
         *        überschreiben und mit neuem Preis
         *        zurückgeben                           POST /aenderung             ✔️
         *
         *      - löscht Vertrag ohne Prüfung           DELETE /vertraege/{vsnr}    ✔️
         *
         *      {„Methode“: „GET“, „Aktion“: „/vertraege“}
         *
         *
         */
    }
}

