package main.java.coloniaDeRobots.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import main.java.coloniaDeRobots.ElementoLogistico;
import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.cofres.Cofre;

public class ValidadorConectividad {
	private ValidadorConectividad() {
	}

	/**
	 * Separa los cofres en accesibles e inaccesibles.
	 * 
	 * @param cofres  Lista de cofres a validar
	 * @param puertos Lista de robopuertos que definen cobertura
	 */
	public static ResultadoConectividad validarConectividad(List<Cofre> cofres, List<Robopuerto> puertos) {
		// Obtener todos los nodos del grafo
		List<ElementoLogistico> nodos = obtenerNodos(cofres, puertos);
		Map<ElementoLogistico, List<ElementoLogistico>> adj = construirAdyacencia(cofres, puertos);

		// Asignar componente a cada nodo
		Map<ElementoLogistico, Integer> componentes = encontrarComponentes(nodos, adj);

		// Identificar IDs de componentes que contienen robopuertos
		Set<Integer> compsConPuerto = componentesDePuertos(puertos, componentes);

		// Filtrar cofres según componente
		List<Cofre> accesibles = new ArrayList<>();
		List<Cofre> inaccesibles = new ArrayList<>();
		for (Cofre c : cofres) {
			int id = componentes.get(c);
			if (compsConPuerto.contains(id))
				accesibles.add(c);
			else
				inaccesibles.add(c);
		}
		return new ResultadoConectividad(accesibles, inaccesibles);
	}

	private static List<ElementoLogistico> obtenerNodos(List<Cofre> cofres, List<Robopuerto> puertos) {
		List<ElementoLogistico> todos = new ArrayList<>();
		todos.addAll(cofres);
		todos.addAll(puertos);
		return todos;
	}

	private static Map<ElementoLogistico, List<ElementoLogistico>> construirAdyacencia(List<Cofre> cofres,
			List<Robopuerto> puertos) {
		Map<ElementoLogistico, List<ElementoLogistico>> adj = new HashMap<>();
		// Inicializar lista vacía para cada nodo
		for (Cofre c : cofres)
			adj.put(c, new ArrayList<>());
		for (Robopuerto rp : puertos)
			adj.put(rp, new ArrayList<>());

		// Aristas Cofre <-> Robopuerto
		for (Cofre c : cofres) {
			for (Robopuerto rp : puertos) {
				if (rp.cubre(c.getUbicacion())) {
					adj.get(c).add(rp);
					adj.get(rp).add(c);
				}
			}
		}
		// Aristas Robopuerto <-> Robopuerto
		for (int i = 0; i < puertos.size(); i++) {
			for (int j = i + 1; j < puertos.size(); j++) {
				Robopuerto r1 = puertos.get(i);
				Robopuerto r2 = puertos.get(j);
				double d = r1.getUbicacion().distanciaA(r2.getUbicacion());
				if (d <= r1.getAlcance() + r2.getAlcance()) {
					adj.get(r1).add(r2);
					adj.get(r2).add(r1);
				}
			}
		}
		return adj;
	}

	private static Map<ElementoLogistico, Integer> encontrarComponentes(List<ElementoLogistico> nodos,
			Map<ElementoLogistico, List<ElementoLogistico>> adj) {
		Map<ElementoLogistico, Integer> comp = new HashMap<>();
		int id = 0;
		for (ElementoLogistico e : nodos) {
			if (!comp.containsKey(e)) {
				asignarComponenteBFS(e, id, adj, comp);
				id++;
			}
		}
		return comp;
	}

	private static void asignarComponenteBFS(ElementoLogistico inicio, int idComp,
			Map<ElementoLogistico, List<ElementoLogistico>> adj, Map<ElementoLogistico, Integer> comp) {
		Queue<ElementoLogistico> cola = new ArrayDeque<>();
		cola.add(inicio);
		comp.put(inicio, idComp);
		while (!cola.isEmpty()) {
			ElementoLogistico actual = cola.poll();
			for (ElementoLogistico v : adj.get(actual)) {
				if (!comp.containsKey(v)) {
					comp.put(v, idComp);
					cola.add(v);
				}
			}
		}
	}

	private static Set<Integer> componentesDePuertos(List<Robopuerto> puertos, Map<ElementoLogistico, Integer> comp) {
		Set<Integer> set = new HashSet<>();
		for (Robopuerto rp : puertos) {
			set.add(comp.get(rp));
		}
		return set;
	}
}