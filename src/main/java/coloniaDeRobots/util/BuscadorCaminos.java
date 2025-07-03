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
import main.java.logistica.excepciones.ExcepcionLogistica;

public class BuscadorCaminos {
	
	private BuscadorCaminos() {
		/* utilitario */ }

	/**
	 * Retorna la lista de ubicaciones del camino más corto entre origen y destino,
	 * o [origen,destino] si alguno no está en la red.
	 * @throws ExcepcionLogistica 
	 */
	public static CaminoEsperado calcularCaminoMasCorto(Ubicacion origen, Ubicacion destino, List<Cofre> cofres,
			List<Robopuerto> puertos) throws ExcepcionLogistica { //tener en cuenta el tema de la bateria
		// 1) Reunir todos los nodos en una lista (objetos, no ubicaciones)
		List<ElementoLogistico> nodos = new ArrayList<>();
		nodos.addAll(cofres);
		nodos.addAll(puertos);

		// 2) Identificar el nodo que corresponde a origen y destino
		ElementoLogistico nodoOrigen = buscarNodo(nodos, origen);
		ElementoLogistico nodoDestino = buscarNodo(nodos, destino);
		if (nodoOrigen == null || nodoDestino == null) {
			// Si no existe alguno, devolvemos el fallback
			throw new ExcepcionLogistica("No existen las ubicaciones solicitadas");
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
		List<Robopuerto> estacionDeCarga = new ArrayList<>();
		
		for (ElementoLogistico n : nodos)
			dist.put(n, Double.POSITIVE_INFINITY);
		dist.put(nodoOrigen, 0.0);

		Set<ElementoLogistico> nodosRecorridos = new HashSet<>();
		PriorityQueue<ElementoLogistico> colaPrioridad = new PriorityQueue<>(Comparator.comparing(dist::get));
		colaPrioridad.addAll(nodos);

		while (!colaPrioridad.isEmpty()) {
            ElementoLogistico nodoSeleccionado = colaPrioridad.poll();
            if (!nodosRecorridos.add(nodoSeleccionado))
                continue;
            if (nodoSeleccionado.equals(nodoDestino))
                break;

            for (var nodoADeterminar : grafo.getOrDefault(nodoSeleccionado, Map.of()).entrySet()) {
                ElementoLogistico siguienteNodo = nodoADeterminar.getKey();
                double peso = nodoADeterminar.getValue();
                double alt = dist.get(nodoSeleccionado) + peso;// q hace aca?
                if (alt < dist.get(siguienteNodo)) {
                    dist.put(siguienteNodo, alt);
                    prev.put(siguienteNodo, nodoSeleccionado);
                    colaPrioridad.add(siguienteNodo);
                }
            }
        }

		// 5) Reconstruir camino en términos de Ubicacion
		List<ElementoLogistico> camino = new ArrayList<>();
		for (ElementoLogistico ubicacionActual = nodoDestino; ubicacionActual != null; ubicacionActual = prev.get(ubicacionActual)) {
			camino.add(ubicacionActual);
			if(ubicacionActual instanceof Robopuerto)
				estacionDeCarga.add((Robopuerto)ubicacionActual);
		}
		Collections.reverse(camino);
		Collections.reverse(estacionDeCarga);
		
		return new CaminoEsperado(camino,prev,dist,estacionDeCarga);
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
