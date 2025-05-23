package coloniaDeRobots;
import coloniaDeRobots.Cofres.Cofre;
import coloniaDeRobots.Cofres.CofreAlmacenamiento;

public class CofreAlmacenamientoFactory extends CofreFactory{
	@Override
	public Cofre crearCofre(int coordX,int coordY) {
		return new CofreAlmacenamiento(coordX,coordY);
	}
}
