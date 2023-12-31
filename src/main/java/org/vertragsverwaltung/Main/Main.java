package org.vertragsverwaltung.Main;


import org.vertragsverwaltung.Controller.VertragController;

public class Main {

    public static void main(String[] args) {
        /**
         * Die Main-Methode führt den Code aus
         */

        VertragController vertragController = new VertragController();
        // Initialisierung des VertragControllers

        vertragController.optionsAuswahl();
        // Ausführen der Methode "optionsAuswahl", welche das Programm durchläuft
    }
}

