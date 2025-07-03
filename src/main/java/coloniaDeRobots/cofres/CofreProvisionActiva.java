package main.java.coloniaDeRobots.cofres;

import java.util.List;
import java.util.Map;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;


public class CofreProvisionActiva extends Cofre {
	public CofreProvisionActiva(Ubicacion ubicacion, Map<Item, Integer> inventario) {
		super(ubicacion, inventario);
	}

	@Override
	public void accionar(SistemaLogistico sistema) {
	    System.out.println("Accionando en un cofre de provisión activa");
	    List<Solicitud> pendientes = sistema.obtenerSolicitudesPendientes();

	    for (Solicitud s : pendientes) {
	        Item item = s.getItem();
	        int faltante = s.getCantidadPendiente();
	        int disponible = getCantidadItem(item);

	        if (disponible <= 0 || faltante <= 0) continue;

	        int aEnviar = Math.min(disponible, faltante);
	        if (retirarItem(item, aEnviar)) {
	            // Ahora le paso la solicitud en curso:
	            sistema.generarTransporte(
	                this,
	                s.getCofreDestino(),
	                item,
	                aEnviar,
	                s               // <— aquí
	            );
	        }
	    }
	}


}