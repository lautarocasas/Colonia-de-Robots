package coloniaDeRobots.cofres.parsers;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;
import coloniaDeRobots.Cofres.CofreAlmacenamiento;

public class AlmacenamientoParser {
    public CofreAlmacenamiento parse(JsonNode n) throws ExcepcionLogistica {
        Ubicacion u = parseUbicacion(n);
        Map<Item,Integer> inv = parseInventario(n);
        return new CofreAlmacenamiento(u, inv);
    }
}
