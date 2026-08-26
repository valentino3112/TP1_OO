package com.example.tp1.exception;

/** Levee quand on loue une voiture deja louee, ou qu'on rend une voiture non louee -&gt; HTTP 409. */
public class CarNotAvailableException extends RuntimeException {

    public CarNotAvailableException(String message) {
        super(message);
    }
}
