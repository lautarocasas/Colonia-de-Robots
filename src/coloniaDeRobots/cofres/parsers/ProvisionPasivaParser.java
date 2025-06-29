package coloniaDeRobots.cofres.parsers;

public class ProvisionPasivaParser {
    public CofreProvisionPasiva parse(JsonNode n) throws ExcepcionLogistica {
        Ubicacion u = parseUbicacion(n);
        Map<Item,Integer> inv = parseInventario(n);
        return new CofreProvisionPasiva(u, inv);
    }
}
