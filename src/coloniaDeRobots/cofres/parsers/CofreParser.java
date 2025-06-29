package coloniaDeRobots.cofres.parsers;
public interface CofreParser {
       Cofre parse(JsonNode node) throws ExcepcionLogistica;
}