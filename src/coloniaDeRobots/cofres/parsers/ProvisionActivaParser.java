package coloniaDeRobots.cofres.parsers;

public class ProvisionActivaParser {
	public CofreProvisionActiva parse(JsonNode n) throws ExcepcionLogistica {
        Ubicacion u = parseUbicacion(n);
        Map<Item,Integer> inv = parseInventario(n);
        return new CofreProvisionActiva(u, inv);
    }
}
