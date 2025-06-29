package coloniaDeRobots.cofres.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.cofres.CofreIntermedio;
import coloniaDeRobots.cofres.CofreSolicitud;
import logistica.excepciones.ExcepcionLogistica;

public class IntermedioParser extends BaseParser {
	
	public CofreIntermedio parse(JsonNode nodo) throws ExcepcionLogistica {
        CofreSolicitud base = new SolicitudParser().parse(nodo);
        return new CofreIntermedio(base.getUbicacion(), base.getInventario(), base.getSolicitudes());
    }
}
