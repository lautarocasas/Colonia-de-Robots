package coloniaDeRobots;
import coloniaDeRobots.Cofres.Cofre;
import coloniaDeRobots.Cofres.CofreIntermedio;

public class CofreIntermedioFactory extends CofreFactory{
	@Override
	public Cofre crearCofre(int coordX,int coordY) {
		return new CofreIntermedio(coordX,coordY);
	}	
}
