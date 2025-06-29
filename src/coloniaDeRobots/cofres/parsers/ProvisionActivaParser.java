package coloniaDeRobots.cofres.parsers;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;
import coloniaDeRobots.cofres.CofreProvisionActiva;
import logistica.excepciones.ExcepcionLogistica;

public class ProvisionActivaParser extends BaseParser {
	
	public CofreProvisionActiva parse(JsonNode nodo) throws ExcepcionLogistica {
        Ubicacion ubicacion = parseUbicacion(nodo);
        Map<Item,Integer> inventario = parseInventario(nodo);
        return new CofreProvisionActiva(ubicacion, inventario);
    }
}
