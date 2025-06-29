package coloniaDeRobots.cofres.parsers;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;
import coloniaDeRobots.cofres.CofreProvisionPasiva;
import logistica.excepciones.ExcepcionLogistica;

public class ProvisionPasivaParser extends BaseParser {
    public CofreProvisionPasiva parse(JsonNode n) throws ExcepcionLogistica {
        Ubicacion u = parseUbicacion(n);
        Map<Item,Integer> inv = parseInventario(n);
        return new CofreProvisionPasiva(u, inv);
    }
}
