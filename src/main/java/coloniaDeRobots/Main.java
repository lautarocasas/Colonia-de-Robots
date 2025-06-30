package main.java.coloniaDeRobots;

import main.java.coloniaDeRobots.eventos.ConsoleLoggerListener;
import main.java.coloniaDeRobots.eventos.EventBus;
import main.java.coloniaDeRobots.eventos.Evento;
import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.io.GestorArchivos;

public class Main {

	public static void main(String[] args) {
		// Configurar el EventBus singleton
		EventBus bus = EventBus.getDefault();
		ConsoleLoggerListener consola = new ConsoleLoggerListener();
		// Suscribir al tipo Evento para capturar cualquier evento
		bus.register(Evento.class, consola);

		try {
			GestorArchivos loader = new GestorArchivos();
			SistemaLogistico sistema = loader.cargarDesdeArchivo("src/main/resources/complex.json");
		} catch (ExcepcionLogistica e) {
			System.err.println("Error al inicializar el sistema: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
