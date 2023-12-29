package org.vertragsverwaltung.Main;


import org.vertragsverwaltung.Controller.VertragController;

public class Main {

    public static void main(String[] args) {

        VertragController vertragController = new VertragController();
        vertragController.optionsAuswahl();

        /**
         *  Optionen:
         *      - alle Verträge anzeigen                    GET /vertraege              ✔️
         *
         *      - einen bestimmten Vertrag anzeigen         GET /vertraege/{vsnr}       ✔️
         *
         *      - neuen/geänderten Vertrag empfangen
         *        und Preis zurückgeben                     POST /preis                 ✔️      // nur den Preis berechnen und ausgeben // vorher prüfen
         *
         *      - leeren Vertrag anlegen                    POST /anlegen               ✔️
         *
         *      - neuen Vertrag empfangen, anlegen (prüfen)
         *        und neue VSNR zurückgeben                 POST /neu                   ✔️
         *
         *      - geänderten Vertag empfangen, alten
         *        überschreiben und mit neuem Preis
         *        zurückgeben                               POST /aenderung             ✔️
         *
         *      - löscht Vertrag ohne Prüfung               DELETE /vertraege/{vsnr}    ✔️
         *
         *      {„Methode“: „GET“, „Aktion“: „/vertraege“}
         *
         *
         */
    }
}

