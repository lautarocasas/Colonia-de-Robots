package coloniaDeRobots;

public class Ubicacion {
	private int coordX;
	private int coordY;
	
	public Ubicacion(int coordX,int coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	public int getX() {
		return this.coordX;
	}
	
	public int getY() {
		return this.coordY;
	}
}
