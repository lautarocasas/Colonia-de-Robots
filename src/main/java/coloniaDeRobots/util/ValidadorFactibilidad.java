package main.java.coloniaDeRobots.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.cofres.CofreIntermedio;
import main.java.coloniaDeRobots.cofres.CofreProvisionActiva;
import main.java.coloniaDeRobots.cofres.CofreProvisionPasiva;
import main.java.coloniaDeRobots.cofres.CofreSolicitud;
import main.java.logistica.excepciones.SolicitudInviableException;

public class ValidadorFactibilidad {
	
    private ValidadorFactibilidad() {}

    /**
     * Revisa cada cofre de solicitud o intermedio y sus ítems solicitados.
     * @param cofres lista de cofres (accesibles) en el sistema
     * @throws SolicitudInviableException si existen solicitudes sin proveedores
     */
    public static void validarFactibilidad(List<Cofre> cofres) throws SolicitudInviableException {
        Map<Cofre, List<Item>> inviables = new LinkedHashMap<>();
        // Prepara lista de proveedores iniciales
        List<Cofre> proveedores = obtenerCofresProveedores(cofres);
        // Para cada cofre con solicitudes
        for (Cofre cofre : cofres) {
            Map<Item,Integer> solicitudes = obtenerSolicitudes(cofre);
         // Si el cofre no tiene solicitudes, se salta al siguiente.
            if (solicitudes == null) {
            	continue;
            }
            List<Item> itemsFaltantes = obtenerItemsFaltantes(solicitudes,proveedores);
         // Si hay ítems faltantes, el cofre se marca como inviable.
            if (!itemsFaltantes.isEmpty()) {
                inviables.put(cofre, itemsFaltantes);
            }
        }
        haySolicitudesInviables(inviables);
    }
    
    private static List<Cofre> obtenerCofresProveedores(List<Cofre> cofres){
    	return cofres.stream()
    			.filter(cofre-> cofre instanceof CofreProvisionActiva || 
    						    cofre instanceof CofreProvisionPasiva ||
    			                cofre instanceof CofreIntermedio)
    			.collect(Collectors.toList());
    }
    
    private static Map<Item, Integer> obtenerSolicitudes(Cofre cofre) {
        if (cofre instanceof CofreSolicitud) {
            return ((CofreSolicitud) cofre).getSolicitudes();
        } else if (cofre instanceof CofreIntermedio) {
            return ((CofreIntermedio) cofre).getSolicitudes();
        }
        return new LinkedHashMap<>(); // Devuelve un mapa vacío si no es un tipo con solicitudes
    }
    
    private static List<Item> obtenerItemsFaltantes(Map<Item,Integer> solicitudes,List<Cofre> proveedores){
    	List<Item> itemsFaltantes = new ArrayList<>();
    	boolean hayProveedor;
    	for (Item item : solicitudes.keySet()) {
             hayProveedor = false;
            for (Cofre p : proveedores) {
                if (p.getCantidadItem(item) > 0) {
                    hayProveedor = true;
                    break;
                }
            }
            if (!hayProveedor) {
            	itemsFaltantes.add(item);
            }
        }
    	return itemsFaltantes;
    }
      
    private static void haySolicitudesInviables(Map<Cofre, List<Item>> inviables) throws SolicitudInviableException {
    	if (!inviables.isEmpty())  // 4. Si se encontraron solicitudes inviables, lanzar una excepción
    		throw new SolicitudInviableException(construirMensajeSolicitudInviable(inviables));
    }
    
    private static String construirMensajeSolicitudInviable(Map<Cofre, List<Item>> inviables) {
    	StringBuilder sb = new StringBuilder();
        sb.append("Solicitudes inviables detectadas:\n");
        inviables.forEach((cofre, items) -> {
            sb.append(" - Cofre en ")
              .append(cofre.getUbicacion())
              .append(" solicita: ")
              .append(items)
              .append("\n");
        });
        return sb.toString();
    }
}
