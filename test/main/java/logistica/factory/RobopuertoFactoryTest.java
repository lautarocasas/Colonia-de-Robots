package main.java.logistica.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.ValorInvalidoException;

public class RobopuertoFactoryTest {
	private static ObjectMapper mapper;
	private static RobopuertoFactory factory;

	@BeforeAll
	static void setup() {
		mapper = new ObjectMapper();
		factory = new RobopuertoFactory();
	}

	@Test
	void testCreateValidRobopuerto() throws Exception {
		String json = "{ \"x\": 1, \"y\": 2, \"alcance\": 5.5 }";
		JsonNode node = mapper.readTree(json);
		Robopuerto rp = factory.crearDesdeJson(node);
		assertEquals(5.5, rp.getAlcance(), 1e-9);
		assertEquals(new Ubicacion(1, 2).toString(), rp.getUbicacion().toString());
	}

	@Test
	void testMissingFieldsThrows() throws Exception {
		String json = "{ \"x\": 1 }";
		JsonNode node = mapper.readTree(json);
		assertThrows(EstructuraInvalidaException.class, () -> factory.crearDesdeJson(node));
	}

	@Test
	void testNegativeAlcanceThrows() throws Exception {
		String json = "{ \"x\": 0, \"y\": 0, \"alcance\": -3.0 }";
		JsonNode node = mapper.readTree(json);
		assertThrows(ValorInvalidoException.class, () -> factory.crearDesdeJson(node));
	}
}
