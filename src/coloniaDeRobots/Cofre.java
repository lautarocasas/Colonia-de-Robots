package coloniaDeRobots;
import java.util.HashMap;
import java.util.Map;

public abstract class Cofre{
	protected Ubicacion ubicacion;
	protected HashMap<Item,Integer> inventario; //Mapa que asigna a cada itemId una cantidad
	
	public Cofre(Ubicacion ubicacion,Map<Item,Integer> inv) {
		this.ubicacion = ubicacion;
		this.inventario = (HashMap<Item,Integer>)inv;
	}
	
	public void accionar(SistemaLogistico sistema) {
		//El cofre debe tener acceso al estado general del sistema
	}
	
	public int getCantidadItem(Item item) {
		int cant = this.inventario.getOrDefault(item,0);	// Si existe el item en el inventario, se obtiene la cantidad, sino, se obtiene 0
		return cant;
	}
	
	public boolean retirarItem(Item item, int cantSolicitada) {
		int cantActual = this.getCantidadItem(item);
		if (cantActual < cantSolicitada) {
			return false;
		}
		inventario.put(item, cantActual - cantSolicitada);
		if (this.getCantidadItem(item) == 0)
			inventario.remove(item);
		return true;
	}
    
    public void agregarItem(Item item, int cantidad) {
    	int cantActual = this.getCantidadItem(item);
    	inventario.put(item, cantActual+ cantidad);
    }

	public void accion(SistemaLogistico sistema) {
		// TODO Auto-generated method stub
		
	}
	
	public Ubicacion getUbicacion() {
		return this.ubicacion;
	}
	
	public Map<Item,Integer> getInventario(){
		return this.inventario;
	}
}
