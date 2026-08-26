package com.example.tp1.controller;

import com.example.tp1.model.Car;
import com.example.tp1.model.Dates;
import com.example.tp1.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Les services REST de l'application de location.
 *
 * GET  /cars                          -> toutes les voitures
 * GET  /cars?rented=false             -> les voitures non louees
 * GET  /cars/{plateNumber}            -> les caracteristiques d'une voiture
 * PUT  /cars/{plateNumber}?rent=true  -> louer (dates dans le corps de la requete)
 * PUT  /cars/{plateNumber}?rent=false -> rendre la voiture
 */
@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    /** Injection de dependance par constructeur (recommandee par Spring). */
    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Liste des voitures.
     * Le parametre rented est optionnel :
     * - absent          -> toutes les voitures
     * - rented=false    -> uniquement les voitures disponibles
     * - rented=true     -> uniquement les voitures louees
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Car> listOfCars(@RequestParam(value = "rented", required = false) Boolean rented) {
        if (rented == null) {
            return carService.findAll();
        }
        return rented ? carService.findRented() : carService.findAvailable();
    }

    /** Raccourci pratique : la liste des voitures disponibles. */
    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Car> availableCars() {
        return carService.findAvailable();
    }

    /** Les caracteristiques d'une voiture : { "plateNumber":..., "brand":..., "price":... }. */
    @GetMapping("/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Car aCar(@PathVariable("plateNumber") String plateNumber) {
        return carService.findByPlateNumber(plateNumber);
    }

    /**
     * Louer ou rendre une voiture.
     *
     * Une seule methode gere les deux cas : elles partagent la meme URI et la
     * meme methode HTTP, deux @PutMapping identiques provoqueraient une erreur
     * de mapping ambigu au demarrage.
     *
     * Le corps de la requete (les dates) n'est necessaire que pour la location,
     * d'ou required = false.
     */
    @PutMapping("/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Car rentOrGetBack(@PathVariable("plateNumber") String plateNumber,
                             @RequestParam(value = "rent", required = true) boolean rent,
                             @RequestBody(required = false) Dates dates) {
        if (rent) {
            return carService.rent(plateNumber, dates);
        }
        return carService.giveBack(plateNumber);
    }
}
