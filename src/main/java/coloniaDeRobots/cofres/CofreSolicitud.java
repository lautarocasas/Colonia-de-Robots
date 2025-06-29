package main.java.coloniaDeRobots.cofres;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;

public class CofreSolicitud extends Cofre {
    private final Map<Item,Integer> solicitudes;

    public CofreSolicitud(Ubicacion ubicacion, Map<Item,Integer> inventario, Map<Item,Integer> solicitudes) {
        super(ubicacion, inventario);
        this.solicitudes = Collections.unmodifiableMap(new HashMap<>(solicitudes));
    }

    @Override
    public void accionar(SistemaLogistico sistema) {
        Logger.getLogger(getClass().getName()).info(() -> "Accionando Solicitud en " + ubicacion);
        for (Map.Entry<Item,Integer> e : solicitudes.entrySet()) {
            Item item = e.getKey();
            int total = e.getValue();
            int recibidos = sistema.getCantidadRecibida(this, item);
            int pendiente = total - recibidos;
            if (pendiente > 0) {
                sistema.registrarSolicitud(new Solicitud(this, item, pendiente));
                Logger.getLogger(getClass().getName()).info(() ->
                    String.format("[%s] Solicita %d de %s", ubicacion, pendiente, item)
                );
            }
        }
    }

	public Map<Item, Integer> getSolicitudes() {
		return solicitudes;
	}
}