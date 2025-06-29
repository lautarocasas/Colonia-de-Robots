package coloniaDeRobots.cofres.parsers;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;
import coloniaDeRobots.cofres.CofreAlmacenamiento;
import logistica.excepciones.ExcepcionLogistica;

public class AlmacenamientoParser extends BaseParser {
	
    public CofreAlmacenamiento parse(JsonNode nodo) throws ExcepcionLogistica {
        Ubicacion ubicacion = parseUbicacion(nodo);
        Map<Item,Integer> inventario = parseInventario(nodo);
        return new CofreAlmacenamiento(ubicacion, inventario);
    }
}
