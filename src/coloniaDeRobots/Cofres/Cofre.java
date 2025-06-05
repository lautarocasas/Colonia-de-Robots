package coloniaDeRobots.Cofres;
import java.util.HashMap;
import coloniaDeRobots.Ubicacion;

public abstract class Cofre {
	private Ubicacion ubicacion;
	private HashMap<String,Integer> inventario; //Mapa que asigna a cada itemId una cantidad
	
	Cofre(int coordX,int coordY){
		this.ubicacion = new Ubicacion(coordX,coordY);
		this.inventario = new HashMap<String,Integer>();
	}
}
