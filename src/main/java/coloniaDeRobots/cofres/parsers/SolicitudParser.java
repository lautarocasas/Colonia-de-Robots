package main.java.coloniaDeRobots.cofres.parsers;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.cofres.CofreSolicitud;
import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.excepciones.ValorInvalidoException;

public class SolicitudParser extends BaseParser {
	
	public CofreSolicitud parse(JsonNode nodo) throws ExcepcionLogistica {
	    Ubicacion ubicacion = parseUbicacion(nodo);
	    Map<Item, Integer> inventario = parseInventario(nodo);
	    Map<Item, Integer> solicitudes = parseSolicitudes(nodo);
	    return new CofreSolicitud(ubicacion, inventario, solicitudes);
	}
	
	private Map<Item, Integer> parseSolicitudes(JsonNode nodo) throws ExcepcionLogistica {
	    JsonNode nodoSolicitud = nodo.path("solicitudes");

	    if (nodoSolicitud.isMissingNode() || nodoSolicitud.size() == 0) {
	        throw new EstructuraInvalidaException("Cofre SOLICITUD sin 'solicitudes'.", null);
	    }

	    Map<Item, Integer> solicitudes = new HashMap<>();

	    for (String nombreItem : (Iterable<String>) () -> nodoSolicitud.fieldNames()) {
	        int cantidad = nodoSolicitud.get(nombreItem).asInt(-1);

	        if (cantidad <= 0) {
	            throw new ValorInvalidoException("Cantidad inválida en solicitud de " + nombreItem);
	        }

	        solicitudes.put(new Item(nombreItem), cantidad);
	    }

	    return solicitudes;
	}

	/*Metodo antiguo: si les parece ok dejamos el que refactorice, sino volvemos a poner esto
	 * 
	 * public CofreSolicitud parse(JsonNode nodo) throws ExcepcionLogistica {
	Ubicacion ubicacion = parseUbicacion(nodo);
	Map<Item, Integer> inventario = parseInventario(nodo);
	JsonNode nodoSolicitud = nodo.path("solicitudes");
	if (nodoSolicitud.isMissingNode() || nodoSolicitud.size() == 0)
		throw new EstructuraInvalidaException("Cofre SOLICITUD sin 'solicitudes'.", null);
	Map<Item, Integer> solicitudes = new HashMap<>();
	for (Iterator<String> it = nodoSolicitud.fieldNames(); it.hasNext();) {
		String nombre = it.next();
		int cantidad = nodoSolicitud.get(nombre).asInt(-1);
		if (cantidad <= 0)
			throw new ValorInvalidoException("Cantidad inválida en solicitud de " + nombre);
		solicitudes.put(new Item(nombre), cantidad);
	}
	return new CofreSolicitud(ubicacion, inventario, solicitudes);
}*/

}
