package coloniaDeRobots.cofres.parsers;

public class IntermedioParser {
	public CofreIntermedio parse(JsonNode n) throws ExcepcionLogistica {
        CofreSolicitud base = (CofreSolicitud) new SolicitudParser().parse(n);
        return new CofreIntermedio(base.getUbicacion(), base.getInventario(), base.getSolicitudes());
    }
}
