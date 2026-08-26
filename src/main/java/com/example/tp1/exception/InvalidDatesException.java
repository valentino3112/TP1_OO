package com.example.tp1.exception;

/** Levee quand les dates de location sont absentes ou mal formatees -&gt; HTTP 400. */
public class InvalidDatesException extends RuntimeException {

    public InvalidDatesException(String message) {
        super(message);
    }
}
