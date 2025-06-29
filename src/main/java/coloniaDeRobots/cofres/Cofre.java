package main.java.coloniaDeRobots.cofres;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Ubicacion;

public abstract class Cofre{
    private static final Logger LOGGER = Logger.getLogger(Cofre.class.getName());
    protected final Ubicacion ubicacion;
    protected final Map<Item,Integer> inventario; //Mapa que asigna a cada itemId una cantidad
	
    public Cofre(Ubicacion ubicacion, Map<Item,Integer> inventario) {
        this.ubicacion = ubicacion;
        this.inventario = new HashMap<>(inventario);
        LOGGER.info(() -> String.format("Cofre creado en %s con inventario %s", ubicacion, this.inventario));
    }
	
	
	/**
     * Cantidad actual del ítem.
     */
	public int getCantidadItem(Item item) {
		return inventario.getOrDefault(item, 0);	// Si existe el item en el inventario, se obtiene la cantidad, sino, se obtiene 0
	}

	public void accion(SistemaLogistico sistema) {
		// TODO Auto-generated method stub
		
	}
	/**
     * Acción que realiza el cofre en cada ciclo de simulación.
     */
    public abstract void accionar(SistemaLogistico sistema); //El cofre debe tener acceso al estado general del sistema

    

    /**
     * Retira hasta cantSolicitada unidades, retorna true si extrajo todo.
     */
    public boolean retirarItem(Item item, int cantSolicitada) {
        int actual = getCantidadItem(item);
        int extraido = Math.min(actual, cantSolicitada);
        if (extraido <= 0) {
            LOGGER.fine(() -> String.format("[%s] Sin stock para retirar %d de %s", ubicacion, cantSolicitada, item));
            return false;
        }
        inventario.put(item, actual - extraido);
        if (inventario.get(item) == 0) inventario.remove(item);
        LOGGER.info(() -> String.format("[%s] Retirado %d de %s (restan %d)", ubicacion, extraido, item, getCantidadItem(item)));
        return true;
    }

    /**
     * Agrega cantidad unidades del ítem.
     */
    public void agregarItem(Item item, int cantidad) {
        int actual = getCantidadItem(item);
        inventario.put(item, actual + cantidad);
        LOGGER.info(() -> String.format("[%s] Agregado %d de %s (total %d)", ubicacion, cantidad, item, getCantidadItem(item)));
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public Map<Item,Integer> getInventario() {
        return Collections.unmodifiableMap(inventario); // Protegemos la integridad del objeto
    }
}