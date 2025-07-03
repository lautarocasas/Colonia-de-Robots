package main.java.coloniaDeRobots;

import java.util.Objects;
import java.util.logging.Logger;

import main.java.coloniaDeRobots.cofres.Cofre;

public class Solicitud {
    //private static final Logger LOGGER = Logger.getLogger(Solicitud.class.getName());
    private final Cofre origen;
    private Cofre cofreMaterial = null;
    private final Item item;
    private int cantidadTotal;
    private int cantidadRecibida;

    public Solicitud(Cofre origen, Item item, int cantidadTotal) {
        this.origen = Objects.requireNonNull(origen, "Cofre de origen no puede ser null");
        this.item = Objects.requireNonNull(item, "Item no puede ser null");
        if (cantidadTotal <= 0) throw new IllegalArgumentException("Cantidad debe ser positiva");
        this.cantidadTotal = cantidadTotal;
        this.cantidadRecibida = 0;
        //LOGGER.info(() -> String.format("Solicitud creada en %s: %d de %s", origen.getUbicacion(), cantidadTotal, item));
        System.out.println(String.format("Solicitud creada en %s: %d de %s", origen.getUbicacion(), cantidadTotal, item));
    }

    public Item getItem() { return item; }
    public Cofre getCofreOrigen() { return origen; }
    /**
     * Para compatibilidad con ProvisionActiva.
     */
    public Cofre getCofreDestino() { return cofreMaterial; }
    public int getCantidadTotal() { return cantidadTotal; }
    public int getCantidadRecibida() { return cantidadRecibida; }
    public int getCantidadPendiente() { return cantidadTotal - cantidadRecibida; }
    public boolean estaCompletada() { return cantidadRecibida >= cantidadTotal; }
    public boolean tieneCofreAsignado() { return cofreMaterial != null; }
    
    public void setCofreDestino(Cofre destino) {this.cofreMaterial = destino; }
    
    /**
     * Registra una entrega parcial o total.
     */
    public void registrarEntrega(int cantidad) {
        int toAdd = Math.min(cantidad, getCantidadPendiente());
        if (toAdd <= 0) return;
        cantidadRecibida += toAdd;
        //LOGGER.info(() -> String.format("Solicitud en %s recibió %d de %s (pendiente %d)",
           // origen.getUbicacion(), toAdd, item, getCantidadPendiente()));
        System.out.println(String.format("Solicitud en %s recibió %d de %s (pendiente %d)",
            origen.getUbicacion(), toAdd, item, getCantidadPendiente()));
        if (estaCompletada()) {
           // LOGGER.info(() -> String.format("Solicitud en %s COMPLETADA para %s", origen.getUbicacion(), item));
            System.out.println(String.format("Solicitud en %s COMPLETADA para %s", origen.getUbicacion(), item));
        }
    }

	public void setCantidadTotal(int nuevaCantidad) {
		// Validar que no sea negativa
		if(nuevaCantidad<0)
			return;
		
		this.cantidadTotal = nuevaCantidad;
	}
}