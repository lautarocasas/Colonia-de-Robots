package main.java.coloniaDeRobots.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        List<Cofre> proveedores = new ArrayList<>();
        for (Cofre c : cofres) {
            if (c instanceof CofreProvisionActiva || c instanceof CofreProvisionPasiva || c instanceof CofreIntermedio) {
                proveedores.add(c);
            }
        }

        // Para cada cofre con solicitudes
        for (Cofre c : cofres) {
            Map<Item,Integer> lista = null;
            if (c instanceof CofreSolicitud) {
                lista = ((CofreSolicitud) c).getSolicitudes();
            } else if (c instanceof CofreIntermedio) {
                lista = ((CofreIntermedio) c).getSolicitudes();
            }
            if (lista == null) continue;

            List<Item> faltantes = new ArrayList<>();
            for (Item item : lista.keySet()) {
                boolean hayProveedor = false;
                for (Cofre p : proveedores) {
                    if (p.getCantidadItem(item) > 0) {
                        hayProveedor = true;
                        break;
                    }
                }
                if (!hayProveedor) {
                    faltantes.add(item);
                }
            }
            if (!faltantes.isEmpty()) {
                inviables.put(c, faltantes);
            }
        }

        if (!inviables.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Solicitudes inviables detectadas:\n");
            inviables.forEach((cofre, items) -> {
                sb.append(" - Cofre en ")
                  .append(cofre.getUbicacion())
                  .append(" solicita: ")
                  .append(items)
                  .append("\n");
            });
            throw new SolicitudInviableException(sb.toString());
        }
    }
}
