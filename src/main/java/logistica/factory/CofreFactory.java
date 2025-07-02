package main.java.logistica.factory;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.cofres.TipoCofre;
import main.java.coloniaDeRobots.cofres.parsers.AlmacenamientoParser;
import main.java.coloniaDeRobots.cofres.parsers.CofreParser;
import main.java.coloniaDeRobots.cofres.parsers.IntermedioParser;
import main.java.coloniaDeRobots.cofres.parsers.ProvisionActivaParser;
import main.java.coloniaDeRobots.cofres.parsers.ProvisionPasivaParser;
import main.java.coloniaDeRobots.cofres.parsers.SolicitudParser;
import main.java.logistica.excepciones.EstructuraInvalidaException;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.excepciones.TipoCofreDesconocidoException;

public class CofreFactory {
    private final Map<TipoCofre, CofreParser> parsers = new HashMap<>();

    public CofreFactory() {
        parsers.put(TipoCofre.PROVISION_ACTIVA, new ProvisionActivaParser());
        parsers.put(TipoCofre.PROVISION_PASIVA, new ProvisionPasivaParser());
        parsers.put(TipoCofre.ALMACENAMIENTO, new AlmacenamientoParser());
        parsers.put(TipoCofre.SOLICITUD, new SolicitudParser());
        parsers.put(TipoCofre.INTERMEDIO, new IntermedioParser());
    }

    public Cofre crearCofreDesdeJson(JsonNode node) throws ExcepcionLogistica {
        // 1) Extraer el campo tipo como String
        String tipoStr = node.path("tipo").asText(null);
        if (tipoStr == null) {
            throw new EstructuraInvalidaException("Cofre sin campo 'tipo'.", null);
        }

        // 2) Convertir a enum, capturando caso inválido
        TipoCofre tipoEnum;
        try {
            tipoEnum = TipoCofre.valueOf(tipoStr);
        } catch (IllegalArgumentException e) {
            throw new TipoCofreDesconocidoException("Tipo de cofre inválido: " + tipoStr);
        }

        // 3) Obtener el parser correspondiente y parsear
        CofreParser parser = parsers.get(tipoEnum);
        // parser nunca debería ser null porque cubrimos todos los enum, pero por seguridad:
        if (parser == null) {
            throw new TipoCofreDesconocidoException("No existe parser para el tipo: " + tipoEnum);
        }
        return parser.parse(node);
    }
}
