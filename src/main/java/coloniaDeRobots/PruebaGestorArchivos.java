package main.java.coloniaDeRobots;

import main.java.logistica.excepciones.ExcepcionLogistica;
import main.java.logistica.io.GestorArchivos;

public class PruebaGestorArchivos {
    public static void main(String[] args) {
        String base = "src/main/resources/";
        String[] archivos = {
            base + "valid_simple.json",
            base + "missing_robots.json",
            base + "negative_alcance.json",
            base + "duplicate_location.json",
            base + "complex.json"
        };
        GestorArchivos loader = new GestorArchivos();
        for (String path : archivos) {
            try {
                System.out.printf("Cargando %s... ", path);
                loader.cargarDesdeArchivo(path);
                System.out.println("OK");
            } catch (ExcepcionLogistica e) {
                System.out.println("ERROR -> " + e.getMessage());
            }
        }
    }
}
