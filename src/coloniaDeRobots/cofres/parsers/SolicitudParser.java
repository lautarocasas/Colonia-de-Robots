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
    public CofreSolicitud parse(JsonNode n) throws ExcepcionLogistica {
        Ubicacion u = parseUbicacion(n);
        Map<Item,Integer> inv = parseInventario(n);
        JsonNode solNode = n.path("solicitudes");
        if (solNode.isMissingNode() || solNode.size() == 0)
            throw new EstructuraInvalidaException("Cofre SOLICITUD sin 'solicitudes'.", null);
        Map<Item,Integer> req = new HashMap<>();
        for (Iterator<String> it = solNode.fieldNames(); it.hasNext(); ) {
            String name = it.next();
            int qty = solNode.get(name).asInt(-1);
            if (qty <= 0)
                throw new ValorInvalidoException("Cantidad inválida en solicitud de " + name);
            req.put(new Item(name), qty);
        }
        return new CofreSolicitud(u, inv, req);
    }
}
