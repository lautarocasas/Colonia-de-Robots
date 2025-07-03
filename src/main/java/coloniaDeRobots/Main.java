package main.java.coloniaDeRobots;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.util.MetricsCollector;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.io.GestorArchivos;

public class Main {
	private static final double FACTOR_CONSUMO = 1.0;

    public static void main(String[] args) {
        MetricsCollector metrics = new MetricsCollector();
        try {
            // 1) Carga toda la configuración desde JSON
            GestorArchivos loader = new GestorArchivos();
            SistemaLogistico sistema = loader.cargarDesdeArchivo(
                "src/main/resources/config_stress.json",
                FACTOR_CONSUMO
            );

            // 2) Ejecuta la simulación
            int ciclosEjecutados = sistema.run();

            // 4) Muestra inventarios finales de cada cofre
            System.out.println("\n=== Inventarios finales de cofres ===");
            for (Cofre c : sistema.getCofres()) {
                System.out.printf(
                    "- %s [%s]: %s%n",
                    c.getClass().getSimpleName(),
                    c.getUbicacion(),
                    c.getInventario()
                );
            }

            // 5) Cuántas solicitudes se completaron
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
