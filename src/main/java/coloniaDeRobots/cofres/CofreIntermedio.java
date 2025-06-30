package main.java.coloniaDeRobots.cofres;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;

public class CofreIntermedio extends Cofre {
	private static final Logger LOGGER = Logger.getLogger(CofreIntermedio.class.getName());
	private final Map<Item, Integer> solicitudes;

	public CofreIntermedio(Ubicacion ubicacion, Map<Item, Integer> inventario, Map<Item, Integer> solicitudes) {
		super(ubicacion, inventario);
		this.solicitudes = Collections.unmodifiableMap(new HashMap<>(solicitudes));
	}

	@Override
	public void accionar(SistemaLogistico sistema) {
		Logger.getLogger(getClass().getName()).info(() -> "Accionando Intermedio en " + ubicacion);
		// Fase solicitud
		for (Map.Entry<Item, Integer> e : solicitudes.entrySet()) {
			Item item = e.getKey();
			int total = e.getValue();
			int recibidos = sistema.getCantidadRecibida(this, item);
			int pendiente = total - recibidos;
			if (pendiente > 0) {
				sistema.registrarSolicitud(new Solicitud(this, item, pendiente));
				Logger.getLogger(getClass().getName())
						.info(() -> String.format("[%s] Intermedio solicita %d de %s", ubicacion, pendiente, item));
			}
		}
		// Fase pasiva: el sistema puede retirar stock como en pasiva
	}

	/**
	 * Devuelve el mapa de solicitudes originales.
	 */
	public Map<Item, Integer> getSolicitudes() {
		return solicitudes;
	}
}