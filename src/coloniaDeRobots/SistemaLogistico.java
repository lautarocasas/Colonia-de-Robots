package coloniaDeRobots;
import java.util.ArrayList;
import java.util.List;
import coloniaDeRobots.Cofres.*;

public class SistemaLogistico {
    private List<Cofre> cofres;
    private List<RobotLogistico> robots;
    private List<Robopuerto> robopuertos;
    private List<Solicitud> solicitudesPendientes;
    private int capacidadCargaGlobal;
    private double factorDeConsumoEnergia;
    
    public void registrarSolicitud(Solicitud solicitud) {
        solicitudesPendientes.add(solicitud);
    }

    public List<Solicitud> obtenerSolicitudesPendientes() {
        return new ArrayList<>(solicitudesPendientes);
    }

    public void marcarSolicitudComoSatisfecha(Solicitud solicitud) {
        solicitudesPendientes.remove(solicitud);
    }
}
