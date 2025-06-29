package coloniaDeRobots.cofres.parsers;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;
import coloniaDeRobots.cofres.CofreProvisionPasiva;
import logistica.excepciones.ExcepcionLogistica;

public class ProvisionPasivaParser extends BaseParser {
	
    public CofreProvisionPasiva parse(JsonNode nodo) throws ExcepcionLogistica {
        Ubicacion ubicacion = parseUbicacion(nodo);
        Map<Item,Integer> inventario = parseInventario(nodo);
        return new CofreProvisionPasiva(ubicacion, inventario);
    }
}
