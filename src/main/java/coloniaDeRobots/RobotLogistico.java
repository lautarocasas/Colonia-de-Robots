package main.java.coloniaDeRobots;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.eventos.EventBus;
import main.java.coloniaDeRobots.eventos.RobotEvent;
import main.java.coloniaDeRobots.util.BuscadorCaminos;

public class RobotLogistico extends ElementoLogistico {
	private static final Logger LOGGER = Logger.getLogger(RobotLogistico.class.getName());
	private final int capacidadCarga;
	private final double bateriaMaxima;
	private double bateriaActual;
	private List<Robopuerto> robopuertos; // para detectar recarga
	private List<Cofre> cofres;
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
		this.setBateriaActual(capacidadBateria);
		// LOGGER.info(() -> String.format("Robot en %s: carga %d, batería %.2f",
		// ubicacion, capacidadCarga, capacidadBateria));
	}

	/**
	 * Inyecta la lista de robopuertos y cofres para planificación.
	 */
	public void setEntornoLogistico(List<Robopuerto> puertos, List<Cofre> cofres) {
		this.robopuertos = puertos;
		this.cofres = cofres;
	}

	/**
	 * @return true si hay ruta pendiente
	 */
	public boolean tieneTarea() {
		return !ruta.isEmpty();
	}

	/**
	 * Planifica la ruta óptima hasta destino, pasando por consecución de
	 * ubicaciones.
	 */
	public void planificarRuta(Ubicacion origen, Ubicacion destino) {
		List<Ubicacion> camino = BuscadorCaminos.calcularCaminoMasCorto(origen, destino, cofres, robopuertos);
		ruta.clear();
		for (Ubicacion u : camino) {
			ruta.add(u);
		}
	}

	/**
	 * Avanza al siguiente punto de la ruta, consumiendo batería.
	 */
	public void avanzar() {
		if (ruta.isEmpty())
			return;
		Ubicacion siguiente = ruta.poll();
		double distancia = getUbicacion().distanciaA(siguiente);
		double consumo = distancia * 1.0;//factor de consumofijo
		if (getBateriaActual() < consumo) {
			// No puede avanzar sin recarga; cancelar ruta
			ruta.clear();
			return;
		}
		setBateriaActual(getBateriaActual() - consumo);
		// Actualizar posición
		this.ubicacion = siguiente;
	}

	public boolean puedeTransportar(int cantidad) {
		return cantidad <= capacidadCarga;
	}

	public boolean tieneBateriaPara(double distancia, double factor) {
		return getBateriaActual() >= distancia * factor;
	}

	public void consumirBateria(double cantidad) {
		setBateriaActual(Math.max(0, getBateriaActual() - cantidad));
		LOGGER.fine(() -> String.format("Robot en %s consumió %.2f batería (resta %.2f)", ubicacion, cantidad,
				getBateriaActual()));
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
		Robopuerto rp = getRobopuertoActual();
		if (rp != null) {
			setBateriaActual(bateriaMaxima);
			EventBus.getDefault().post(new RobotEvent(this, rp));
		}
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

	public void setBateriaActual(double bateriaActual) {
		this.bateriaActual = bateriaActual;
	}
}
