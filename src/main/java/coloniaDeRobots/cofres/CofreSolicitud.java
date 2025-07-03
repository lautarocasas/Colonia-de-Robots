package main.java.coloniaDeRobots.cofres;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.eventos.CofreAccionadoEvent;
import main.java.coloniaDeRobots.eventos.EventBus;

public class CofreSolicitud extends Cofre {
    private final Map<Item,Integer> solicitudes;

    public CofreSolicitud(Ubicacion ubicacion, Map<Item,Integer> inventario, Map<Item,Integer> solicitudes) {
        super(ubicacion, inventario);
        this.solicitudes = Collections.unmodifiableMap(new HashMap<>(solicitudes));
    }

    @Override
    public void accionar(SistemaLogistico sistema) {
    	System.out.println("Accionando solicitud en "+ubicacion);
    	
        for (Map.Entry<Item,Integer> e : solicitudes.entrySet()) {
            Item item = e.getKey();
            int total = e.getValue();
            int recibidos = sistema.getCantidadRecibida(this, item);
            int pendiente = total - recibidos;
            if (pendiente > 0) {
                sistema.registrarSolicitud(new Solicitud(this, item, pendiente));
            }
        }
    }

	public Map<Item, Integer> getSolicitudes() {
		return solicitudes;
	}

	@Override
	public int ofrenda(Solicitud s) {
		return -1;
	}
	
	
}