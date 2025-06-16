package coloniaDeRobots;
import java.util.HashMap;

public abstract class ElementoLogistico {
	private Ubicacion ubicacion;
	private HashMap<Cofre,Double> cofresVecinos;
	
	public ElementoLogistico(double coordX, double coordY) {
		this.ubicacion = new Ubicacion(coordX,coordY);
	}	
}
