package main.java.logistica.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.RobotLogistico;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;
import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.RobotFueraDeCoberturaException;
import main.java.logistica.excepciones.ValorInvalidoException;

/**
 * Tests para RobotFactory, ahora con inyección de cofres y robopuertos.
 */
class RobotFactoryTest {
	private static ObjectMapper mapper;
	private static RobotFactory factory;

	@BeforeAll
	static void setup() {
		mapper = new ObjectMapper();
		// Robopuerto que cubre (0,0)
		Robopuerto rp = new Robopuerto(new Ubicacion(0, 0), 10.0);
		// La fábrica ahora recibe ambos: puertos y cofres (vacío)
		factory = new RobotFactory(List.of(rp), List.of(new CofreAlmacenamiento(new Ubicacion(0, 0), Map.of())));
	}

	@Test
	void creaRobotValido() throws Exception {
		String json = "{ \"x\": 3, \"y\": 4, \"bateria\": 20, \"capacidadCarga\": 5 }";
		JsonNode node = mapper.readTree(json);
		RobotLogistico r = factory.crearDesdeJson(node);
		assertEquals(5, r.getCapacidadCarga(), "Capacidad de carga incorrecta");
		assertTrue(r.getBateriaActual() == 20.0, "Batería inicial debe ser igual al valor JSON");
	}

	@Test
	void lanzaEstructuraInvalidaSiFaltanCampos() throws Exception {
		String json = "{ \"x\": 0, \"y\": 0, \"bateria\": 10 }"; // falta capacidadCarga
		JsonNode node = mapper.readTree(json);
		assertThrows(EstructuraInvalidaException.class, () -> factory.crearDesdeJson(node),
				"Debe lanzar EstructuraInvalidaException si hay campos faltantes");
	}

	@Test
	void lanzaValorInvalidoSiParametrosInvalidos() throws Exception {
		String json = "{ \"x\": 0, \"y\": 0, \"bateria\": 0, \"capacidadCarga\": -1 }";
		JsonNode node = mapper.readTree(json);
		assertThrows(ValorInvalidoException.class, () -> factory.crearDesdeJson(node),
				"Debe lanzar ValorInvalidoException si batería o carga <= 0");
	}

	@Test
	void lanzaFueraDeCoberturaSiSaleDeAlcance() throws Exception {
		// Fábrica con robopuerto de alcance pequeño
		Robopuerto rp = new Robopuerto(new Ubicacion(0, 0), 5.0);
		RobotFactory localFactory = new RobotFactory(List.of(rp), List.of());
		String json = "{ \"x\": 10, \"y\": 10, \"bateria\": 10, \"capacidadCarga\": 2 }";
		JsonNode node = mapper.readTree(json);
		assertThrows(RobotFueraDeCoberturaException.class, () -> localFactory.crearDesdeJson(node),
				"Debe lanzar RobotFueraDeCoberturaException si el robot inicia fuera de cobertura");
	}
}
