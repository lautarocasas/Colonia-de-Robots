package main.java.coloniaDeRobots.eventos;

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
		Class<? extends Evento> eventoClase = evento.getClass();
		for (var entry : listeners.entrySet()) {
			Class<? extends Evento> tipoRegistrado = entry.getKey();
			if (tipoRegistrado.isAssignableFrom(eventoClase)) {
				for (EventListener<? extends Evento> raw : entry.getValue()) {
					@SuppressWarnings("unchecked")
					EventListener<T> listener = (EventListener<T>) raw;
					listener.onEvent(evento);
				}
			}
		}
	}

}
