package coloniaDeRobots.cofres;

import java.util.HashMap;
import java.util.Map;

import coloniaDeRobots.Cofre;
import coloniaDeRobots.Item;
import coloniaDeRobots.Ubicacion;

public class CofreIntermedio extends Cofre{
	private Map<Item,Integer> solicitudes = new HashMap<>();

	public CofreIntermedio(Ubicacion ubicacion,Map<Item,Integer> inv,Map<Item,Integer>solicitudes) {
		super(ubicacion,inv);
		this.solicitudes = solicitudes;
	}

	public Map<Item,Integer> getSolicitudes(){
		return solicitudes;
	}
}

