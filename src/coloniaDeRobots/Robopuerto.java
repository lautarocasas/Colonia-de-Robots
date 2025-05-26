package coloniaDeRobots;

public class Robopuerto {
	private Ubicacion ubicacion;
	private double alcanceMax;
	
	public Robopuerto(int coordX,int coordY,double alcance) {
		this.ubicacion = new Ubicacion(coordX,coordY);
		this.alcanceMax = alcance;
	}
}
