package coloniaDeRobots.cofres.parsers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;
import coloniaDeRobots.cofres.CofreSolicitud;
import logistica.excepciones.EstructuraInvalidaException;
import logistica.excepciones.ExcepcionLogistica;
import logistica.excepciones.ValorInvalidoException;

public class SolicitudParser extends BaseParser {
	
	public CofreSolicitud parse(JsonNode nodo) throws ExcepcionLogistica {
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
	}
}
