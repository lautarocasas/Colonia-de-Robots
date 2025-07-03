package main.java.coloniaDeRobots;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.util.MetricsCollector;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.io.GestorArchivos;

public class Main {
	private static final double FACTOR_CONSUMO = 1.0;

    public static void main(String[] args) {
        try {
            // Cargamos toda la configuración desde JSON
            GestorArchivos loader = new GestorArchivos();
            SistemaLogistico sistema = loader.cargarDesdeArchivo(
                "src/main/resources/config.json",
                FACTOR_CONSUMO
            );

            // Ejecutamos la simulación
            sistema.run();

            // Mostramos inventarios finales de cada cofre
            System.out.println("\n=== Inventarios finales de cofres ===");
            for (Cofre c : sistema.getCofres()) {
                System.out.printf(
                    "- %s [%s]: %s%n",
                    c.getClass().getSimpleName(),
                    c.getUbicacion(),
                    c.getInventario()
                );
            }

            // Solicitudes completadas
            System.out.printf(
                "%nSolicitudes completadas: %d%n",
                sistema.getSolicitudesCompletadas().size()
            );

        } catch (ExcepcionLogistica e) {
            System.err.println("Error al inicializar o correr la simulación:");
            e.printStackTrace();
        }
    }

}
