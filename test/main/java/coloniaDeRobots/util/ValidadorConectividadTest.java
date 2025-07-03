package main.java.coloniaDeRobots.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;

class ValidadorConectividadTest {
	@Test
	void cofreConPuertoCercanoEsAccesible() {
		var puerto = new Robopuerto(new Ubicacion(0, 0), 5.0);
		var cofre = new CofreAlmacenamiento(new Ubicacion(3, 4), Map.of());
		ResultadoConectividad res = ValidadorConectividad.validarConectividad(List.of(cofre), List.of(puerto));
		assertTrue(res.cofresAccesibles.contains(cofre));
		assertTrue(res.cofresInaccesibles.isEmpty());
	}

	@Test
	void cofreLejanoEsInaccesible() {
		var puerto = new Robopuerto(new Ubicacion(0, 0), 5.0);
		var cofre = new CofreAlmacenamiento(new Ubicacion(10, 10), Map.of());
		ResultadoConectividad res = ValidadorConectividad.validarConectividad(List.of(cofre), List.of(puerto));
		assertTrue(res.cofresInaccesibles.contains(cofre));
		assertTrue(res.cofresAccesibles.isEmpty());
	}

	@Test
	void cofresConectadosTransitivamenteSonAccesibles() {
		var puerto1 = new Robopuerto(new Ubicacion(0, 0), 5.0);
		var puerto2 = new Robopuerto(new Ubicacion(8, 0), 5.0);
		var cofre1 = new CofreAlmacenamiento(new Ubicacion(3, 0), Map.of());
		var cofre2 = new CofreAlmacenamiento(new Ubicacion(11, 0), Map.of());
		ResultadoConectividad res = ValidadorConectividad.validarConectividad(List.of(cofre1, cofre2),
				List.of(puerto1, puerto2));
		assertTrue(res.cofresAccesibles.containsAll(List.of(cofre1, cofre2)));
	}
}
