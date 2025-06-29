package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UbicacionTest {
    @Test
    void testDistanceSymmetry() {
        Ubicacion p1 = new Ubicacion(0, 0);
        Ubicacion p2 = new Ubicacion(3, 4);
        assertEquals(5.0, p1.distanciaA(p2), 1e-9);
        assertEquals(p1.distanciaA(p2), p2.distanciaA(p1), 1e-9);
    }

    @Test
    void testToStringFormat() {
        Ubicacion p = new Ubicacion(1.2345, 6.789);
        assertEquals("(1,23, 6,79)", p.toString());
    }

    @Test
    void testZeroDistance() {
        Ubicacion p = new Ubicacion(2.0, -3.0);
        assertEquals(0.0, p.distanciaA(p), 1e-9);
    }
}