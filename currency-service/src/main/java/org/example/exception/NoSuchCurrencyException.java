package org.example.exception;

public class NoSuchCurrencyException extends RuntimeException {
    public NoSuchCurrencyException(String message) {
        super(message);
    }
}
