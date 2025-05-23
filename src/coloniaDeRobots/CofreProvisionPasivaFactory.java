package coloniaDeRobots;
import coloniaDeRobots.Cofres.Cofre;
import coloniaDeRobots.Cofres.CofreProvisionPasiva;

public class CofreProvisionPasivaFactory extends CofreFactory{
	@Override
	public Cofre crearCofre(int coordX,int coordY) {
		return new CofreProvisionPasiva(coordX,coordY);
	}	
}