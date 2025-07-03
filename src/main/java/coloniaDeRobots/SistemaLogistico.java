package main.java.coloniaDeRobots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.logistica.excepciones.ExcepcionLogistica;

public class SistemaLogistico {
	//private static final Logger LOGGER = Logger.getLogger(SistemaLogistico.class.getName());
	private final List<Cofre> cofres = new ArrayList<>();
	private final List<Robopuerto> puertos = new ArrayList<>();
	private final List<RobotLogistico> robots = new ArrayList<>();
	private final List<Solicitud> solicitudes = new LinkedList<>();
	private final List<Solicitud> solicitudesCompletadas = new ArrayList<>();
	private final double factorConsumo;

	public SistemaLogistico(double factorConsumo) {
		if (factorConsumo <= 0)
			throw new IllegalArgumentException("Factor de consumo invalido");
		this.factorConsumo = factorConsumo;
	}

	public void agregarCofre(Cofre c) {
		cofres.add(c);
		System.out.println("Cofre agregado: " + c.getUbicacion());
		//LOGGER.info(() -> "Cofre agregado: " + c.getUbicacion());
	}

	public void registrarSolicitud(Solicitud s) {
		solicitudes.add(s);
		System.out.println("Registrando solicitud ");
		//EventBus.getDefault().post(new SolicitudRegistradaEvent(s));
	}

	public void agregarRobopuerto(Robopuerto p) {
		puertos.add(p);
		System.out.println("Robopuerto agregado: " + p.getUbicacion());
		//LOGGER.info(() -> "Robopuerto agregado: " + p.getUbicacion());
	}

	public void agregarRobot(RobotLogistico r) {
		robots.add(r);
		//LOGGER.info(() -> "Robot agregado: " + r.getUbicacion());
		System.out.println("Robot agregado: " + r.getUbicacion());
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
		//LOGGER.info(() -> String.format("Generando transporte: %d de %s de %s a %s", cantidad, item,
				//origen.getUbicacion(), destino.getUbicacion()));
		System.out.println(String.format("Generando transporte: %d de %s de %s a %s", cantidad, item,
				origen.getUbicacion(), destino.getUbicacion()));
		solicitudes.stream()
				.filter(s -> s.getCofreDestino().equals(destino) && s.getItem().equals(item) && !s.estaCompletada())
				.findFirst().ifPresent(s -> {
					s.registrarEntrega(cantidad);
					if (s.estaCompletada()) {
						solicitudes.remove(s);
						solicitudesCompletadas.add(s);
						//LOGGER.info(() -> String.format("Solicitud completada y removida: %s en %s", item,
							//	destino.getUbicacion()));
						System.out.println(String.format("Solicitud completada y removida: %s en %s", item,
								destino.getUbicacion()));
					}
				});
		//EventBus.getDefault().post(new TransporteGeneradoEvent(origen, destino, item, cantidad));
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
	 * @throws ExcepcionLogistica 
	 */
	public int run() throws ExcepcionLogistica {
		int ciclo = 0;
		int cantMat = 0, cantMatNueva = 0, cantMatVieja = 0;
		final int MAX_CICLOS = 1000;
		
		
		// Obtener las solicitudes de los cofres.
		// 1) Cofres actúan
		for (Cofre c : new ArrayList<>(cofres)) {
			c.accionar(this);
		}
		
		// Asignar los cofres de provision activa o pasiva
		asignarCofresProveedoresASolicitudes();
		
		while (ciclo < MAX_CICLOS) {
			System.out.println("Ciclo " + (ciclo + 1) + ": " + solicitudes.size() + " solicitudes pendientes");

			// 2) Robots actúan (movimiento y recarga)
			for (RobotLogistico robot : robots) {
				if (robot.tieneTarea()) {
					
					robot.avanzar(factorConsumo);
					
					if(robot.tareaCompleta()) {
						solicitudesCompletadas.add(robot.devolverSolicitud());
						solicitudes.remove(robot.devolverSolicitud());
						robot.finalizarSolicitud();
					}
						
				} else {
					if (!solicitudes.isEmpty()) {
						Solicitud s = solicitudes.get(0);
						robot.planificarRuta(s.getCofreOrigen().getUbicacion()
								, s.getCofreDestino().getUbicacion()
								, factorConsumo
								, s);
					}
				}
			}

			// 3) Verificar estado estable
			if (solicitudes.isEmpty()) {
				System.out.println("Estado estable alcanzado en " + (ciclo + 1) + " ciclos.");
				return ciclo + 1;
			}
			ciclo++;
		}

	    System.out.println("Limite de ciclos alcanzado.");
	    return ciclo;
	}
	// Métodos para planificar rutas, asignar robots, simular ciclos, etc.
	// Pendientes de implementar basados en grafo y cobertura.

	private void asignarCofresProveedoresASolicitudes() {
		int cantMat;
		int cantMatNueva;
		int cantMatVieja;
		for (Cofre c : cofres) {
			for(int i = 0; i < solicitudes.size(); i++) {
				if(!solicitudes.get(i).tieneCofreAsignado()) {					
					cantMat = c.ofrenda(solicitudes.get(i));
					if(cantMat == solicitudes.get(i).getCantidadTotal() || cantMat > solicitudes.get(i).getCantidadTotal()) {
						solicitudes.get(i).setCofreDestino(c);
						break;
					} else if (cantMat > 0 && cantMat < solicitudes.get(i).getCantidadTotal()) {
						solicitudes.get(i).setCofreDestino(c);
						// Registrar que el cofre va a entregar menos y crear un nueva solicitud.
						cantMatVieja = solicitudes.get(i).getCantidadTotal();
						cantMatNueva = cantMatVieja - cantMat;
						solicitudes.get(i).setCantidadTotal(cantMat);
						solicitudes.add(new Solicitud(
								solicitudes.get(i).getCofreOrigen()
								,	solicitudes.get(i).getItem()
								,	cantMatNueva));
						break;
					}
				}
			}
		}
	}
}
