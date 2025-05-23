package coloniaDeRobots;
import coloniaDeRobots.Cofres.*;

public interface CofreFactory {
    CofreSolicitud crearCofreSolicitud();
    CofreProvisionActiva crearCofreProvisionActiva();
    CofreProvisionPasiva crearCofreProvisionPasiva();
    CofreIntermedio crearCofreIntermedio();
    CofreAlmacenamiento crearCofreAlmacenamiento();
}

