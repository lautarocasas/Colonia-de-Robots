package main.java.coloniaDeRobots;

import java.util.Locale;
import java.util.Objects;

public class Ubicacion {
    private final double x;
    private final double y;

    public Ubicacion(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public double calcularDistanciaA(Ubicacion otra) {
        double dx = x - otra.x;
        double dy = y - otra.y;
        return Math.hypot(dx, dy);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "(%.2f, %.2f)", x, y);
    }

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ubicacion other = (Ubicacion) obj;
		return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
				&& Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}
    
    
}