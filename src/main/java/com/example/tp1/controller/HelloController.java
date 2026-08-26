package com.example.tp1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Page d'accueil : sert surtout a verifier que le serveur a bien demarre.
 * http://localhost:8080
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "hello - API de location de voitures\n\n"
                + "GET  /cars                          : toutes les voitures\n"
                + "GET  /cars?rented=false             : les voitures disponibles\n"
                + "GET  /cars/{plateNumber}            : une voiture\n"
                + "PUT  /cars/{plateNumber}?rent=true  : louer (corps : {\"begin\":\"11/11/2017\",\"end\":\"1/1/2018\"})\n"
                + "PUT  /cars/{plateNumber}?rent=false : rendre la voiture\n";
    }
}
