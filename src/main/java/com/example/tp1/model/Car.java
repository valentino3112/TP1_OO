package com.example.tp1.model;

/**
 * Une voiture du parc de location.
 *
 * Serialisee automatiquement en JSON par Jackson (via les getters) :
 * { "plateNumber": "11AA22", "brand": "Ferrari", "price": 100.0, "rented": false }
 */
public class Car {

    private String plateNumber;
    private String brand;
    private double price;

    /** true si la voiture est actuellement louee. */
    private boolean rented;

    /** Date de debut de location (null si la voiture n'est pas louee). */
    private String begin;

    /** Date de fin de location (null si la voiture n'est pas louee). */
    private String end;

    /** Constructeur vide obligatoire pour Jackson. */
    public Car() {
    }

    public Car(String plateNumber, String brand, double price) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.price = price;
        this.rented = false;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Car{" + plateNumber + ", " + brand + ", " + price + ", rented=" + rented + "}";
    }
}
