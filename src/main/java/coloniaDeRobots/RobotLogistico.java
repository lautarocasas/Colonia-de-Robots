package main.java.coloniaDeRobots;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.eventos.EventBus;
import main.java.coloniaDeRobots.eventos.RobotEvent;

public class RobotLogistico extends ElementoLogistico {
	private static final Logger LOGGER = Logger.getLogger(RobotLogistico.class.getName());
	private final int capacidadCarga;
	private final double bateriaMaxima;
	private double bateriaActual;
	private List<Robopuerto> robopuertos; // para detectar recarga
	private final Deque<Ubicacion> ruta = new ArrayDeque<>();

	/**
	 * @param ubicacion      Posición inicial
	 * @param capacidadCarga Unidades máximas que puede transportar
	 * @param bateriaMaxima  Células máximas de batería
	 */
	public RobotLogistico(Ubicacion ubicacion, int capacidadCarga, double capacidadBateria) {
		super(ubicacion);
		if (capacidadCarga <= 0 || capacidadBateria <= 0)
			throw new IllegalArgumentException("Capacidad inválida");
		this.capacidadCarga = capacidadCarga;
		this.bateriaMaxima = capacidadBateria;
		this.bateriaActual = capacidadBateria;
		// LOGGER.info(() -> String.format("Robot en %s: carga %d, batería %.2f",
		// ubicacion, capacidadCarga, capacidadBateria));
	}

	/**
	 * Inyecta la lista de robopuertos para que el robot detecte recarga.
	 */
	public void setRobopuertos(List<Robopuerto> robopuertos) {
		this.robopuertos = robopuertos;
	}

	/**
	 * @return true si hay ruta pendiente
	 */
	public boolean tieneTarea() {
		return !ruta.isEmpty();
	}

	/**
	 * Define una ruta simple: primero al origen (si es distinto), luego al destino.
	 */
	public void planificarRuta(Ubicacion origen, Ubicacion destino) {
		ruta.clear();
		Ubicacion actual = getUbicacion();
		if (!actual.equals(origen)) {
			ruta.add(origen);
		}
		ruta.add(destino);
	}

	/**
	 * Avanza al siguiente punto de la ruta, consumiendo batería.
	 */
	public void avanzar() {
		if (ruta.isEmpty())
			return;
		Ubicacion siguiente = ruta.poll();
		double distancia = getUbicacion().distanciaA(siguiente);
		double consumo = distancia * 1.0; // factor de consumo fijo
		if (bateriaActual < consumo) {
			// No puede avanzar sin recarga; cancelar ruta
			ruta.clear();
			return;
		}
		bateriaActual -= consumo;
		// Actualizar posición
		this.ubicacion = siguiente;
	}

	public boolean puedeTransportar(int cantidad) {
		return cantidad <= capacidadCarga;
	}

	public boolean tieneBateriaPara(double distancia, double factor) {
		return bateriaActual >= distancia * factor;
	}

	public void consumirBateria(double cantidad) {
		bateriaActual = Math.max(0, bateriaActual - cantidad);
		LOGGER.fine(() -> String.format("Robot en %s consumió %.2f batería (resta %.2f)", ubicacion, cantidad,
				bateriaActual));
	}

	/**
	 * Si el robot está dentro del alcance de algún robopuerto, retorna ese
	 * robopuerto.
	 */
	public Robopuerto getRobopuertoActual() {
		if (robopuertos == null)
			return null;
		for (Robopuerto rp : robopuertos) {
			if (rp.cubre(getUbicacion())) {
				return rp;
			}
		}
		return null;
	}

	/**
	 * Recarga completamente la batería.
	 */
	public void recargar() {
		this.bateriaActual = this.bateriaMaxima;
		EventBus.getDefault().post(new RobotEvent(this, getRobopuertoActual()));
	}

	/**
	 * @return la capacidad máxima de carga
	 */
	public int getCapacidadCarga() {
		return capacidadCarga;
	}

	/**
	 * @return nivel actual de batería
	 */
	public double getBateriaActual() {
		return bateriaActual;
	}
}
