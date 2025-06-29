package main.java.coloniaDeRobots;

import java.util.logging.Logger;

public class RobotLogistico extends ElementoLogistico {
    private static final Logger LOGGER = Logger.getLogger(RobotLogistico.class.getName());
    private final int capacidadCarga;
    private final double capacidadBateria;
    private double bateriaActual;

    public RobotLogistico(Ubicacion ubicacion, int capacidadCarga, double capacidadBateria) {
        super(ubicacion);
        if (capacidadCarga <= 0 || capacidadBateria <= 0)
            throw new IllegalArgumentException("Capacidad inválida");
        this.capacidadCarga = capacidadCarga;
        this.capacidadBateria = capacidadBateria;
        this.bateriaActual = capacidadBateria;
        LOGGER.info(() -> String.format("Robot en %s: carga %d, batería %.2f", ubicacion, capacidadCarga, capacidadBateria));
    }

    public boolean puedeTransportar(int cantidad) {
        return cantidad <= capacidadCarga;
    }

    public boolean tieneBateriaPara(double distancia, double factor) {
        return bateriaActual >= distancia * factor;
    }

    public void consumirBateria(double cantidad) {
        bateriaActual = Math.max(0, bateriaActual - cantidad);
        LOGGER.fine(() -> String.format("Robot en %s consumió %.2f batería (resta %.2f)", ubicacion, cantidad, bateriaActual));
    }

    public void recargar() {
        bateriaActual = capacidadBateria;
        LOGGER.info(() -> String.format("Robot en %s recargado a %.2f", ubicacion, capacidadBateria));
    }

    public int getCapacidadCarga() { return capacidadCarga; }
    public double getBateriaActual() { return bateriaActual; }
}

