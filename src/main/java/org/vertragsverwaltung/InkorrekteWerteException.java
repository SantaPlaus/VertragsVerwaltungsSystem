package org.vertragsverwaltung;

import java.text.ParseException;

public class InkorrekteWerteException extends RuntimeException {
    private String errorMessage;
    public InkorrekteWerteException(String errorMessage) {
        super(errorMessage);
    }
}