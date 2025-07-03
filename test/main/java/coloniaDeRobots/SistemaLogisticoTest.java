package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;
import main.java.coloniaDeRobots.cofres.CofreSolicitud;

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
    void testTransporteCompletaSolicitud() {
        // destinaton is same cofre for simplicity
        Solicitud s1 = new Solicitud(cofre, item, 3);
        sistema.registrarSolicitud(s1);
        sistema.generarTransporte(cofre, cofre, item, 3, s1);
        assertTrue(s1.estaCompletada());
        assertTrue(sistema.obtenerSolicitudesPendientes(cofre).isEmpty());
    }

    @Test
    void testGenerateTransporteParcial() {
        Solicitud s1 = new Solicitud(cofre, item, 5);
        sistema.registrarSolicitud(s1);
        sistema.generarTransporte(cofre, cofre, item, 2, s1);
        assertFalse(s1.estaCompletada());
        assertEquals(3, s1.getCantidadPendiente());
        assertEquals(1, sistema.obtenerSolicitudesPendientes(cofre).size());
    }
}

class SistemaLogisticoMetodosTest {

    private SistemaLogistico sistema;
    private CofreAlmacenamiento almacenamiento;
    private CofreSolicitud cofreSolicitaA;
    private CofreSolicitud cofreSolicitaB;
    private Item itemA;
    private Item itemB;

    @BeforeEach
    void configurarEscenario() {
        // Factor de consumo indiferente aquí
        sistema = new SistemaLogistico(1.0);

        // Cofres
        almacenamiento = new CofreAlmacenamiento(new Ubicacion(0, 0), Map.of());
        cofreSolicitaA = new CofreSolicitud(
            new Ubicacion(1, 0), Map.of(), Map.of());
        cofreSolicitaB = new CofreSolicitud(
            new Ubicacion(2, 0), Map.of(), Map.of());

        itemA = new Item("A");
        itemB = new Item("B");

        // Registro de cofres en el sistema
        sistema.agregarCofre(almacenamiento);
        sistema.agregarCofre(cofreSolicitaA);
        sistema.agregarCofre(cofreSolicitaB);
    }

    @Test
    void testGetCantidadRecibidaVariasSolicitudes() {
        // Creamos dos solicitudes al mismo cofreSolicitaA, distinto ítem
        Solicitud sol1 = new Solicitud(cofreSolicitaA, itemA, 5);
        Solicitud sol2 = new Solicitud(cofreSolicitaA, itemA, 3);
        Solicitud sol3 = new Solicitud(cofreSolicitaB, itemA, 10);
        sistema.registrarSolicitud(sol1);
        sistema.registrarSolicitud(sol2);
        sistema.registrarSolicitud(sol3);

        // Simulamos entregas parciales
        sol1.registrarEntrega(2); // sol1 recibido 2/5
        sol2.registrarEntrega(3); // sol2 recibido 3/3 (completa)
        sol3.registrarEntrega(4); // sol3 recibido 4/10

        // Debe sumar sólo sobresol1 y sol2 para cofreSolicitaA/itemA
        int recibidosEnA = sistema.getCantidadRecibida(cofreSolicitaA, itemA);
        assertEquals(5, recibidosEnA,
            "La suma de cantidades recibidas en cofre A debe ser 2+3=5");

        // Y en B sólo sol3
        int recibidosEnB = sistema.getCantidadRecibida(cofreSolicitaB, itemA);
        assertEquals(4, recibidosEnB,
            "La suma de cantidades recibidas en cofre B debe ser 4");
    }

    @Test
    void testObtenerSolicitudesPendientesPorCofre() {
        // Solicitamos 3 unidades de A en cofreSolicitaA
        Solicitud sA1 = new Solicitud(cofreSolicitaA, itemA, 3);
        // Y 2 unidades de B en cofreSolicitaB
        Solicitud sB1 = new Solicitud(cofreSolicitaB, itemB, 2);

        sistema.registrarSolicitud(sA1);
        sistema.registrarSolicitud(sB1);

        // Completamos sA1
        sA1.registrarEntrega(3);

        // Ahora pedimos el listado de pendientes por origen
        List<Solicitud> pendientesA = sistema.obtenerSolicitudesPendientes(cofreSolicitaA);
        List<Solicitud> pendientesB = sistema.obtenerSolicitudesPendientes(cofreSolicitaB);

        // Cofre A no debe tener pendientes (se completó)
        assertTrue(pendientesA.isEmpty(),
            "No debe quedar ninguna solicitud pendiente en el cofre A");

        // Cofre B sí debe devolver la suya
        assertEquals(1, pendientesB.size(),
            "Debe devolver 1 solicitud pendiente para el cofre B");
        assertEquals(sB1, pendientesB.get(0),
            "La solicitud pendiente debe ser exactamente sB1");
    }
}