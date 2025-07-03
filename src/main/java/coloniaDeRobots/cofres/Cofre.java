package main.java.coloniaDeRobots.cofres;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.ElementoLogistico;
import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;

public abstract class Cofre extends ElementoLogistico {
	protected final Map<Item, Integer> inventario; // Mapa que asigna a cada itemId una cantidad

	public Cofre(Ubicacion ubicacion, Map<Item, Integer> inventario) {
		super(ubicacion);
		this.inventario = new HashMap<>(inventario);
		System.out.println(String.format("Cofre creado en %s con inventario %s", ubicacion, this.inventario));
	}

	/**
	 * Cantidad actual del ítem.
	 */
	public int getCantidadItem(Item item) {
		return inventario.getOrDefault(item, 0); // Si existe el item en el inventario, se obtiene la cantidad, sino, se
													// obtiene 0
	}

	/**
	 * Acción que realiza el cofre en cada ciclo de simulación.
	 */
	public abstract void accionar(SistemaLogistico sistema); // El cofre debe tener acceso al estado general del sistema

	 /**
     * Retira exactamente cantSolicitada unidades si hay stock suficiente.
     * @return true si se retiró la cantidad solicitada; false si no había stock.
     */
	public boolean retirarItem(Item item, int cantSolicitada) {
		int actual = getCantidadItem(item);
		// Solo retira si hay suficiente stock para la solicitud completa
		if (actual < cantSolicitada) {
			System.out.println(String.format("[%s] Stock insuficiente para retirar %d de %s (solo %d disponibles)",
					ubicacion, cantSolicitada, item, actual));
			return false;
		}
		// Retira exactamente la cantidad solicitada
		int restante = actual - cantSolicitada;
		if (restante > 0) {
			inventario.put(item, restante);
		} else {
			inventario.remove(item);
		}
		System.out.println(String.format("[%s] Retirado %d de %s (restan %d)", ubicacion, cantSolicitada, item,
				getCantidadItem(item)));
		return true;
	}

	/**
	 * Agrega cantidad unidades del ítem.
	 */
	public void agregarItem(Item item, int cantidad) {
		int actual = getCantidadItem(item);
		inventario.put(item, actual + cantidad);
		System.out.println(String.format("[%s] Agregado %d de %s (total %d)", ubicacion, cantidad, item,
				getCantidadItem(item)));
	}

	public Ubicacion getUbicacion() {
		return super.getUbicacion();
	}

	public Map<Item, Integer> getInventario() {
		return Collections.unmodifiableMap(inventario); // Protegemos la integridad del objeto
	}
}