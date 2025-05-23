package coloniaDeRobots;
import coloniaDeRobots.Cofres.Cofre;
import coloniaDeRobots.Cofres.CofreProvisionActiva;

public class CofreProvisionActivaFactory extends CofreFactory{
	@Override
	public Cofre crearCofre(int coordX,int coordY) {
		return new CofreProvisionActiva(coordX,coordY);
	}	
}