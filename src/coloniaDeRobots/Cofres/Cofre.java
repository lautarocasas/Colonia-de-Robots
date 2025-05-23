package coloniaDeRobots.Cofres;
import java.util.HashMap;

public abstract class Cofre {
	private int coordX;
	private int coordY;
	private HashMap<String,Integer> inventario; //Mapa que asigna a cada itemId una cantidad
	
	Cofre(int coordX,int coordY){
		this.coordX = coordX;
		this.coordY = coordY;
		this.inventario = new HashMap<String,Integer>();
	}
}
