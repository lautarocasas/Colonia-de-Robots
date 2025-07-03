// File: main/java/coloniaDeRobots/util/BuscadorCaminos.java
package main.java.coloniaDeRobots.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import main.java.coloniaDeRobots.ElementoLogistico;
import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.Ubicacion;
import main.java.coloniaDeRobots.cofres.Cofre;

public class BuscadorCaminos {

	private BuscadorCaminos() {
		/* utilitario */ }

	/**
	 * Retorna la lista de ubicaciones del camino más corto entre origen y destino,
	 * o [origen,destino] si alguno no está en la red.
	 */
	public static List<Ubicacion> calcularCaminoMasCorto(Ubicacion origen, Ubicacion destino, List<Cofre> cofres,
			List<Robopuerto> puertos) { //tener en cuenta el tema de la bateria
		// 1) Reunir todos los nodos en una lista (objetos, no ubicaciones)
		List<ElementoLogistico> nodos = new ArrayList<>();
		nodos.addAll(cofres);
		nodos.addAll(puertos);

		// 2) Identificar el nodo que corresponde a origen y destino
		ElementoLogistico nodoOrigen = buscarNodo(nodos, origen);
		ElementoLogistico nodoDestino = buscarNodo(nodos, destino);
		if (nodoOrigen == null || nodoDestino == null) {
			// Si no existe alguno, devolvemos el fallback
			return List.of(origen, destino);
		}

		// 3) Construir el grafo como adyacencia con pesos = distancia euclídea
		var grafo = new HashMap<ElementoLogistico, Map<ElementoLogistico, Double>>();
		inicializarGrafo(cofres,puertos,grafo);
	
		// Cofre <-> Robopuerto
		añadirAristasCofreRobopuerto(cofres,puertos,grafo);
		// Robopuerto <-> Robopuerto
		añadirAristasRobopuertoArobopuerto(puertos,grafo);
		
		// 4) Dijkstra
		Map<ElementoLogistico, Double> dist = new HashMap<>();
		Map<ElementoLogistico, ElementoLogistico> prev = new HashMap<>();
		for (ElementoLogistico n : nodos)
			dist.put(n, Double.POSITIVE_INFINITY);
		dist.put(nodoOrigen, 0.0);

		Set<ElementoLogistico> seen = new HashSet<>();
		PriorityQueue<ElementoLogistico> colaPrioridad = new PriorityQueue<>(Comparator.comparing(dist::get));
		colaPrioridad.addAll(nodos);

		while (!colaPrioridad.isEmpty()) {
			ElementoLogistico u = colaPrioridad.poll();
			if (!seen.add(u))
				continue;
			if (u.equals(nodoDestino))
				break;

			for (var e : grafo.getOrDefault(u, Map.of()).entrySet()) {
				ElementoLogistico v = e.getKey();
				double peso = e.getValue();
				double alt = dist.get(u) + peso;
				if (alt < dist.get(v)) {
					dist.put(v, alt);
					prev.put(v, u);
					colaPrioridad.add(v);
				}
			}
		}

		// 5) Reconstruir camino en términos de Ubicacion
		List<Ubicacion> camino = new ArrayList<>();
		for (ElementoLogistico at = nodoDestino; at != null; at = prev.get(at)) {
			camino.add(at.getUbicacion());
		}
		Collections.reverse(camino);
		return camino;
	}

	private static ElementoLogistico buscarNodo(List<ElementoLogistico> nodos, Ubicacion ub) {
		// Priorizar cofres si ambos comparten coords
		for (ElementoLogistico e : nodos) {
			if (e instanceof Cofre && e.getUbicacion().equals(ub)) {
				return e;
			}
		}
		for (ElementoLogistico e : nodos) {
			if (e instanceof Robopuerto && e.getUbicacion().equals(ub)) {
				return e;
			}
		}
		return null;
	}
	
	private static void inicializarGrafo(List<Cofre> cofres,List<Robopuerto> puertos,HashMap<ElementoLogistico, Map<ElementoLogistico, Double>> grafo){
		for (Cofre c : cofres)
			grafo.put(c, new HashMap<>());
		for (Robopuerto r : puertos)
			grafo.put(r, new HashMap<>());
	}
	
	private static void añadirAristasCofreRobopuerto(List<Cofre> cofres,List<Robopuerto> puertos,HashMap<ElementoLogistico, Map<ElementoLogistico, Double>> grafo) {
		for (Cofre c : cofres) {
			for (Robopuerto r : puertos) {
				if (r.cubre(c.getUbicacion())) {
					double d = c.getUbicacion().calcularDistanciaA(r.getUbicacion());
					grafo.get(c).put(r, d);
					grafo.get(r).put(c, d);
				}
			}
		}
	}
	
	private static void añadirAristasRobopuertoArobopuerto(List<Robopuerto> puertos,HashMap<ElementoLogistico, Map<ElementoLogistico, Double>> grafo) {
		for (int i = 0; i < puertos.size(); i++) {
			for (int j = i + 1; j < puertos.size(); j++) {
				Robopuerto r1 = puertos.get(i), r2 = puertos.get(j);
				double d = r1.getUbicacion().calcularDistanciaA(r2.getUbicacion());
				if (d <= r1.getAlcance() + r2.getAlcance()) {
					grafo.get(r1).put(r2, d);
					grafo.get(r2).put(r1, d);
				}
			}
		}

	}
	
}
