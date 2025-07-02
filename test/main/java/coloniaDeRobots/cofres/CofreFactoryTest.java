package main.java.coloniaDeRobots.cofres;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.TipoCofreDesconocidoException;
import main.java.logistica.factory.CofreFactory;

class CofreFactoryTest {
    private static ObjectMapper mapper;
    private static CofreFactory factory;

    @BeforeAll
    static void init() {
        mapper = new ObjectMapper();
        factory = new CofreFactory();
    }

    @Test
    void testProvisionActiva() throws Exception {
        String json = "{ \"tipo\": \"PROVISION_ACTIVA\", \"x\":1, \"y\":2, \"inventario\": { \"foo\": 3 } }";
        JsonNode node = mapper.readTree(json);
        assertTrue(factory.crearCofreDesdeJson(node) instanceof CofreProvisionActiva);
    }

    @Test
    void testProvisionPasiva() throws Exception {
        String json = "{ \"tipo\": \"PROVISION_PASIVA\", \"x\":0, \"y\":0, \"inventario\": {} }";
        JsonNode node = mapper.readTree(json);
        assertTrue(factory.crearCofreDesdeJson(node) instanceof CofreProvisionPasiva);
    }

    @Test
    void testAlmacenamiento() throws Exception {
        String json = "{ \"tipo\": \"ALMACENAMIENTO\", \"x\":5, \"y\":5, \"inventario\": {} }";
        JsonNode node = mapper.readTree(json);
        assertTrue(factory.crearCofreDesdeJson(node) instanceof CofreAlmacenamiento);
    }

    @Test
    void testSolicitudAndIntermedio() throws Exception {
        String sol = "{ \"tipo\": \"SOLICITUD\", \"x\":2, \"y\":2, \"solicitudes\": { \"bar\": 4 } }";
        JsonNode n1 = mapper.readTree(sol);
        assertTrue(factory.crearCofreDesdeJson(n1) instanceof CofreSolicitud);

        String inter = "{ \"tipo\": \"INTERMEDIO\", \"x\":3, \"y\":3, \"inventario\": {\"bar\":1}, \"solicitudes\": { \"bar\": 2 } }";
        JsonNode n2 = mapper.readTree(inter);
        assertTrue(factory.crearCofreDesdeJson(n2) instanceof CofreIntermedio);
    }

    @Test
    void testMissingTipo() throws Exception {
        String json = "{ \"x\":1 }";
        JsonNode node = mapper.readTree(json);
        assertThrows(EstructuraInvalidaException.class, () -> factory.crearCofreDesdeJson(node));
    }

    @Test
    void testUnknownTipo() throws Exception {
        String json = "{ \"tipo\": \"XYZ\", \"x\":1, \"y\":1 }";
        JsonNode node = mapper.readTree(json);
        assertThrows(TipoCofreDesconocidoException.class, () -> factory.crearCofreDesdeJson(node));
    }
}

