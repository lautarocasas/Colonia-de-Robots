package coloniaDeRobots.cofres.parsers;

public class SolicitudParser {
    public CofreSolicitud parse(JsonNode n) throws ExcepcionLogistica {
        Ubicacion u = parseUbicacion(n);
        Map<Item,Integer> inv = parseInventario(n);
        JsonNode solNode = n.path("solicitudes");
        if (solNode.isMissingNode() || solNode.size() == 0)
            throw new EstructuraInvalidaException("Cofre SOLICITUD sin 'solicitudes'.");
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
