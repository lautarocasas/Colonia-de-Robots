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
        protected Ubicacion parseUbicacion(JsonNode n) throws ExcepcionLogistica {
            if (!n.has("x") || !n.has("y"))
                throw new EstructuraInvalidaException("Faltan coordenadas 'x' o 'y'.", null);
            return new Ubicacion(n.get("x").asInt(), n.get("y").asInt());
        }
        protected Map<Item, Integer> parseInventario(JsonNode n) throws ExcepcionLogistica {
            Map<Item,Integer> inv = new HashMap<>();
            if (n.has("inventario")) {
                JsonNode invNode = n.get("inventario");
                for (Iterator<String> it = invNode.fieldNames(); it.hasNext(); ) {
                    String name = it.next();
                    int qty = invNode.get(name).asInt(-1);
                    if (qty < 0)
                        throw new ValorInvalidoException("Cantidad negativa en inventario de " + name);
                    inv.put(new Item(name), qty);
                }
            }
            return inv;
        }
    }