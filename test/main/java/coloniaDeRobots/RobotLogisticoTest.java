package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.cofres.Cofre;
import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;

class RobotLogisticoTest {
	private RobotLogistico robot;
	private Ubicacion a, b;
	private List<Cofre> cofres;
	private List<Robopuerto> robopuertos;

//	@BeforeEach
//	void configurar() {
//		a = new Ubicacion(0, 0);
//		b = new Ubicacion(3, 4);
//		cofres = List.of(new CofreAlmacenamiento(a, Map.of()));
//		robopuertos = List.of(new Robopuerto(a, 10.0));
//		robot = new RobotLogistico(a, 5, 100.0);
//		robot.setEntornoLogistico(robopuertos, List.copyOf(cofres));
//	}
//
//	@Test
//	void planificaRutaOptima() {
//		robot.planificarRuta(a, b);
//		// La ruta debe comenzar en origen y terminar en destino
//		assertTrue(robot.tieneTarea());
//		// Avanzar hasta final
//		while (robot.tieneTarea())
//			robot.avanzar(1.0);
//		assertEquals(b, robot.getUbicacion());
//	}
//
//	@Test
//	void recargaCuandoEnPuerto() {
//		// Mover robot al puerto
//		robot.planificarRuta(a, a);
//		robot.avanzar();
//		// Agotar batería
//		robot = new RobotLogistico(a, 5, 1.0);
//		robot.setEntornoLogistico(robopuertos, cofres);
//		robot.setBateriaActual(0.0);
//		robot.recargar();
//		assertEquals(1.0, robot.getBateriaActual());
//	}
//
//	@Test
//	void recargaCuandoEnPuerto2() {
//		// Simulo que el robot está dentro del área de un robopuerto
//		robot.setBateriaActual(0.0);
//		// Debe recargar porque su ubicación (0,0) está cubierta
//		robot.recargar();
//		assertEquals(100.0, robot.getBateriaActual(), "Tras recarga en puerto, la batería debe volver a su máximo");
//	}

}