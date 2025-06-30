package main.java.coloniaDeRobots.eventos;

import main.java.coloniaDeRobots.cofres.Cofre;

/**
 * Evento que indica que un cofre acaba de accionar.
 */
public class CofreAccionadoEvent implements Evento {
	public final Cofre cofre;

	public CofreAccionadoEvent(Cofre cofre) {
		this.cofre = cofre;
	}
}
