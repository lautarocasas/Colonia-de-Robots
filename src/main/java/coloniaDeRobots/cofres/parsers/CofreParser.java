package main.java.coloniaDeRobots.cofres.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.logistica.excepciones.ExcepcionLogistica;

public interface CofreParser {
       Cofre parse(JsonNode nodo) throws ExcepcionLogistica;
}