package main.java.logistica.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.RobotLogistico;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.cofres.CofreFactory;
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

            // 1. Parseo de robopuertos
            List<Robopuerto> robopuertos = cargarRobopuertos(root.get("robopuertos"));

            // 2. Parseo de cofres
            List<Cofre> cofres = cargarCofres(root.get("cofres"));

            // 3. Parseo de robots
            List<RobotLogistico> robots = cargarRobots(root.get("robots"), robopuertos);

            // 4. Validación de ubicaciones duplicadas
            validarUbicaciones(cofres, robopuertos);

            // 5. Construcción del sistema logístico
            SistemaLogistico sistema = new SistemaLogistico(/* factorConsumoGlobal */ 1.0);
            cofres.forEach(sistema::agregarCofre);
            robopuertos.forEach(sistema::agregarRobopuerto);
            robots.forEach(sistema::agregarRobot);
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
