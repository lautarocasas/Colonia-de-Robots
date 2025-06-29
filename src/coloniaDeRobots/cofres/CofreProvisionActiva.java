package coloniaDeRobots.cofres;

import java.util.List;

import coloniaDeRobots.*;

public class CofreProvisionActiva extends Cofre {
	public CofreProvisionActiva(int coordX,int coordY){
		super(coordX,coordY);
	}
	
	@Override
    public void accion(SistemaLogistico sistema) {
        // Buscar solicitudes en el sistema para ítems que yo tengo
        List<Solicitud> solicitudes = sistema.obtenerSolicitudesPendientes();

        for (Solicitud s : solicitudes) {
        	Item itemSolicitado = s.getItem();
        	int cantidadDisponible = this.getCantidadItem(itemSolicitado);
        	
        	if(cantidadDisponible > 0 && s.getCantidadPendiente() > 0 ) {
        		//Si tengo stock del item que me pidieron y faltan items para completar la solicitud, envio la cantidad necesaria
        		int cantidadAEnviar = Math.min(cantidadDisponible, s.getCantidadPendiente());
        		
        	}
        }
}
}
