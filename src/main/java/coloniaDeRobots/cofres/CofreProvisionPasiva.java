package main.java.coloniaDeRobots.cofres;

import java.util.Map;

import main.java.coloniaDeRobots.Item;
import main.java.coloniaDeRobots.SistemaLogistico;
import main.java.coloniaDeRobots.Ubicacion;

public class CofreProvisionPasiva extends Cofre {
    public CofreProvisionPasiva(Ubicacion ubicacion, Map<Item,Integer> inventario) {
        super(ubicacion, inventario);
    }

    @Override
    public void accionar(SistemaLogistico sistema) {
        // No empuja; el sistema extrae cuando lo planifica
    }
}