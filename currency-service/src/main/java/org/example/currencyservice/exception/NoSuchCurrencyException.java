package org.example.currencyservice.exception;

public class NoSuchCurrencyException extends RuntimeException {
    public NoSuchCurrencyException(String message) {
        super(message);
    }
}
