package main.java.coloniaDeRobots.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map; // <- añadido

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.RobotLogistico;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;
import main.java.coloniaDeRobots.eventos.RobotEvent;
import main.java.coloniaDeRobots.eventos.TransporteGeneradoEvent;

class MetricsCollectorTest {
	private MetricsCollector metrics;
	private ByteArrayOutputStream outContent;
	private PrintStream originalOut;

	@BeforeEach
	void setup() {
		metrics = new MetricsCollector();
		// Capturar salida estándar
		originalOut = System.out;
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@Test
	void mideTransporteYDistancia() {
		// Crear cofres y item
		CofreAlmacenamiento origen = new CofreAlmacenamiento(new Ubicacion(0, 0), Map.of());
		CofreAlmacenamiento destino = new CofreAlmacenamiento(new Ubicacion(3, 4), Map.of());
		Item item = new Item("X");
		// Simular dos transportes (cada uno recorre 5 unidades)
		//metrics.onEvent(new TransporteGeneradoEvent(origen, destino, item, 2));
		//metrics.onEvent(new TransporteGeneradoEvent(origen, destino, item, 3));
		// Simular una recarga
		RobotLogistico robot = new RobotLogistico(new Ubicacion(0, 0), 5, 10.0);
		Robopuerto puerto = new Robopuerto(new Ubicacion(0, 0), 5.0);
		//metrics.onEvent(new RobotEvent(robot, puerto));

		// Imprimir resumen a consola
		metrics.printSummary(5);
		String salida = outContent.toString();

		assertTrue(salida.contains("5 ciclos"), "Debe reportar los ciclos ejecutados");
		assertTrue(salida.contains("Transportes realizados: 2"), "Debe contar 2 transportes");
		assertTrue(salida.contains("Distancia total:        10.00"), "Distancia total debe ser 10.00 (5+5)");
		assertTrue(salida.contains("Recargas de batería:    1"), "Debe contar 1 recarga");
	}

	@Test
	void resumenSinEventos() {
		// Sin invocar onEvent
		metrics.printSummary(0);
		String salida = outContent.toString();

		assertTrue(salida.contains("Ciclos ejecutados:      0"));
		assertTrue(salida.contains("Transportes realizados: 0"));
		assertTrue(salida.contains("Distancia total:        0.00"));
		assertTrue(salida.contains("Recargas de batería:    0"));
	}
}
