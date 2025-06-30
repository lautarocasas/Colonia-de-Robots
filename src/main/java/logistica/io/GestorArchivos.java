package main.java.logistica.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.RobotLogistico;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.SistemaLogisticoBuilder;
import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.cofres.CofreFactory;
import main.java.coloniaDeRobots.util.ResultadoConectividad;
import main.java.coloniaDeRobots.util.ValidadorConectividad;
import main.java.coloniaDeRobots.util.ValidadorFactibilidad;
import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.excepciones.UbicacionDuplicadaException;
import main.java.logistica.factory.RobopuertoFactory;
import main.java.logistica.factory.RobotFactory;

/**
 * Carga y valida toda la configuración inicial desde un archivo JSON,
 * construyendo la red de cofres, robopuertos y robots.
 */
public class GestorArchivos {
	private static final Logger LOGGER = Logger.getLogger(GestorArchivos.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();
    private final CofreFactory cofreFactory = new CofreFactory();
    private final RobopuertoFactory robopuertoFactory = new RobopuertoFactory();

    public SistemaLogistico cargarDesdeArchivo(String rutaArchivo) throws ExcepcionLogistica {
        try {
            JsonNode root = mapper.readTree(new File(rutaArchivo));

            if (!root.has("robopuertos") || !root.has("cofres") || !root.has("robots")) {
                throw new EstructuraInvalidaException(
                    "Faltan secciones obligatorias: robopuertos, cofres o robots", null);
            }

            // 1. Parsear robopuertos
            List<Robopuerto> robopuertos = cargarRobopuertos(root.get("robopuertos"));

            // 2. Parsear cofres
            List<Cofre> cofres = cargarCofres(root.get("cofres"));

            // 3. Validar conectividad y filtrar cofres inaccesibles
            ResultadoConectividad cr = ValidadorConectividad.validarConectividad(cofres, robopuertos);
            if (!cr.cofresInaccesibles.isEmpty()) {
                LOGGER.warning(() -> String.format(
                    "Se omitiran %d cofres fuera de cobertura: %s",
                    cr.cofresInaccesibles.size(), cr.cofresInaccesibles));
            }
            cofres = cr.cofresAccesibles;

            // 4. Validación de factibilidad de solicitudes
            ValidadorFactibilidad.validarFactibilidad(cofres);
            LOGGER.info(() -> "Todas las solicitudes tienen al menos un proveedor potencial.");

            // 5. Parsear robots
            List<RobotLogistico> robots = cargarRobots(root.get("robots"), robopuertos);

            // 5. Validar ubicaciones duplicadas
            validarUbicaciones(cofres, robopuertos);

            // 6. Construir sistema
            SistemaLogistico sistema = new SistemaLogisticoBuilder()
            	    .withFactorConsumo(1.0)
            	    .addCofres(cofres)
            	    .addRobopuertos(robopuertos)
            	    .addRobots(robots)
            	    .build();

            return sistema;

        } catch (ExcepcionLogistica e) {
            throw e;
        } catch (Exception e) {
            throw new ExcepcionLogistica("Error general durante la carga: " + e.getMessage(), e);
        }
    }

    private List<Robopuerto> cargarRobopuertos(JsonNode array) throws ExcepcionLogistica {
        List<Robopuerto> list = new ArrayList<>();
        for (JsonNode node : array) {
            list.add(robopuertoFactory.crearDesdeJson(node));
        }
        return list;
    }

    private List<Cofre> cargarCofres(JsonNode array) throws ExcepcionLogistica {
        List<Cofre> list = new ArrayList<>();
        for (JsonNode node : array) {
            list.add(cofreFactory.crearCofreDesdeJson(node));
        }
        return list;
    }

    private List<RobotLogistico> cargarRobots(JsonNode array, List<Robopuerto> robopuertos)
            throws ExcepcionLogistica {
        RobotFactory rf = new RobotFactory(robopuertos);
        List<RobotLogistico> list = new ArrayList<>();
        for (JsonNode node : array) {
            list.add(rf.crearDesdeJson(node));
        }
        return list;
    }

    private void validarUbicaciones(List<Cofre> cofres, List<Robopuerto> rps)
            throws UbicacionDuplicadaException {
        Set<String> seen = new HashSet<>();
        for (Cofre c : cofres) {
            String key = c.getUbicacion().toString();
            if (!seen.add(key)) {
                throw new UbicacionDuplicadaException("Ubicación duplicada entre cofres: " + key);
            }
        }
        for (Robopuerto rp : rps) {
            String key = rp.getUbicacion().toString();
            if (!seen.add(key)) {
                throw new UbicacionDuplicadaException("Ubicación duplicada entre cofre y robopuerto: " + key);
            }
        }
    }
}
