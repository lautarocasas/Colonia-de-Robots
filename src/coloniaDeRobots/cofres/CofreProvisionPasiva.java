package coloniaDeRobots.cofres;

import java.util.Map;

import coloniaDeRobots.Cofre;
import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;

public class CofreProvisionPasiva extends Cofre{
	
	public CofreProvisionPasiva(Ubicacion ubicacion,Map<Item,Integer> inv) {
		super(ubicacion,inv);
	}

}
