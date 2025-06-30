package main.java.coloniaDeRobots.eventos;

import main.java.coloniaDeRobots.Robopuerto;
import main.java.coloniaDeRobots.RobotLogistico;

/**
 * Evento que indica que un robot se mueve o recarga.
 */
public class RobotEvent implements Evento {
    public final RobotLogistico robot;
    public final Robopuerto estacionRecarga;
    public RobotEvent(RobotLogistico robot, Robopuerto estacionRecarga) {
        this.robot = robot;
        this.estacionRecarga = estacionRecarga;
    }
}