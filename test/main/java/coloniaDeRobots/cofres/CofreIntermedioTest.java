package main.java.coloniaDeRobots.cofres;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;

class CofreIntermedioTest {
    private SistemaLogistico system;
    private CofreIntermedio inter;
    private Item item;

    @BeforeEach
    void setup() {
        system = new SistemaLogistico(1.0);
        item = new Item("Z");
        inter = new CofreIntermedio(new Ubicacion(0,0), Map.of(item, 1), Map.of(item, 2));
        system.agregarCofre(inter);
    }

    @Test
    void firstPhaseRegistersSolicitud() {
        inter.accionar(system);
        List<Solicitud> pend = system.obtenerSolicitudesPendientes();
        assertEquals(1, pend.size());
        assertEquals(2, pend.get(0).getCantidadPendiente());
    }

    @Test
    void noNewSolicitudAfterComplete() {
        Solicitud s = new Solicitud(inter, item, 2);
        system.registrarSolicitud(s);
        s.registrarEntrega(2);
        inter.accionar(system);
        assertTrue(system.obtenerSolicitudesPendientes().isEmpty());
    }
}
