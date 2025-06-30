package main.java.coloniaDeRobots;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import main.java.coloniaDeRobots.cofres.Cofre;

public class SistemaLogistico {
    private static final Logger LOGGER = Logger.getLogger(SistemaLogistico.class.getName());
    private final List<Cofre> cofres = new ArrayList<>();
    private final List<Robopuerto> puertos = new ArrayList<>();
    private final List<RobotLogistico> robots = new ArrayList<>();
    private final List<Solicitud> solicitudes = new LinkedList<>();
    private final List<Solicitud> solicitudesCompletadas = new ArrayList<>();
    private final double factorConsumo;

    public SistemaLogistico(double factorConsumo) {
        if (factorConsumo <= 0) throw new IllegalArgumentException("Factor de consumo inválido");
        this.factorConsumo = factorConsumo;
    }

    public void agregarCofre(Cofre c) { cofres.add(c); LOGGER.info(() -> "Cofre agregado: " + c.getUbicacion()); }
    public void agregarRobopuerto(Robopuerto p) { puertos.add(p); LOGGER.info(() -> "Robopuerto agregado: " + p.getUbicacion()); }
    public void agregarRobot(RobotLogistico r) { robots.add(r); LOGGER.info(() -> "Robot agregado: " + r.getUbicacion()); }

    public void registrarSolicitud(Solicitud s) {
        solicitudes.add(s);
        LOGGER.info(() -> "Solicitud registrada: " + s.getItem() + " en " + s.getCofreOrigen().getUbicacion());
    }

    public List<Solicitud> obtenerSolicitudesPendientes() {
        return solicitudes.stream()
            .filter(s -> !s.estaCompletada())
            .toList();
    }

    public List<Solicitud> obtenerSolicitudesPendientes(Cofre destino) {
        return solicitudes.stream()
            .filter(s -> s.getCofreDestino().equals(destino) && !s.estaCompletada())
            .toList();
    }


    public int getCantidadRecibida(Cofre c, Item item) {
        return Stream.concat(solicitudes.stream(), solicitudesCompletadas.stream())
            .filter(s -> s.getCofreDestino().equals(c) && s.getItem().equals(item))
            .mapToInt(Solicitud::getCantidadRecibida)
            .sum();
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
            .findFirst()
            .ifPresent(s -> {
                s.registrarEntrega(cantidad);
                if (s.estaCompletada()) {
                    solicitudes.remove(s);
                    solicitudesCompletadas.add(s);  // ← ✅ conservar
                    LOGGER.info(() -> String.format("Solicitud completada y removida: %s en %s", item, destino.getUbicacion()));
                }
            });
    }


    // Métodos para planificar rutas, asignar robots, simular ciclos, etc.
    // Pendientes de implementar basados en grafo y cobertura.
}
