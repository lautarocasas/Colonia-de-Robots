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

/**
 * Utilidad para encontrar el camino de menor distancia entre dos ubicaciones
 * en la red logística usando Dijkstra.
 */
public class BuscadorCaminos {

    private BuscadorCaminos() { /* Utilitario */ }

    /**
     * Devuelve el camino más corto entre origen y destino como lista de Ubicaciones.
     */
    public static List<Ubicacion> caminoMasCorto(
            Ubicacion origen,
            Ubicacion destino,
            List<Cofre> cofres,
            List<Robopuerto> puertos
    ) {
        Map<Ubicacion, ElementoLogistico> mapa = crearMapaNodos(cofres, puertos);
        if (!mapa.containsKey(origen) || !mapa.containsKey(destino)) {
            return List.of(origen, destino);
        }
        List<ElementoLogistico> nodos = new ArrayList<>(mapa.values());
        Map<ElementoLogistico, Map<ElementoLogistico, Double>> grafo = construirGrafo(cofres, puertos);
        Map<ElementoLogistico, Double> dist = iniciarDistancias(nodos, mapa.get(origen));
        Map<ElementoLogistico, ElementoLogistico> previo = dijkstra(nodos, grafo, dist);
        return reconstruirCamino(previo, mapa.get(origen), mapa.get(destino));
    }

    private static Map<Ubicacion, ElementoLogistico> crearMapaNodos(
            List<Cofre> cofres,
            List<Robopuerto> puertos
    ) {
        Map<Ubicacion, ElementoLogistico> mapa = new HashMap<>();
        for (Cofre c : cofres) mapa.put(c.getUbicacion(), c);
        for (Robopuerto r : puertos) mapa.put(r.getUbicacion(), r);
        return mapa;
    }

    private static Map<ElementoLogistico, Map<ElementoLogistico, Double>> construirGrafo(
            List<Cofre> cofres,
            List<Robopuerto> puertos
    ) {
        Map<ElementoLogistico, Map<ElementoLogistico, Double>> grafo = new HashMap<>();
        for (Cofre c : cofres) grafo.put(c, new HashMap<>());
        for (Robopuerto r : puertos) grafo.put(r, new HashMap<>());

        // Cofre <-> Robopuerto
        for (Cofre c : cofres) {
            for (Robopuerto r : puertos) {
                if (r.cubre(c.getUbicacion())) {
                    double d = c.getUbicacion().distanciaA(r.getUbicacion());
                    grafo.get(c).put(r, d);
                    grafo.get(r).put(c, d);
                }
            }
        }
        // Robopuerto <-> Robopuerto
        for (int i = 0; i < puertos.size(); i++) {
            for (int j = i + 1; j < puertos.size(); j++) {
                Robopuerto r1 = puertos.get(i);
                Robopuerto r2 = puertos.get(j);
                double d = r1.getUbicacion().distanciaA(r2.getUbicacion());
                if (d <= r1.getAlcance() + r2.getAlcance()) {
                    grafo.get(r1).put(r2, d);
                    grafo.get(r2).put(r1, d);
                }
            }
        }
        return grafo;
    }

    private static Map<ElementoLogistico, Double> iniciarDistancias(
            List<ElementoLogistico> nodos,
            ElementoLogistico inicio
    ) {
        Map<ElementoLogistico, Double> dist = new HashMap<>();
        for (ElementoLogistico n : nodos) dist.put(n, Double.POSITIVE_INFINITY);
        dist.put(inicio, 0.0);
        return dist;
    }

    private static Map<ElementoLogistico, ElementoLogistico> dijkstra(
            List<ElementoLogistico> nodos,
            Map<ElementoLogistico, Map<ElementoLogistico, Double>> grafo,
            Map<ElementoLogistico, Double> dist
    ) {
        Map<ElementoLogistico, ElementoLogistico> previo = new HashMap<>();
        Set<ElementoLogistico> visitados = new HashSet<>();
        PriorityQueue<ElementoLogistico> cola = new PriorityQueue<>(Comparator.comparing(dist::get));
        cola.addAll(nodos);

        while (!cola.isEmpty()) {
            ElementoLogistico u = cola.poll();
            if (!visitados.add(u)) continue;
            for (Map.Entry<ElementoLogistico, Double> entry : grafo.getOrDefault(u, Collections.emptyMap()).entrySet()) {
                ElementoLogistico v = entry.getKey();
                double peso = entry.getValue();
                double alt = dist.get(u) + peso;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    previo.put(v, u);
                    cola.add(v); // versiones antiguas ignoradas
                }
            }
        }
        return previo;
    }

    private static List<Ubicacion> reconstruirCamino(
            Map<ElementoLogistico, ElementoLogistico> previo,
            ElementoLogistico inicio,
            ElementoLogistico fin
    ) {
        List<Ubicacion> camino = new ArrayList<>();
        for (ElementoLogistico actual = fin; actual != null; actual = previo.get(actual)) {
            camino.add(actual.getUbicacion());
        }
        Collections.reverse(camino);
        return camino;
    }
}
