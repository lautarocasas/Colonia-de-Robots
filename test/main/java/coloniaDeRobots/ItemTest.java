package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ItemTest {
	@Test
	void testEqualsAndHashCodeAndToString() {
		Item a = new Item("foo");
		Item b = new Item("foo");
		Item c = new Item("bar");
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a, c);
		assertEquals("foo", a.toString());
	}

	@Test
	void testNullNameThrows() {
		assertThrows(NullPointerException.class, () -> new Item(null));
	}
}
