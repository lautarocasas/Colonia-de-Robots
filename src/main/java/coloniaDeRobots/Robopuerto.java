package main.java.coloniaDeRobots;

public class Robopuerto extends ElementoLogistico {
    private final double alcance;

    public Robopuerto(Ubicacion ubicacion, double alcance) {
        super(ubicacion);
        if (alcance <= 0) throw new IllegalArgumentException("Alcance debe ser positivo");
        this.alcance = alcance;
    }

    public double getAlcance() { return alcance; }

    public boolean cubre(Ubicacion u) {
        return ubicacion.calcularDistanciaA(u) <= alcance;
    }
}
