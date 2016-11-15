package org.okarmus.currency.exception;

/**
 * Created by mateusz on 14.11.16.
 */
public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
