package coloniaDeRobots.cofres.parsers;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;
import coloniaDeRobots.cofres.CofreProvisionActiva;
import logistica.excepciones.ExcepcionLogistica;

public class ProvisionActivaParser extends BaseParser {
	public CofreProvisionActiva parse(JsonNode n) throws ExcepcionLogistica {
        Ubicacion u = parseUbicacion(n);
        Map<Item,Integer> inv = parseInventario(n);
        return new CofreProvisionActiva(u, inv);
    }
}
