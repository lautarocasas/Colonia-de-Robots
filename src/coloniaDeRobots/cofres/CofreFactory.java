package coloniaDeRobots.Cofres;

import coloniaDeRobots.Cofre;

public class CofreFactory {
	
	public static Cofre crearCofre(TipoCofre tipoCofre){
		return switch (tipoCofre) {
			case ACTIVO -> new CofreProvisionActiva(0, 0);
			case PASIVO -> new CofreProvisionPasiva(0, 0);
			case SOLICITUD -> new CofreSolicitud(0, 0);
			case INTERMEDIO -> new CofreIntermedio(0, 0);
			case ALMACENAMIENTO -> new CofreAlmacenamiento(0, 0);
			default -> throw new IllegalArgumentException("Tipo de cofre no soportado: " + tipoCofre);
		};
	}

}
