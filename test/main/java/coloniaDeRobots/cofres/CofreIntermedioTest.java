package main.java.coloniaDeRobots.cofres;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private SistemaLogistico sistema;
    private CofreIntermedio intermedio;
    private Item item;

    @BeforeEach
    void configurar() {
        sistema = new SistemaLogistico(1.0);
        item = new Item("Z");
        // Inventario inicial con 1 unidad de "Z"
        Map<Item,Integer> inventario = Map.of(item, 1);
        // Solicita 2 unidades de "Z"
        Map<Item,Integer> solicitudes = Map.of(item, 2);
        intermedio = new CofreIntermedio(
            new Ubicacion(0,0), inventario, solicitudes
        );
        sistema.agregarCofre(intermedio);
    }

    @Test
    void registraSolicitudInicial() {
        // Primera fase: debe registrar la solicitud pendiente
        intermedio.accionar(sistema);
        List<Solicitud> pendientes = sistema.obtenerSolicitudesPendientes();
        assertEquals(1, pendientes.size(), "Debe registrar una solicitud");
        assertEquals(2, pendientes.get(0).getCantidadPendiente(),
            "Cantidad pendiente debe ser 2");
    }

    @Test
    void noRegistraNuevaSolicitudSiYaEstaCompleta() {
        // Simular solicitud completa antes de accionar
        Solicitud previa = new Solicitud(intermedio, item, 2);
        sistema.registrarSolicitud(previa);
        previa.registrarEntrega(2); // completa la solicitud

        intermedio.accionar(sistema);
        List<Solicitud> pendientes = sistema.obtenerSolicitudesPendientes();
        assertTrue(pendientes.isEmpty(),
            "No debe registrar nueva solicitud si ya está completa");
    }

    @Test
    void getSolicitudesDevuelveMapaInmutableYContenidoCorrecto() {
        Map<Item,Integer> mapa = intermedio.getSolicitudes();
        assertEquals(1, mapa.size(), "Debe contener 1 tipo de solicitud");
        assertTrue(mapa.containsKey(item), "Debe contener el ítem solicitado");
        assertEquals(2, mapa.get(item).intValue(), "Cantidad solicitada debe ser 2");
        // Verificar inmutabilidad del mapa
        assertThrows(UnsupportedOperationException.class, () -> mapa.put(item, 5));
    }
}
