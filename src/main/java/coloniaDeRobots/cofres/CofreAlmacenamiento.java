package main.java.coloniaDeRobots.cofres;

import java.util.Map;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;

public class CofreAlmacenamiento extends Cofre {
    public CofreAlmacenamiento(Ubicacion ubicacion, Map<Item,Integer> inventario) {
        super(ubicacion, inventario);
    }

    @Override
    public void accionar(SistemaLogistico sistema) {
        // No genera acción activa ni solicitud.
        // Solo recibe transports.
    }

	@Override
	public int ofrenda(Solicitud s) {

		return -1;
	}
    
    
}