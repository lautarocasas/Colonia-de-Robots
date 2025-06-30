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

/**
 * Tests para CofreProvisionActiva, incluyendo interacción con SistemaLogistico
 */
class CofreProvisionActivaTest {
    private SistemaLogistico sistema;
    private CofreProvisionActiva activa;
    private CofreSolicitud solCofre;
    private Item item;

    @BeforeEach
    void setup() {
        sistema = new SistemaLogistico(1.0);
        item = new Item("A");
        activa = new CofreProvisionActiva(new Ubicacion(0,0), Map.of(item, 10));
        solCofre = new CofreSolicitud(new Ubicacion(1,0), Map.of(), Map.of(item, 5));
        sistema.agregarCofre(activa);
        sistema.agregarCofre(solCofre);
    }

    @Test
    void entregaCantidadExacta_y_actualizaEstadoYSolicitudes() {
        // Registrar solicitud inicial
        solCofre.accionar(sistema);
        // Ejecutar acción de provisión activa
        activa.accionar(sistema);
        // Ya no debe haber solicitudes pendientes para solCofre
        List<Solicitud> pendientes = sistema.obtenerSolicitudesPendientes();
        assertTrue(pendientes.isEmpty(), "No debe quedar solicitud pendiente tras entrega completa");
        // Y la cantidad recibida debe ser exactamente 5
        int recibidos = sistema.getCantidadRecibida(solCofre, item);
        assertEquals(5, recibidos, "Debe registrar 5 unidades entregadas");
    }

    @Test
    void entregaParcialSiNoHayStockSuficiente_y_dejaPendiente() {
        // Reducir stock a 2 unidades
        activa = new CofreProvisionActiva(new Ubicacion(0,0), Map.of(item, 2));
        sistema = new SistemaLogistico(1.0);
        sistema.agregarCofre(activa);
        solCofre = new CofreSolicitud(new Ubicacion(1,0), Map.of(), Map.of(item, 5));
        sistema.agregarCofre(solCofre);

        solCofre.accionar(sistema);
        activa.accionar(sistema);

        // Debe quedar pendiente la diferencia 5-2 = 3
        List<Solicitud> pendientes = sistema.obtenerSolicitudesPendientes();
        assertEquals(1, pendientes.size(), "Debe quedar una solicitud pendiente");
        assertEquals(3, pendientes.get(0).getCantidadPendiente(),
            "La cantidad pendiente debe ser 3 tras entrega parcial");
        // Y getCantidadRecibida refleja 2 entregadas
        int recibidos = sistema.getCantidadRecibida(solCofre, item);
        assertEquals(2, recibidos, "Debe registrar 2 unidades entregadas");
    }
}