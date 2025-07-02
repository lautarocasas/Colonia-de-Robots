package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RobopuertoTest {
    @Test
    void testCoverageBoundary() {
        Ubicacion center = new Ubicacion(0,0);
        Robopuerto rp = new Robopuerto(center, 5.0);
        assertTrue(rp.cubre(new Ubicacion(5,0)));
        assertTrue(rp.cubre(new Ubicacion(3,4)));
        assertFalse(rp.cubre(new Ubicacion(5.1, 0)));
    }

    @Test
    void testInvalidAlcanceThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Robopuerto(new Ubicacion(0,0), 0));
    }
}