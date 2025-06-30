package main.java.coloniaDeRobots.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.cofres.CofreProvisionActiva;
import main.java.coloniaDeRobots.cofres.CofreSolicitud;
import main.java.logistica.excepciones.SolicitudInviableException;

class ValidadorFactibilidadTest {

	@Test
	void factibleCuandoHayProveedor() {
		Item hierro = new Item("hierro");
		var proveedor = new CofreProvisionActiva(new Ubicacion(0, 0), Map.of(hierro, 10));
		var solicitante = new CofreSolicitud(new Ubicacion(1, 1), Map.of(), Map.of(hierro, 5));
		// No debe lanzar excepción
		assertDoesNotThrow(() -> ValidadorFactibilidad.validarFactibilidad(List.of(proveedor, solicitante)));
	}

	@Test
	void esInviableSinProveedor() {
		Item hierro = new Item("hierro");
		var solicitante = new CofreSolicitud(new Ubicacion(1, 1), Map.of(), Map.of(hierro, 5));
		assertThrows(SolicitudInviableException.class,
				() -> ValidadorFactibilidad.validarFactibilidad(List.of(solicitante)));
	}

}
