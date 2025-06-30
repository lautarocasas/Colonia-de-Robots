package main.java.coloniaDeRobots.eventos;

/**
 * Listener para recibir eventos de tipo T.
 */
public interface EventListener<T extends Evento> {
	void onEvent(T evento);
}
