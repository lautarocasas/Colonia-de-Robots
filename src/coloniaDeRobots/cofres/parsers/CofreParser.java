package coloniaDeRobots.cofres.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import coloniaDeRobots.Cofre;
import logistica.excepciones.ExcepcionLogistica;

public interface CofreParser {
       Cofre parse(JsonNode node) throws ExcepcionLogistica;
}