package main.java.coloniaDeRobots.cofres;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
		Logger.getLogger(getClass().getName()).info(() -> "Accionando Provisión Activa en " + ubicacion);
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
				Logger.getLogger(getClass().getName()).info(() -> String.format("[%s] Envío %d de %s a %s", ubicacion,
						aEnviar, item, s.getCofreDestino().getUbicacion()));
			}
		}
	}

}