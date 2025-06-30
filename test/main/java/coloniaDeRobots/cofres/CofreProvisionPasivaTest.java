package main.java.coloniaDeRobots.cofres;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Ubicacion;

class CofreProvisionPasivaTest {
    private SistemaLogistico system;
    private CofreProvisionPasiva pasiva;
    private Item item;

    @BeforeEach
    void setup() {
        system = new SistemaLogistico(1.0);
        item = new Item("X");
        pasiva = new CofreProvisionPasiva(new Ubicacion(0,0), Map.of(item, 2));
        system.agregarCofre(pasiva);
    }

    @Test
    void actionDoesNothing() {
        assertTrue(system.obtenerSolicitudesPendientes().isEmpty());
        pasiva.accionar(system);
        assertTrue(system.obtenerSolicitudesPendientes().isEmpty());
    }
}
