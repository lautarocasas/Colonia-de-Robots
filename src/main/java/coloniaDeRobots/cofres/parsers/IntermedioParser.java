package main.java.coloniaDeRobots.cofres.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import main.java.coloniaDeRobots.cofres.CofreIntermedio;
import main.java.coloniaDeRobots.cofres.CofreSolicitud;
import main.java.logistica.excepciones.ExcepcionLogistica;

public class IntermedioParser extends BaseParser {
	
	public CofreIntermedio parse(JsonNode nodo) throws ExcepcionLogistica {
        CofreSolicitud base = new SolicitudParser().parse(nodo);
        return new CofreIntermedio(base.getUbicacion(), base.getInventario(), base.getSolicitudes());
    }
}
