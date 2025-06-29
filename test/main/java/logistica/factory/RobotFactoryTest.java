package main.java.logistica.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.RobotLogistico;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.RobotFueraDeCoberturaException;
import main.java.logistica.excepciones.ValorInvalidoException;

public class RobotFactoryTest {
    private static ObjectMapper mapper;
    private static RobotFactory factory;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
        // create a Robopuerto covering origin
        Robopuerto rp = new Robopuerto(new Ubicacion(0,0), 10.0);
        factory = new RobotFactory(List.of(rp));
    }

    @Test
    void testCreateValidRobot() throws Exception {
        String json = "{ \"x\": 3, \"y\": 4, \"bateria\": 20, \"capacidadCarga\": 5 }";
        JsonNode node = mapper.readTree(json);
        RobotLogistico r = factory.crearDesdeJson(node);
        assertEquals(5, r.getCapacidadCarga());
        assertTrue(r.tieneBateriaPara(4.0, 5.0));
    }

    @Test
    void testMissingFieldsThrows() throws Exception {
        String json = "{ \"x\": 0, \"y\": 0, \"bateria\": 10 }";
        JsonNode node = mapper.readTree(json);
        assertThrows(EstructuraInvalidaException.class, () -> factory.crearDesdeJson(node));
    }

    @Test
    void testInvalidValuesThrows() throws Exception {
        String json = "{ \"x\": 0, \"y\": 0, \"bateria\": 0, \"capacidadCarga\": -1 }";
        JsonNode node = mapper.readTree(json);
        assertThrows(ValorInvalidoException.class, () -> factory.crearDesdeJson(node));
    }

    @Test
    void testOutOfCoverageThrows() throws Exception {
        // create a factory with a robopuerto far away
        Robopuerto rp = new Robopuerto(new Ubicacion(0,0), 5.0);
        RobotFactory localFactory = new RobotFactory(List.of(rp));
        String json = "{ \"x\": 10, \"y\": 10, \"bateria\": 10, \"capacidadCarga\": 2 }";
        JsonNode node = mapper.readTree(json);
        assertThrows(RobotFueraDeCoberturaException.class, () -> localFactory.crearDesdeJson(node));
    }
}
