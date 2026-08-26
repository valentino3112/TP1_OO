package com.example.tp1.service;

import com.example.tp1.exception.CarNotAvailableException;
import com.example.tp1.exception.CarNotFoundException;
import com.example.tp1.exception.InvalidDatesException;
import com.example.tp1.model.Car;
import com.example.tp1.model.Dates;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Contient toute la logique metier de la location.
 * Les voitures sont stockees en memoire dans une Map (pas de base de donnees
 * pour ce TP) : les donnees sont donc reinitialisees a chaque redemarrage.
 */
@Service
public class CarService {

    /** Format des dates du sujet : 11/11/2017 ou 1/1/2018 (jour/mois/annee). */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d/M/uuuu");

    /** Cle = plaque d'immatriculation en majuscules. */
    private final Map<String, Car> cars = new LinkedHashMap<>();

    public CarService() {
        save(new Car("11AA22", "Ferrari", 100));
        save(new Car("AA11BB", "Renault", 35));
        save(new Car("BB22CC", "Peugeot", 40));
        save(new Car("CC33DD", "Tesla", 90));
        save(new Car("DD44EE", "Fiat", 25));
    }

    private void save(Car car) {
        cars.put(key(car.getPlateNumber()), car);
    }

    private String key(String plateNumber) {
        return plateNumber == null ? "" : plateNumber.trim().toUpperCase();
    }

    /** Toutes les voitures du parc. */
    public synchronized List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }

    /** Les voitures disponibles (non louees). */
    public synchronized List<Car> findAvailable() {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (!car.isRented()) {
                result.add(car);
            }
        }
        return result;
    }

    /** Les voitures actuellement louees. */
    public synchronized List<Car> findRented() {
        List<Car> result = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.isRented()) {
                result.add(car);
            }
        }
        return result;
    }

    /**
     * Les caracteristiques d'une voiture.
     *
     * @throws CarNotFoundException si la plaque est inconnue
     */
    public synchronized Car findByPlateNumber(String plateNumber) {
        Car car = cars.get(key(plateNumber));
        if (car == null) {
            throw new CarNotFoundException(plateNumber);
        }
        return car;
    }

    /**
     * Loue une voiture sur la periode donnee.
     *
     * @throws CarNotFoundException     si la plaque est inconnue
     * @throws InvalidDatesException    si les dates sont absentes ou invalides
     * @throws CarNotAvailableException si la voiture est deja louee
     */
    public synchronized Car rent(String plateNumber, Dates dates) {
        Car car = findByPlateNumber(plateNumber);

        if (car.isRented()) {
            throw new CarNotAvailableException("La voiture " + car.getPlateNumber() + " est deja louee");
        }
        if (dates == null || dates.getBegin() == null || dates.getEnd() == null) {
            throw new InvalidDatesException(
                    "Le corps de la requete doit contenir les dates, ex : {\"begin\":\"11/11/2017\",\"end\":\"1/1/2018\"}");
        }

        LocalDate begin = parse(dates.getBegin());
        LocalDate end = parse(dates.getEnd());
        if (end.isBefore(begin)) {
            throw new InvalidDatesException("La date de fin doit etre posterieure a la date de debut");
        }

        car.setRented(true);
        car.setBegin(dates.getBegin());
        car.setEnd(dates.getEnd());
        return car;
    }

    /**
     * Rend une voiture louee.
     *
     * @throws CarNotFoundException     si la plaque est inconnue
     * @throws CarNotAvailableException si la voiture n'etait pas louee
     */
    public synchronized Car giveBack(String plateNumber) {
        Car car = findByPlateNumber(plateNumber);

        if (!car.isRented()) {
            throw new CarNotAvailableException("La voiture " + car.getPlateNumber() + " n'est pas louee");
        }

        car.setRented(false);
        car.setBegin(null);
        car.setEnd(null);
        return car;
    }

    private LocalDate parse(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidDatesException("Date invalide : " + date + " (format attendu : jour/mois/annee)");
        }
    }
}
