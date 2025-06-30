package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ElementoLogisticoTest {
    @Test
    void getUbicacionDevuelveLoMismo() {
        Ubicacion u = new Ubicacion(5, 7);
        ElementoLogistico el = new Robopuerto(u, 8);
        assertEquals(u, el.getUbicacion());
    }
}
