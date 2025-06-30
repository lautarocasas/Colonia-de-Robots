package main.java.coloniaDeRobots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.eventos.EventBus;
import main.java.coloniaDeRobots.eventos.RobotEvent;
import main.java.coloniaDeRobots.eventos.SolicitudRegistradaEvent;
import main.java.coloniaDeRobots.eventos.TransporteGeneradoEvent;

public class SistemaLogistico {
	private static final Logger LOGGER = Logger.getLogger(SistemaLogistico.class.getName());
	private final List<Cofre> cofres = new ArrayList<>();
	private final List<Robopuerto> puertos = new ArrayList<>();
	private final List<RobotLogistico> robots = new ArrayList<>();
	private final List<Solicitud> solicitudes = new LinkedList<>();
	private final List<Solicitud> solicitudesCompletadas = new ArrayList<>();
	private final double factorConsumo;

	public SistemaLogistico(double factorConsumo) {
		if (factorConsumo <= 0)
			throw new IllegalArgumentException("Factor de consumo inválido");
		this.factorConsumo = factorConsumo;
	}

	public void agregarCofre(Cofre c) {
		cofres.add(c);
		LOGGER.info(() -> "Cofre agregado: " + c.getUbicacion());
	}

	public void registrarSolicitud(Solicitud s) {
		solicitudes.add(s);
		EventBus.getDefault().post(new SolicitudRegistradaEvent(s));
	}

	public void agregarRobopuerto(Robopuerto p) {
		puertos.add(p);
		LOGGER.info(() -> "Robopuerto agregado: " + p.getUbicacion());
	}

	public void agregarRobot(RobotLogistico r) {
		robots.add(r);
		LOGGER.info(() -> "Robot agregado: " + r.getUbicacion());
	}

	public List<Solicitud> obtenerSolicitudesPendientes() {
		return solicitudes.stream().filter(s -> !s.estaCompletada()).toList();
	}

	public List<Solicitud> obtenerSolicitudesPendientes(Cofre destino) {
		return solicitudes.stream().filter(s -> s.getCofreDestino().equals(destino) && !s.estaCompletada()).toList();
	}

	public int getCantidadRecibida(Cofre c, Item item) {
		return Stream.concat(solicitudes.stream(), solicitudesCompletadas.stream())
				.filter(s -> s.getCofreDestino().equals(c) && s.getItem().equals(item))
				.mapToInt(Solicitud::getCantidadRecibida).sum();
	}

	/**
	 * Genera un transporte de ítems de un cofre origen a un cofre destino.
	 * Actualiza la solicitud correspondiente y registra logs.
	 */
	public void generarTransporte(Cofre origen, Cofre destino, Item item, int cantidad) {
		LOGGER.info(() -> String.format("Generando transporte: %d de %s de %s a %s", cantidad, item,
				origen.getUbicacion(), destino.getUbicacion()));
		solicitudes.stream()
				.filter(s -> s.getCofreDestino().equals(destino) && s.getItem().equals(item) && !s.estaCompletada())
				.findFirst().ifPresent(s -> {
					s.registrarEntrega(cantidad);
					if (s.estaCompletada()) {
						solicitudes.remove(s);
						solicitudesCompletadas.add(s);
						LOGGER.info(() -> String.format("Solicitud completada y removida: %s en %s", item,
								destino.getUbicacion()));
					}
				});
		EventBus.getDefault().post(new TransporteGeneradoEvent(origen, destino, item, cantidad));
	}

	/**
	 * Devuelve la lista de cofres en el sistema (uso interno o para
	 * testing/simulación).
	 */
	public List<Cofre> getCofres() {
		return Collections.unmodifiableList(cofres);
	}

	/**
	 * Ejecuta la simulación: en cada ciclo, los cofres accionan y luego los robots
	 * se mueven y recargan.
	 */
	public void run() {
		int ciclo = 0;
		while (true) {
			if (solicitudes.isEmpty()) {
				System.out.println("✅ Estado estable en " + ciclo + " ciclos.");
				break;
			}
			System.out.println("🔄 Ciclo " + ciclo + ": " + solicitudes.size() + " solicitudes pendientes");

			// Cofres actúan (generan solicitudes y transporte)
			for (Cofre c : new ArrayList<>(cofres)) {
				c.accionar(this);
			}

			// Robots ejecutan su turno: moverse y recargar
			for (RobotLogistico robot : robots) {
				// Si tiene ruta planificada, avanza un paso
				if (robot.tieneTarea()) {
					robot.avanzar();
				} else {
					// Si no hay tarea, busco una nueva solicitud y asigno
					Solicitud s = solicitudes.stream().findFirst().orElse(null);
					if (s != null) {
						robot.planificarRuta(s.getCofreOrigen().getUbicacion(), s.getCofreDestino().getUbicacion());
					}
				}
				// Si pasa por un robopuerto recarga
				Robopuerto rp = robot.getRobopuertoActual();
				if (rp != null) {
					robot.recargar();
					EventBus.getDefault().post(new RobotEvent(robot, rp));
				}
			}

			ciclo++;
			if (ciclo > 1000) {
				System.err.println("⚠️ Límite de 1000 ciclos alcanzado.");
				break;
			}
		}
	}
	// Métodos para planificar rutas, asignar robots, simular ciclos, etc.
	// Pendientes de implementar basados en grafo y cobertura.
}
