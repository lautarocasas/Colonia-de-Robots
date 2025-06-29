package coloniaDeRobots;


public class Item {
	private String id;
	private String nombre;
	
	public Item(String nombre) {
		this.nombre = nombre;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getNombre(){
		return this.nombre;
	}
	
}
