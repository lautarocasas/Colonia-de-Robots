package main.java.logistica.factory;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.RobotLogistico;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.excepciones.RobotFueraDeCoberturaException;
import main.java.logistica.excepciones.ValorInvalidoException;

/**
 * Fabrica para crear instancias de RobotLogistico a partir de JSON.
 * Valida que el robot inicie dentro de cobertura de algún Robopuerto.
 */
public class RobotFactory {
    private final List<Robopuerto> robopuertos;

    public RobotFactory(List<Robopuerto> robopuertos) {
        this.robopuertos = robopuertos;
    }

    /**
     * Crea un RobotLogistico leyendo atributos "x","y","bateria","capacidadCarga" del nodo JSON.
     * @param node JsonNode con los campos necesarios.
     * @return nueva instancia de RobotLogistico.
     * @throws ExcepcionLogistica si falta algún campo, valor inválido o fuera de cobertura.
     */
    public RobotLogistico crearDesdeJson(JsonNode node) throws ExcepcionLogistica {
        if (!node.has("x") || !node.has("y") || !node.has("bateria") || !node.has("capacidadCarga")) {
            throw new EstructuraInvalidaException(
                "Robot sin campo 'x','y','bateria' o 'capacidadCarga'.", null);
        }
        int x = node.get("x").asInt();
        int y = node.get("y").asInt();
        int bateria = node.get("bateria").asInt(-1);
        int carga = node.get("capacidadCarga").asInt(-1);
        if (bateria <= 0 || carga <= 0) {
            throw new ValorInvalidoException(
                String.format("Valores inválidos para robot en (%d,%d): bateria=%d, carga=%d", x, y, bateria, carga));
        }
        Ubicacion ubic = new Ubicacion(x, y);
        boolean cubierto = robopuertos.stream().anyMatch(rp -> rp.cubre(ubic));
        if (!cubierto) {
            throw new RobotFueraDeCoberturaException(
                String.format("Robot fuera de cobertura en (%d,%d)", x, y));
        }
        return new RobotLogistico(ubic, carga, bateria);
    }
}
