package main.java.coloniaDeRobots.eventos;

import main.java.coloniaDeRobots.Solicitud;

/**
 * Evento que indica que se registró una solicitud.
 */
public class SolicitudRegistradaEvent implements Evento {
	public final Solicitud solicitud;

	public SolicitudRegistradaEvent(Solicitud solicitud) {
		this.solicitud = solicitud;
	}
}
