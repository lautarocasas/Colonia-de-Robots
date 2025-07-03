package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;

class SolicitudTest {
	private CofreAlmacenamiento dummyCofre;
	private Item item;

	@BeforeEach
	void setup() {
		dummyCofre = new CofreAlmacenamiento(new Ubicacion(0, 0), Map.of());
		item = new Item("X");
	}

	@Test
	void testCreationAndProperties() {
		Solicitud s = new Solicitud(dummyCofre, item, 10);
		assertEquals(item, s.getItem());
		assertEquals(10, s.getCantidadTotal());
		assertEquals(10, s.getCantidadPendiente());
		assertFalse(s.estaCompletada());
	}

	@Test
	void testInvalidTotalThrows() {
		assertThrows(IllegalArgumentException.class, () -> new Solicitud(dummyCofre, item, 0));
	}

	@Test
	void testRegistrarEntregaPartialAndComplete() {
		Solicitud s = new Solicitud(dummyCofre, item, 5);
		s.registrarEntrega(2);
		assertEquals(3, s.getCantidadPendiente());
		assertFalse(s.estaCompletada());

		s.registrarEntrega(3);
		assertEquals(0, s.getCantidadPendiente());
		assertTrue(s.estaCompletada());

		// Further deliveries should have no effect
		s.registrarEntrega(10);
		assertEquals(0, s.getCantidadPendiente());
	}
}
