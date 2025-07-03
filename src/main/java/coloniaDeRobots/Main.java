package main.java.coloniaDeRobots;

import main.java.coloniaDeRobots.eventos.ConsoleLoggerListener;
import main.java.coloniaDeRobots.eventos.EventBus;
import main.java.coloniaDeRobots.eventos.Evento;
import main.java.coloniaDeRobots.util.MetricsCollector;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.io.GestorArchivos;

public class Main {
	private static double FACTOR_CONSUMO = 1.0;
	public static void main(String[] args) {
		// Configurar el EventBus singleton
		MetricsCollector metrics = new MetricsCollector();
		//EventBus bus = EventBus.getDefault();
		//ConsoleLoggerListener consola = new ConsoleLoggerListener();
		// Suscribir al tipo Evento para capturar cualquier evento
		//bus.register(Evento.class, consola);
		//bus.register(Evento.class, metrics);
		try {
			GestorArchivos loader = new GestorArchivos();
			SistemaLogistico sistema = loader.cargarDesdeArchivo("src/main/resources/config.json", FACTOR_CONSUMO);
			int ciclosEjecutados = sistema.run();  // lo adaptamos para devolver int
		    metrics.printSummary(ciclosEjecutados);
		} catch (ExcepcionLogistica e) {
			System.err.println("Error al inicializar el sistema: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
