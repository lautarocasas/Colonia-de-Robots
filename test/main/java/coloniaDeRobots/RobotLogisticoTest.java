package main.java.coloniaDeRobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RobotLogisticoTest {
    private RobotLogistico robot;

    @BeforeEach
    void setup() {
        robot = new RobotLogistico(new Ubicacion(0,0), 5, 10.0);
    }

    @Test
    void testCapacityAndBattery() {
        assertTrue(robot.puedeTransportar(5));
        assertFalse(robot.puedeTransportar(6));
        assertTrue(robot.tieneBateriaPara(2.0, 5.0)); // 2*5=10
        assertFalse(robot.tieneBateriaPara(2.1, 5.0));
    }

    @Test
    void testConsumeAndRecharge() {
        robot.consumirBateria(3.5);
        assertEquals(6.5, robot.getBateriaActual(), 1e-9);
        robot.recargar();
        assertEquals(10.0, robot.getBateriaActual(), 1e-9);
    }

    @Test
    void testInvalidConstructionThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new RobotLogistico(new Ubicacion(0,0), -1, 10.0));
        assertThrows(IllegalArgumentException.class,
            () -> new RobotLogistico(new Ubicacion(0,0), 5, 0));
    }
}
