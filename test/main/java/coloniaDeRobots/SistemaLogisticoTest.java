package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;

class SistemaLogisticoTest {
    private SistemaLogistico sistema;
    private CofreAlmacenamiento cofre;
    private Item item;

    @BeforeEach
    void setup() {
        sistema = new SistemaLogistico(1.0);
        cofre = new CofreAlmacenamiento(new Ubicacion(0,0), Map.of());
        item = new Item("X");
        sistema.agregarCofre(cofre);
    }

    @Test
    void testRegistrarAndRetrieveSolicitudes() {
        Solicitud s1 = new Solicitud(cofre, item, 3);
        sistema.registrarSolicitud(s1);
        List<Solicitud> pending = sistema.obtenerSolicitudesPendientes(cofre);
        assertEquals(1, pending.size());
        assertEquals(s1, pending.get(0));
    }

    @Test
    void testGenerateTransporteCompletesSolicitud() {
        // destinaton is same cofre for simplicity
        Solicitud s1 = new Solicitud(cofre, item, 3);
        sistema.registrarSolicitud(s1);
        sistema.generarTransporte(cofre, cofre, item, 3);
        assertTrue(s1.estaCompletada());
        assertTrue(sistema.obtenerSolicitudesPendientes(cofre).isEmpty());
    }

    @Test
    void testGenerateTransportePartial() {
        Solicitud s1 = new Solicitud(cofre, item, 5);
        sistema.registrarSolicitud(s1);
        sistema.generarTransporte(cofre, cofre, item, 2);
        assertFalse(s1.estaCompletada());
        assertEquals(3, s1.getCantidadPendiente());
        assertEquals(1, sistema.obtenerSolicitudesPendientes(cofre).size());
    }
}