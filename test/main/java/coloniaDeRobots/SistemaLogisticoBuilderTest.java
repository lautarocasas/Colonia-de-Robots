package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.cofres.CofreAlmacenamiento;
import main.java.coloniaDeRobots.cofres.CofreProvisionActiva;
import main.java.coloniaDeRobots.cofres.CofreSolicitud;
import main.java.logistica.excepciones.SolicitudInviableException;
import main.java.logistica.excepciones.UbicacionDuplicadaException;

class SistemaLogisticoBuilderTest {

	@Test
	void construyeSistemaCorrectamente() throws Exception {
		Item hierro = new Item("hierro");
		CofreProvisionActiva prov = new CofreProvisionActiva(new Ubicacion(2, 0), Map.of(hierro, 10));
		CofreSolicitud sol = new CofreSolicitud(new Ubicacion(1, 0), Map.of(), Map.of(hierro, 5));
		Robopuerto puerto = new Robopuerto(new Ubicacion(0, 0), 5.0);
		RobotLogistico robot = new RobotLogistico(new Ubicacion(0, 0), 5, 20.0);

		SistemaLogistico sistema = new SistemaLogisticoBuilder().withFactorConsumo(1.0).addCofres(List.of(prov, sol))
				.addRobopuertos(List.of(puerto)).addRobots(List.of(robot)).build();

		sol.accionar(sistema);
		prov.accionar(sistema);
		assertTrue(sistema.obtenerSolicitudesPendientes().isEmpty(), "Todas las solicitudes debieron satisfacerse");
	}

	@Test
	void lanzaExceptionPorUbicacionesDuplicadas() {
		CofreAlmacenamiento c1 = new CofreAlmacenamiento(new Ubicacion(0, 0), Map.of());
		CofreAlmacenamiento c2 = new CofreAlmacenamiento(new Ubicacion(0, 0), Map.of());
		assertThrows(UbicacionDuplicadaException.class, () -> {
			new SistemaLogisticoBuilder().withFactorConsumo(1.0).addCofres(List.of(c1, c2)).addRobopuertos(List.of())
					.addRobots(List.of()).build();
		});
	}

	@Test
	void lanzaExceptionPorSolicitudInviable() {
		Item madera = new Item("madera");
		CofreSolicitud sol = new CofreSolicitud(new Ubicacion(1, 1), Map.of(), Map.of(madera, 5));
		Robopuerto puerto = new Robopuerto(new Ubicacion(0, 0), 10.0);
		assertThrows(SolicitudInviableException.class, () -> {
			new SistemaLogisticoBuilder().withFactorConsumo(1.0).addCofres(List.of(sol)).addRobopuertos(List.of(puerto))
					.addRobots(List.of()).build();
		});
	}

	@Test
	void lanzaExceptionPorFactorConsumoInvalido() {
		assertThrows(IllegalArgumentException.class, () -> new SistemaLogisticoBuilder().withFactorConsumo(0.0));
	}

}
