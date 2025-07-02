package main.java.coloniaDeRobots.cofres;

import java.util.List;
import java.util.Map;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Solicitud;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.eventos.CofreAccionadoEvent;
import main.java.coloniaDeRobots.eventos.EventBus;

public class CofreProvisionActiva extends Cofre {
	public CofreProvisionActiva(Ubicacion ubicacion, Map<Item, Integer> inventario) {
		super(ubicacion, inventario);
	}

	@Override
	public void accionar(SistemaLogistico sistema) {
		EventBus.getDefault().post(new CofreAccionadoEvent(this));

		List<Solicitud> solicitudes = sistema.obtenerSolicitudesPendientes();

		for (Solicitud s : solicitudes) {
			Item item = s.getItem();
			int pendiente = s.getCantidadPendiente();
			int disponible = getCantidadItem(item);

			if (pendiente <= 0 || disponible <= 0)
				continue;

			int aEnviar = Math.min(disponible, pendiente);
			if (retirarItem(item, aEnviar)) {
				sistema.generarTransporte(this, s.getCofreDestino(), item, aEnviar);
			}
		}
	}

	@Override
	public int ofrenda(Solicitud s) {
		if(inventario.containsKey(s.getItem())) {
			return inventario.get(s.getItem());			
		}
		return 0;
	}

}