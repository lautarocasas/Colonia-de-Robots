package main.java.coloniaDeRobots.cofres;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.Ubicacion;

class CofreAlmacenamientoTest {
	private CofreAlmacenamiento cofre;
	private Item item;

	@BeforeEach
	void setup() {
		cofre = new CofreAlmacenamiento(new Ubicacion(0, 0), Map.of());
		item = new Item("Y");
	}

	@Test
	void testAddAndRemoveItem() {
		cofre.agregarItem(item, 5);
		assertEquals(5, cofre.getCantidadItem(item));
		assertTrue(cofre.retirarItem(item, 3));
		assertEquals(2, cofre.getCantidadItem(item));
		assertFalse(cofre.retirarItem(item, 5)); // No me deberia dejar retirar
		assertEquals(2, cofre.getCantidadItem(item));
		assertTrue(cofre.retirarItem(item, 2));
		assertEquals(0, cofre.getCantidadItem(item));
	}

	@Test
	void testGetInventarioUnmodifiable() {
		Map<Item, Integer> inv = cofre.getInventario();
		assertThrows(UnsupportedOperationException.class, () -> inv.put(item, 1));
	}
}
