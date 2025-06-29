package main.java.logistica.factory;

import com.fasterxml.jackson.databind.JsonNode;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.excepciones.ValorInvalidoException;

/**
 * Fabrica para crear instancias de Robopuerto a partir de JSON.
 */
public class RobopuertoFactory {
    /**
     * Crea un Robopuerto leyendo atributos "x", "y" y "alcance" del nodo JSON.
     * @param node JsonNode que debe contener los atributos obligatorios.
     * @return nueva instancia de Robopuerto.
     * @throws ExcepcionLogistica si falta algun campo o tiene valor inválido.
     */
    public Robopuerto crearDesdeJson(JsonNode node) throws ExcepcionLogistica {
        if (!node.has("x") || !node.has("y") || !node.has("alcance")) {
            throw new EstructuraInvalidaException("Robopuerto sin campo 'x', 'y' o 'alcance'.", null);
        }
        int x = node.get("x").asInt();
        int y = node.get("y").asInt();
        double alcance = node.get("alcance").asDouble(Double.NaN);
        if (Double.isNaN(alcance) || alcance <= 0) {
            throw new ValorInvalidoException(
                String.format("Alcance inválido para robopuerto en (%d,%d): %s", x, y, node.get("alcance").asText()));
        }
        return new Robopuerto(new Ubicacion(x, y), alcance);
    }
}