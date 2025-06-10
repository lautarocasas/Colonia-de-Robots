package coloniaDeRobots;

import coloniaDeRobots.Cofres.Cofre;

public class Solicitud {
    private Cofre cofreSolicitante;
    private Item item;
    private int cantidadSolicitada;
    private int cantidadRecibida;		// Una solicitud puede completarse en partes haciendo varios viajes de robot
    private boolean completada;
    
    public Solicitud(Cofre cofreSolicitante, Item item, int cantidadSolicitada) {
        this.cofreSolicitante = cofreSolicitante;
        this.item = item;
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadRecibida = 0;
        this.completada = false;
    }
    
    public void registrarEntrega(int cantidad) {
        this.cantidadRecibida += cantidad;
        if (cantidadRecibida >= cantidadSolicitada) {
            this.completada = true;
        }
    }
    
    public boolean estaCompletada() {
        return completada;
    }

    public int getCantidadPendiente() {
        return Math.max(cantidadSolicitada - cantidadRecibida, 0);
    }
    
    public Cofre getCofreSolicitante() { return cofreSolicitante; }

    public Item getItem() { return item; }

    public int getCantidadSolicitada() { return cantidadSolicitada; }

    public int getCantidadRecibida() { return cantidadRecibida; }
}
