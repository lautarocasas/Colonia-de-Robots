package coloniaDeRobots.cofres;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Cofre;
import coloniaDeRobots.cofres.parsers.AlmacenamientoParser;
import coloniaDeRobots.cofres.parsers.CofreParser;
import coloniaDeRobots.cofres.parsers.IntermedioParser;
import coloniaDeRobots.cofres.parsers.ProvisionActivaParser;
import coloniaDeRobots.cofres.parsers.ProvisionPasivaParser;
import coloniaDeRobots.cofres.parsers.SolicitudParser;
import logistica.excepciones.EstructuraInvalidaException;
import logistica.excepciones.ExcepcionLogistica;
import logistica.excepciones.TipoCofreDesconocidoException;

public class CofreFactory {
	private final Map<TipoCofre, CofreParser> parsers = new HashMap<>();

    public CofreFactory() {
        parsers.put(TipoCofre.ACTIVO, new ProvisionActivaParser());
        parsers.put(TipoCofre.PASIVO, new ProvisionPasivaParser());
        parsers.put(TipoCofre.ALMACENAMIENTO, new AlmacenamientoParser());
        parsers.put(TipoCofre.SOLICITUD, new SolicitudParser());
        parsers.put(TipoCofre.INTERMEDIO, new IntermedioParser());
    }

    public Cofre crearCofreDesdeJson(JsonNode node) throws ExcepcionLogistica {
        String tipo = node.path("tipo").asText(null);
        if (tipo == null)
            throw new EstructuraInvalidaException("Cofre sin campo 'tipo'.", null);
        CofreParser parser = parsers.get(tipo);
        if (parser == null)
            throw new TipoCofreDesconocidoException("Tipo de cofre inválido: " + tipo);
        return parser.parse(node);
    }

}
