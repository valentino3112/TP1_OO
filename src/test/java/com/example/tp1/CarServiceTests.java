package com.example.tp1;

import com.example.tp1.exception.CarNotAvailableException;
import com.example.tp1.exception.CarNotFoundException;
import com.example.tp1.exception.InvalidDatesException;
import com.example.tp1.model.Car;
import com.example.tp1.model.Dates;
import com.example.tp1.service.CarService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests unitaires de la logique metier (sans demarrer Spring). */
class CarServiceTests {

    private final CarService service = new CarService();

    @Test
    void allCarsAreAvailableAtStartup() {
        assertEquals(service.findAll().size(), service.findAvailable().size());
    }

    @Test
    void rentThenGiveBack() {
        Car car = service.rent("11AA22", new Dates("11/11/2017", "1/1/2018"));
        assertTrue(car.isRented());
        assertEquals("11/11/2017", car.getBegin());
        assertFalse(service.findAvailable().contains(car));

        service.giveBack("11AA22");
        assertFalse(car.isRented());
        assertTrue(service.findAvailable().contains(car));
    }

    @Test
    void cannotRentTwice() {
        service.rent("AA11BB", new Dates("1/1/2024", "2/1/2024"));
        assertThrows(CarNotAvailableException.class,
                () -> service.rent("AA11BB", new Dates("1/1/2024", "2/1/2024")));
    }

    @Test
    void cannotGiveBackACarThatIsNotRented() {
        assertThrows(CarNotAvailableException.class, () -> service.giveBack("BB22CC"));
    }

    @Test
    void unknownPlateNumber() {
        assertThrows(CarNotFoundException.class, () -> service.findByPlateNumber("ZZ99ZZ"));
    }

    @Test
    void datesAreRequiredAndValidated() {
        assertThrows(InvalidDatesException.class, () -> service.rent("CC33DD", null));
        assertThrows(InvalidDatesException.class, () -> service.rent("CC33DD", new Dates("hier", "demain")));
        assertThrows(InvalidDatesException.class, () -> service.rent("CC33DD", new Dates("1/1/2024", "1/1/2023")));
    }

    @Test
    void plateNumberIsCaseInsensitive() {
        assertEquals("11AA22", service.findByPlateNumber("11aa22").getPlateNumber());
    }
}
