package main.java.coloniaDeRobots.cofres;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;

/**
 * Tests en Español para CofreSolicitud
 */
class CofreSolicitudTest {
	private SistemaLogistico system;
	private CofreSolicitud solCofre;
	private Item item;

	@BeforeEach
	void setup() {
		system = new SistemaLogistico(1.0);
		item = new Item("I");
		// El mapa de solicitudes pide 3 unidades de item
		solCofre = new CofreSolicitud(new Ubicacion(0, 0), Map.of(), Map.of(item, 3));
		system.agregarCofre(solCofre);
	}

	@Test
	void registersNewSolicitudSiNoExistia() {
		solCofre.accionar(system);
		List<Solicitud> pend = system.obtenerSolicitudesPendientes();
		assertEquals(1, pend.size(), "Debe registrar una nueva solicitud");
		assertEquals(3, pend.get(0).getCantidadPendiente(), "Cantidad pendiente debe ser 3");
	}

}
