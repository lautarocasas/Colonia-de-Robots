package main.java.coloniaDeRobots.eventos;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.cofres.Cofre;

/**
 * Evento que indica que se generó un transporte.
 */
public class TransporteGeneradoEvent implements Evento {
	public final Cofre origen;
	public final Cofre destino;
	public final Item item;
	public final int cantidad;

	public TransporteGeneradoEvent(Cofre origen, Cofre destino, Item item, int cantidad) {
		this.origen = origen;
		this.destino = destino;
		this.item = item;
		this.cantidad = cantidad;
	}
}
