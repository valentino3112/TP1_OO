package com.example.tp1.exception;

/** Levee quand aucune voiture ne correspond a la plaque demandee -&gt; HTTP 404. */
public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(String plateNumber) {
        super("Aucune voiture avec la plaque " + plateNumber);
    }
}
