package coloniaDeRobots.Cofres;

import coloniaDeRobots.Item;
import coloniaDeRobots.SistemaLogistico;
import coloniaDeRobots.Solicitud;

public class CofreSolicitud extends Cofre{
	private Item itemSolicitado;
	private int cantidadSolicitada;
	
	public CofreSolicitud(int coordX,int coordY){
		super(coordX,coordY);
	}
	
    @Override
    public void accion(SistemaLogistico sistema) {
        // Genera la solicitud y la registra en el sistema
        Solicitud solicitud = new Solicitud(this, itemSolicitado, cantidadSolicitada);
        sistema.registrarSolicitud(solicitud);
    }	
}
