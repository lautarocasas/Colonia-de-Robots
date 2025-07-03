package main.java.coloniaDeRobots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;
import main.java.coloniaDeRobots.cofres.CofreIntermedio;
import main.java.coloniaDeRobots.cofres.CofreProvisionActiva;
import main.java.coloniaDeRobots.cofres.CofreProvisionPasiva;
import main.java.coloniaDeRobots.cofres.CofreSolicitud;
import main.java.logistica.excepciones.ExcepcionLogistica;

public class SistemaLogistico {
	// private static final Logger LOGGER =
	// Logger.getLogger(SistemaLogistico.class.getName());
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
	    // Buscamos la primera posición donde la prioridad del
	    // cofre existente sea mayor que la del nuevo:
	    int p = prioridad(c);
	    int i;
	    for (i = 0; i < cofres.size(); i++) {
	        if (prioridad(cofres.get(i)) > p) break;
	    }
	    cofres.add(i, c);
	    System.out.println("Cofre agregado en pos " + i + ": " + c.getUbicacion());
	}

	private int prioridad(Cofre c) {
	    if (c instanceof CofreProvisionActiva)   return 1;
	    if (c instanceof CofreProvisionPasiva)   return 2;
	    if (c instanceof CofreIntermedio)        return 3;
	    // Almacenamiento (aunque normalmente no participaría)
	    return 4;
	}


	public void registrarSolicitud(Solicitud s) {
		solicitudes.add(s);
		System.out.println("Registrando solicitud ");
		// EventBus.getDefault().post(new SolicitudRegistradaEvent(s));
	}

	public void agregarRobopuerto(Robopuerto p) {
		puertos.add(p);
		System.out.println("Robopuerto agregado: " + p.getUbicacion());
		// LOGGER.info(() -> "Robopuerto agregado: " + p.getUbicacion());
	}

	public void agregarRobot(RobotLogistico r) {
		robots.add(r);
		// LOGGER.info(() -> "Robot agregado: " + r.getUbicacion());
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
	        .filter(s ->
	            Objects.equals(s.getCofreDestino(), c)
	            && s.getItem().equals(item)
	        )
	        .mapToInt(Solicitud::getCantidadRecibida)
	        .sum();
	}

	/**
	 * Genera un transporte de ítems de un cofre origen a un cofre destino
	 * para la solicitud indicada.
	 */
	public void generarTransporte(
		    Cofre origen,
		    Cofre destino,
		    Item item,
		    int cantidad,
		    Solicitud solicitud
		) {
		    System.out.printf("Generando transporte: %d de %s de %s a %s%n",
		        cantidad, item, origen.getUbicacion(), destino.getUbicacion());

		    // 1) registro de la entrega
		    solicitud.registrarEntrega(cantidad);

		    // 2) si se completó, moverla a la lista de completadas
		    if (solicitud.estaCompletada()) {
		        solicitudes.remove(solicitud);
		        solicitudesCompletadas.add(solicitud);
		        System.out.printf(
		          "Solicitud COMPLETADA y removida: %s en %s%n",
		          item, destino.getUbicacion()
		        );
		    }
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
	 * 
	 * @throws ExcepcionLogistica
	 */
	public int run() throws ExcepcionLogistica {
		int ciclo = 0;

		// Obtener las solicitudes de los cofres.
		// 1) Cofres actúan
		for (Cofre c : cofres) {
	        if (c instanceof CofreSolicitud) {
	            c.accionar(this);
	        }
	    }

		// Asignar los cofres de provision activa o pasiva
		asignarCofresProveedoresASolicitudes();

		// 3) Bucle de robots hasta que no queden solicitudes
		while (!solicitudes.isEmpty()) {
		    ciclo++;
		    System.out.printf("Ciclo %d — solicitudes pendientes: %d%n",
		                      ciclo, solicitudes.size());

		    // 1) Avanzar los robots ocupados y recolectar los libres
		    List<RobotLogistico> libres = new ArrayList<>();
		    for (RobotLogistico robot : robots) {
		        if (robot.tieneTarea()) {
		            robot.avanzar(factorConsumo);
		            if (robot.tareaCompleta()) {
		                Solicitud done = robot.devolverSolicitud();
		                solicitudesCompletadas.add(done);
		                solicitudes.remove(done);
		                robot.finalizarSolicitud();
		            }
		        } else {
		            libres.add(robot);
		        }
		    }

		    // 2) Intentar planificar la próxima solicitud con cualquier robot libre
		    boolean pudoAsignar = false;
		    if (!solicitudes.isEmpty()) {
		        Solicitud siguiente = solicitudes.get(0);
		        for (RobotLogistico robot : libres) {
		            if (robot.planificarRuta(
		                    siguiente.getCofreOrigen().getUbicacion(),
		                    siguiente.getCofreDestino().getUbicacion(),
		                    factorConsumo,
		                    siguiente)) {
		                // Solo la removemos si la planificación tuvo éxito
		                solicitudes.remove(0);
		                pudoAsignar = true;
		                break;
		            }
		        }
		    }

		    // 3) Si no pudimos asignar ninguna ruta nueva, es deadlock
		    if (!pudoAsignar && libres.size() == robots.size()) {
		        // todos estaban libres y ninguno pudo planificar → no se puede avanzar
		        System.err.println("NO ESTABLE: deadlock detectado, solicitudes inalcanzables.");
		        break;
		    }
		}
		// 4) Fase final: vaciado de provisiones activas
	    if (solicitudes.isEmpty()) {
	        System.out.println("Todas las solicitudes completadas. Vaciando provisiones activas...");
	        boolean ok = vaciarProvisionesActivasEnAlmacen();
	        if (ok) {
	            System.out.printf("ESTABLE tras %d ciclos.%n", ciclo);
	        } else {
	            System.err.println("NO ESTABLE: fallo al vaciar provisiones activas.");
	        }
	    }

	    return ciclo;
	}
	
	/**
	 * Traslada todo el inventario de los cofres de Provisión Activa
	 * al primer cofre de Almacenamiento encontrado.
	 *
	 * @return true si se vaciaron todos los activos correctamente;
	 *         false si no hay cofres de almacenamiento o falla alguna retirada.
	 */
	private boolean vaciarProvisionesActivasEnAlmacen() {
	    // 1) Obtener lista de cofres activos
	    List<Cofre> activos = cofres.stream()
	        .filter(c -> c instanceof CofreProvisionActiva)
	        .collect(Collectors.toList());

	    // 2) Obtener lista de cofres de almacenamiento
	    List<Cofre> almacenes = cofres.stream()
	        .filter(c -> c instanceof CofreAlmacenamiento)
	        .collect(Collectors.toList());

	    // Si no hay destinos de almacenamiento, no podemos vaciar
	    if (almacenes.isEmpty()) {
	        System.err.println("No hay cofres de almacenamiento disponibles.");
	        return false;
	    }

	    Cofre destino = almacenes.get(0);

	    // 3) Para cada cofre activo, trasladar todo su inventario
	    for (Cofre origen : activos) {
	        // Copiamos la vista del inventario para evitar ConcurrentModification
	        Map<Item,Integer> snapshot = new HashMap<>(origen.getInventario());

	        for (Map.Entry<Item,Integer> e : snapshot.entrySet()) {
	            Item item = e.getKey();
	            int cantidad = e.getValue();

	            // Intentamos retirar toda la cantidad en el origen
	            boolean retirado = origen.retirarItem(item, cantidad);
	            if (!retirado) {
	                System.err.printf(
	                    "Error: no se pudo retirar %d de %s en %s%n",
	                    cantidad, item, origen.getUbicacion()
	                );
	                return false;
	            }

	            // Agregamos esa misma cantidad al cofre de almacenamiento
	            destino.agregarItem(item, cantidad);
	            System.out.printf(
	                "Movidos %d de %s de %s → %s%n",
	                cantidad, item,
	                origen.getUbicacion(),
	                destino.getUbicacion()
	            );
	        }
	    }

	    return true;
	}

	// Métodos para planificar rutas, asignar robots, simular ciclos, etc.
	// Pendientes de implementar basados en grafo y cobertura.

	/**
	 * Asigna cofres proveedores a cada solicitud pendiente, respetando
	 * el orden de prioridad que ya tiene la lista `cofres`:
	 *   1) CofreProvisionActiva
	 *   2) CofreProvisionPasiva
	 *   3) CofreIntermedio
	 * (los de almacenamiento no participan aquí)
	 * 
	 * Al entregar, actualiza cantidadRecibida y retira stock del cofre.
	 * Al final, elimina de la lista todas las solicitudes completadas.
	 */
	/**
	 * Asigna cofres proveedores a cada solicitud pendiente, respetando
	 * la prioridad de la lista `cofres` (activa → pasiva → intermedio),
	 * y delega en generarTransporte() tanto la entrega como el paso a completadas.
	 */
	private void asignarCofresProveedoresASolicitudes() {
	    // Hacemos copia para iterar sin riesgo de ConcurrentModification
	    List<Solicitud> pendientes = new ArrayList<>(solicitudes);

	    for (Solicitud sol : pendientes) {
	        if (sol.estaCompletada()) continue;

	        for (Cofre c : cofres) {
	            // Solo proveedores: activa, pasiva o intermedio
	            if (!(c instanceof CofreProvisionActiva
	               || c instanceof CofreProvisionPasiva
	               || c instanceof CofreIntermedio)) {
	                continue;
	            }

	            int disponible = c.getCantidadItem(sol.getItem());
	            if (disponible <= 0) continue;

	            int faltante = sol.getCantidadPendiente();
	            int aEntregar = Math.min(disponible, faltante);

	            // Retiramos stock y generamos el “transporte”
	            if (c.retirarItem(sol.getItem(), aEntregar)) {
	                // Nota: pasamos la propia solicitud
	                generarTransporte(
	                    c,                           // origen
	                    sol.getCofreOrigen(),        // destino
	                    sol.getItem(),
	                    aEntregar,
	                    sol                          // solicitud
	                );
	            }

	            if (sol.estaCompletada()) {
	                // Ya está completo: pasamos al siguiente pedido
	                break;
	            }
	        }

	        if (!sol.estaCompletada()) {
	            System.err.printf(
	                "⚠️ No se pudo satisfacer completamente %d de %s en %s (faltan %d)%n",
	                sol.getCantidadTotal(),
	                sol.getItem(),
	                sol.getCofreOrigen().getUbicacion(),
	                sol.getCantidadPendiente()
	            );
	        }
	    }
	}

	public List<Solicitud> getSolicitudesCompletadas() {
		// TODO Auto-generated method stub
		return solicitudesCompletadas;
	}

}
