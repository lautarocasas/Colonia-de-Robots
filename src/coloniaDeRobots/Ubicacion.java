package coloniaDeRobots;

public class Ubicacion {
	private double coordX;
	private double coordY;
	
	public Ubicacion(double coordX,double coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
    public double calcularDistancia(Ubicacion otra) {
        double dx = this.coordX - otra.coordX;
        double dy = this.coordY - otra.coordY;
        return Math.hypot(dx, dy);
    }
	
	public double getX() {
		return this.coordX;
	}
	
	public double getY() {
		return this.coordY;
	}
	
	public void setX(double coordX) {
		this.coordX = coordX;
	}
	
	public void setY(double coordY) {
		this.coordY = coordY;
	}
	
}
