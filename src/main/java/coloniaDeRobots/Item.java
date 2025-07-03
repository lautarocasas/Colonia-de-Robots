package main.java.coloniaDeRobots;

import java.util.Objects;

public class Item {
    private final String nombre;

    public Item(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre de Item no puede ser null");
        System.out.println("Item creado: " + nombre);
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return nombre.equals(item.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
