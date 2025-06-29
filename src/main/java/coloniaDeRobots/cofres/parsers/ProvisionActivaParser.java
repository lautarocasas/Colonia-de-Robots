package main.java.coloniaDeRobots.cofres.parsers;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.cofres.CofreProvisionActiva;
import main.java.logistica.excepciones.ExcepcionLogistica;

public class ProvisionActivaParser extends BaseParser {
	
	public CofreProvisionActiva parse(JsonNode nodo) throws ExcepcionLogistica {
        Ubicacion ubicacion = parseUbicacion(nodo);
        Map<Item,Integer> inventario = parseInventario(nodo);
        return new CofreProvisionActiva(ubicacion, inventario);
    }
}
