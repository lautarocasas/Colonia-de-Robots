package coloniaDeRobots.Cofres;
import java.util.HashMap;

import coloniaDeRobots.*;

public abstract class Cofre {
	protected Ubicacion ubicacion;
	protected HashMap<Item,Integer> inventario; //Mapa que asigna a cada itemId una cantidad
	
	Cofre(int coordX,int coordY){
		this.ubicacion = new Ubicacion(coordX,coordY);
		this.inventario = new HashMap<Item,Integer>();
	}
	
	public void accion(SistemaLogistico sistema) {
		//El cofre debe tener acceso al estado general del sistema
	}
	
	public int getCantidadItem(Item item) {
		int cant = this.inventario.getOrDefault(item,0);	// Si existe el item en el inventario, se obtiene la cantidad, sino, se obtiene 0
		return cant;
	}
	
    public boolean retirarItem(Item item, int cantidad) {
        int cantActual = this.getCantidadItem(item);
        if (cantActual >= cantidad) {
        	inventario.put(item, cantActual - cantidad);
        	if (this.getCantidadItem(item) == 0) //Si al restar la cantidad solicitada no queda ningun item, se elimina el item del mapa
        		inventario.remove(item);
        	return true;        	
        }else        
        	return false;
    }
    
    public void agregarItem(Item item, int cantidad) {
    	int cantActual = this.getCantidadItem(item);
    	inventario.put(item, cantActual+ cantidad);
    }
}
