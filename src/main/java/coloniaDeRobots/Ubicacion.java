package main.java.coloniaDeRobots;

public class Ubicacion {
    private final double x;
    private final double y;

    public Ubicacion(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public double distanciaA(Ubicacion otra) {
        double dx = x - otra.x;
        double dy = y - otra.y;
        return Math.hypot(dx, dy);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}