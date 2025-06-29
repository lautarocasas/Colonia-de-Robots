package coloniaDeRobots.cofres;

import java.util.Map;

import coloniaDeRobots.Cofre;
import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;

public class CofreAlmacenamiento extends Cofre {
	
	public CofreAlmacenamiento(Ubicacion ubicacion,Map<Item,Integer> inv) {
		super(ubicacion,inv);
	}
}
