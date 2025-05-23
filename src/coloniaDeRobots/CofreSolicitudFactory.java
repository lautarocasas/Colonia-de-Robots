package coloniaDeRobots;
import coloniaDeRobots.Cofres.Cofre;
import coloniaDeRobots.Cofres.CofreSolicitud;

public class CofreSolicitudFactory extends CofreFactory{
	@Override
	public Cofre crearCofre(int coordX,int coordY) {
		return new CofreSolicitud(coordX,coordY);
	}	
}