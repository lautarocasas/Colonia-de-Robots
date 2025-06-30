package main.java.coloniaDeRobots.eventos;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Bus de eventos que permite publicar y suscribir para distintos tipos.
 */
public class EventBus {
	// Singleton default
	private static final EventBus INSTANCE = new EventBus();

	/**
	 * Obtiene la instancia global del EventBus.
	 */
	public static EventBus getDefault() {
		return INSTANCE;
	}

	// Mapa de tipo de evento a lista de listeners
	private final Map<Class<? extends Evento>, List<EventListener<? extends Evento>>> listeners = new HashMap<>();

	/**
	 * Suscribe un listener para un tipo específico de evento.
	 */
	public <T extends Evento> void register(Class<T> tipo, EventListener<T> listener) {
		listeners.computeIfAbsent(tipo, k -> new CopyOnWriteArrayList<>()).add(listener);
	}

	/**
	 * Publica un evento a todos los listeners registrados para su tipo.
	 */
	public <T extends Evento> void post(T evento) {
		Class<?> tipo = evento.getClass();
		// despacho a listeners exactos
		List<EventListener<? extends Evento>> subs = listeners.getOrDefault(tipo, Collections.emptyList());
		for (EventListener<? extends Evento> l : subs) {
			@SuppressWarnings("unchecked")
			EventListener<T> listener = (EventListener<T>) l;
			listener.onEvent(evento);
		}
	}
}
