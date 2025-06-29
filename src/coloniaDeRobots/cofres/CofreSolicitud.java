package coloniaDeRobots.cofres;

import java.util.HashMap;
import java.util.Map;

import coloniaDeRobots.Cofre;
import coloniaDeRobots.Item;
import coloniaDeRobots.SistemaLogistico;
import coloniaDeRobots.Solicitud;
import coloniaDeRobots.Ubicacion;

public class CofreSolicitud extends Cofre{
	private Item itemSolicitado;
	private int cantidadSolicitada;
	private Map<Item,Integer> solicitudes = new HashMap<>();

	public CofreSolicitud(Ubicacion ubicacion,Map<Item,Integer> inv,Map<Item,Integer>solicitudes) {
		super(ubicacion,inv);
		this.solicitudes = (HashMap<Item,Integer>)solicitudes;
	}

	public Map<Item,Integer> getSolicitudes(){
		return solicitudes;
	}
	
    @Override
    public void accion(SistemaLogistico sistema) {
        // Genera la solicitud y la registra en el sistema
        Solicitud solicitud = new Solicitud(this, itemSolicitado, cantidadSolicitada);
        sistema.registrarSolicitud(solicitud);
    }	
}
