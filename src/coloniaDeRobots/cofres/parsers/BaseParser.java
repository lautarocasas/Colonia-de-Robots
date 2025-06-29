package coloniaDeRobots.cofres.parsers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;
import logistica.excepciones.EstructuraInvalidaException;
import logistica.excepciones.ExcepcionLogistica;
import logistica.excepciones.ValorInvalidoException;

public abstract class BaseParser implements CofreParser {

	protected Ubicacion parseUbicacion(JsonNode nodo) throws ExcepcionLogistica {
		if (!nodo.has("x") || !nodo.has("y"))
			throw new EstructuraInvalidaException("Faltan coordenadas 'x' o 'y'.", null);
		return new Ubicacion(nodo.get("x").asInt(), nodo.get("y").asInt());
	}

	protected Map<Item, Integer> parseInventario(JsonNode nodo) throws ExcepcionLogistica {
		Map<Item, Integer> inventario = new HashMap<>();
		if (nodo.has("inventario")) {
			JsonNode nodoInventario = nodo.get("inventario");
			for (Iterator<String> it = nodoInventario.fieldNames(); it.hasNext();) {
				String nombre = it.next();
				int cantidad = nodoInventario.get(nombre).asInt(-1);
				if (cantidad < 0)
					throw new ValorInvalidoException("Cantidad negativa en inventario de " + nombre);
				inventario.put(new Item(nombre), cantidad);
			}
		}
		return inventario;
	}
}