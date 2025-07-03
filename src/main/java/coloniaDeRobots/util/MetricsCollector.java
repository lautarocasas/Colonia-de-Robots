package main.java.coloniaDeRobots.util;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;



/**
 * Collector que acumula métricas de transporte y recarga durante la simulación.
 */
public class MetricsCollector {
	private final AtomicInteger totalTransportes = new AtomicInteger();
	private final AtomicReference<Double> distanciaTotal = new AtomicReference<>(0.0);
	private final AtomicInteger recargas = new AtomicInteger();

	/** Imprime al final un resumen de las métricas. */
	public void printSummary(int ciclos) {
		System.out.println();
		System.out.println("Resumen de simulacion:");
		System.out.println("  - Ciclos ejecutados:      " + ciclos + " ciclos");
		System.out.println("  - Transportes realizados: " + totalTransportes.get());
		System.out.printf(Locale.US, "  - Distancia total:        %.2f%n", distanciaTotal.get());
		System.out.println("  - Recargas de bateria:    " + recargas.get());
	}

	/** Retorna el total de transportes realizados */
	public int getTotalTransportes() {
		return totalTransportes.get();
	}

	/** Retorna la distancia total recorrida */
	public double getDistanciaTotal() {
		return distanciaTotal.get();
	}

	/** Retorna el número de recargas efectuadas */
	public int getRecargas() {
		return recargas.get();
	}
}
