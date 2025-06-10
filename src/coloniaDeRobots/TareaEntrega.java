package coloniaDeRobots;

import coloniaDeRobots.Cofres.Cofre;

public class TareaEntrega {

    private Cofre origen;                 // Cofre que cede el ítem
    private Cofre destino;                // Cofre solicitante
    private Item item;                    // Tipo de ítem a transportar
    private int cantidad;                 // Unidades a mover
    private EstadoTarea estado;           // ENUM: PENDIENTE, ASIGNADA, EN_CURSO, COMPLETADA, CANCELADA
    private RobotLogistico robotAsignado; // null hasta que se le asigne uno
    private Ruta rutaPlanificada;         // ruta calculada para esta entrega
    private int cicloCreacion;            // ciclo de simulación en que se generó
    private int cicloCompletado;          // ciclo en que terminó (opcional, para métricas)


    public TareaEntrega(Cofre origen,Cofre destino,Item item, int cantidad,int cicloCreacion) {
        this.origen = origen;
        this.destino = destino;
        this.item = item;
        this.cantidad = cantidad;
        this.estado = EstadoTarea.PENDIENTE;
        this.robotAsignado = null;
        this.cicloCreacion = cicloCreacion;
    }

    public Cofre getOrigen()              { return origen; }
    public Cofre getDestino()             { return destino; }
    public Item getItem()                 { return item; }
    public int getCantidad()              { return cantidad; }
    public EstadoTarea getEstado()        { return estado; }
    public RobotLogistico getRobotAsignado() { return robotAsignado; }
    public Ruta getRutaPlanificada()      { return rutaPlanificada; }
    public int getCicloCreacion()         { return cicloCreacion; }
    public int getCicloCompletado()       { return cicloCompletado; }

     public void asignarARobot(RobotLogistico r) {
        if (estado == EstadoTarea.PENDIENTE) {
            this.robotAsignado = r;
            this.estado        = EstadoTarea.ASIGNADA;
        }
    }

    public void marcarEnCurso() {
        if (estado == EstadoTarea.ASIGNADA) {
            this.estado = EstadoTarea.EN_CURSO;
        }
    }

    public void marcarCompletada(int cicloActual) {
        if (estado == EstadoTarea.EN_CURSO) {
            this.estado           = EstadoTarea.COMPLETADA;
            this.cicloCompletado  = cicloActual;
        }
    }

    public void cancelar() {
        if (estado == EstadoTarea.PENDIENTE || estado == EstadoTarea.ASIGNADA) {
            this.estado = EstadoTarea.CANCELADA;
        }
    }

    public boolean esAsignable() {
        return estado == EstadoTarea.PENDIENTE && robotAsignado == null;
    }

    public boolean estaFinalizada() {
        return estado == EstadoTarea.COMPLETADA || estado == EstadoTarea.CANCELADA;
    }

    public void setRutaPlanificada(Ruta ruta) {
        this.rutaPlanificada = ruta;
    }

}

enum EstadoTarea {
    PENDIENTE,
    ASIGNADA,
    EN_CURSO,
    COMPLETADA,
    CANCELADA
}
