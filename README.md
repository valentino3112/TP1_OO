# TP Rest Web Service — Location de voitures

Application Spring Boot exposant une API REST de location de voitures.

## Lancer le projet

```bash
./gradlew bootRun          # Linux / macOS
gradlew.bat bootRun        # Windows
```
 <http://localhost:8080>

> Le projet généré par Spring Initializr ne contenait que `spring-boot-starter` :
> la dépendance `spring-boot-starter-web` a été ajoutée dans `build.gradle`,
> sans elle aucun contrôleur REST ne fonctionne.

## Endpoints

| Méthode | URI | Description | Code |
|---|---|---|---|
| GET | `/cars` | Toutes les voitures | 200 |
| GET | `/cars?rented=false` | Les voitures **non louées** | 200 |
| GET | `/cars?rented=true` | Les voitures louées | 200 |
| GET | `/cars/available` | Raccourci pour les voitures non louées | 200 |
| GET | `/cars/{plateNumber}` | Caractéristiques d'une voiture | 200 / 404 |
| PUT | `/cars/{plateNumber}?rent=true` | Louer (dates dans le corps) | 200 / 400 / 404 / 409 |
| PUT | `/cars/{plateNumber}?rent=false` | Rendre la voiture | 200 / 404 / 409 |

Codes d'erreur : `404` plaque inconnue, `409` voiture déjà louée (ou déjà rendue),
`400` dates absentes ou mal formatées.

## Exemples

```bash
# Liste des voitures disponibles
curl "http://localhost:8080/cars?rented=false"

# Une voiture
curl http://localhost:8080/cars/11AA22
# -> {"plateNumber":"11AA22","brand":"Ferrari","price":100.0,"rented":false}

# Louer
curl -X PUT "http://localhost:8080/cars/11AA22?rent=true" \
     -H "Content-Type: application/json" \
     -d '{"begin":"11/11/2017","end":"1/1/2018"}'
# -> {"plateNumber":"11AA22",...,"rented":true,"begin":"11/11/2017","end":"1/1/2018"}

# Rendre
curl -X PUT "http://localhost:8080/cars/11AA22?rent=false"
```

Sous Windows PowerShell, remplacer les guillemets simples par des doubles échappés,
ou utiliser Postman / le fichier `requests.http` fourni (IntelliJ, VS Code REST Client).

## Structure

```
src/main/java/com/example/tp1/
├── Tp1Application.java              point d'entrée
├── controller/
│   ├── CarController.java           les endpoints REST
│   └── HelloController.java         page d'accueil "/"
├── service/
│   └── CarService.java              logique métier + stockage en mémoire (Map)
├── model/
│   ├── Car.java                     plateNumber, brand, price, rented, begin, end
│   └── Dates.java                   corps de la requête PUT
└── exception/
    ├── CarNotFoundException.java        -> 404
    ├── CarNotAvailableException.java    -> 409
    ├── InvalidDatesException.java       -> 400
    └── ApiExceptionHandler.java         @RestControllerAdvice
```

Les données sont stockées **en mémoire** (aucune base de données) : le parc est
réinitialisé à chaque redémarrage avec 5 voitures.

## Choix techniques

- **Une seule méthode PUT** gère la location et le retour. Deux `@PutMapping`
  sur la même URI provoqueraient une erreur de mapping ambigu au démarrage ;
  le paramètre `rent` fait l'aiguillage et `@RequestBody(required = false)`
  rend le corps optionnel (inutile pour un retour).
- **Dates en `String`** : le format du sujet (`11/11/2017`) n'est pas l'ISO
  attendu par défaut pour un `LocalDate`. Le format est tout de même validé
  dans `CarService` via un `DateTimeFormatter` (`d/M/uuuu`).
- Les méthodes PUT renvoient la voiture mise à jour (le sujet propose `void`) :
  c'est plus pratique pour vérifier le résultat dans Postman.
- Les plaques sont insensibles à la casse.

## Tests

```bash
./gradlew test
```

`CarServiceTests` couvre la location, le retour, la double location, le retour
d'une voiture non louée, la plaque inconnue et la validation des dates.
