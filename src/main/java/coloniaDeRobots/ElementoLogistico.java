package main.java.coloniaDeRobots;
import java.util.Objects;

/*public abstract class ElementoLogistico {
	private Ubicacion ubicacion;
	private HashMap<Cofre,Double> cofresVecinos;
	
	public ElementoLogistico(double coordX, double coordY) {
		this.ubicacion = new Ubicacion(coordX,coordY);
	}	
}*/

public abstract class ElementoLogistico {
    protected Ubicacion ubicacion;

    public ElementoLogistico(Ubicacion ubicacion) {
        this.ubicacion = Objects.requireNonNull(ubicacion);
    }

    public Ubicacion getUbicacion() { return ubicacion; }
}

