package main.java.coloniaDeRobots;

import java.util.Objects;
import java.util.logging.Logger;

public class Item {
    //private static final Logger LOGGER = Logger.getLogger(Item.class.getName());
    private final String nombre;

    public Item(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre de Item no puede ser null");
        //LOGGER.info(() -> "Item creado: " + nombre);
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
