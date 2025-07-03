package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;

class SistemaLogisticoTest {
	private SistemaLogistico sistema;
	private CofreAlmacenamiento cofre;
	private Item item;

	@BeforeEach
	void setup() {
		sistema = new SistemaLogistico(1.0);
		cofre = new CofreAlmacenamiento(new Ubicacion(0, 0), Map.of());
		item = new Item("X");
		sistema.agregarCofre(cofre);
	}

	@Test
	void testTransporteCompletaSolicitud() {
		Solicitud s1 = new Solicitud(cofre, item, 3);
		sistema.registrarSolicitud(s1);
		sistema.generarTransporte(cofre, cofre, item, 3, s1);
		assertTrue(s1.estaCompletada());
		assertTrue(sistema.obtenerSolicitudesPendientes(cofre).isEmpty());
	}
}