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
		Set<Integer> compsConPuerto = obtenerComponentesDePuertos(puertos, componentes);

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
		inicializarNodos(cofres,puertos,adj);
		añadirAristasCofreRobopuerto(adj,cofres,puertos);
		añadirAristasRobopuertoARobopuerto(puertos,adj);
		return adj;
	}
	
	private static void inicializarNodos(List<Cofre> cofres,
		List<Robopuerto> puertos,Map<ElementoLogistico, List<ElementoLogistico>>adj){
		for (Cofre c : cofres)
			adj.put(c, new ArrayList<>());
		for (Robopuerto rp : puertos)
			adj.put(rp, new ArrayList<>());
	}
	
	private static void añadirAristasCofreRobopuerto(Map<ElementoLogistico, List<ElementoLogistico>> adj,
			List<Cofre> cofres,List<Robopuerto> puertos) {
        for (Cofre cofre : cofres) {
            for (Robopuerto robopuerto : puertos) {
                if (robopuerto.cubre(cofre.getUbicacion())) {
                    adj.get(cofre).add(robopuerto);
                    adj.get(robopuerto).add(cofre);
                }
            }
        }
    }
	
	private static void añadirAristasRobopuertoARobopuerto(List<Robopuerto> puertos,Map<ElementoLogistico, List<ElementoLogistico>> adj){
		for (int i = 0; i < puertos.size(); i++) {
			for (int j = i + 1; j < puertos.size(); j++) {
				Robopuerto primerRobopuerto = puertos.get(i);
				Robopuerto segundoRobopuerto = puertos.get(j);
				double distancia = primerRobopuerto.getUbicacion().calcularDistanciaA(segundoRobopuerto.getUbicacion());
				if (distancia <= primerRobopuerto.getAlcance() + segundoRobopuerto.getAlcance()) {
					adj.get(primerRobopuerto).add(segundoRobopuerto);
					adj.get(segundoRobopuerto).add(primerRobopuerto);
				}
			}
		}
	}
	
	private static Map<ElementoLogistico, Integer> encontrarComponentes(List<ElementoLogistico> nodos,
			Map<ElementoLogistico, List<ElementoLogistico>> adj) {
		Map<ElementoLogistico, Integer> comp = new HashMap<>();
		int id = 0;
		for (ElementoLogistico e : nodos) {
			if (!comp.containsKey(e)) {
				asignarComponente(e, id, adj, comp);
				id++;
			}
		}
		return comp;
	}

	private static void asignarComponente(ElementoLogistico inicio, int idComp,
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

	private static Set<Integer> obtenerComponentesDePuertos(List<Robopuerto> puertos, Map<ElementoLogistico, Integer> comp) {
		Set<Integer> set = new HashSet<>();
		for (Robopuerto rp : puertos) {
			set.add(comp.get(rp));
		}
		return set;
	}
}