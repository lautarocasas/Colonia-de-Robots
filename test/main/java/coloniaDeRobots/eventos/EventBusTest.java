package main.java.coloniaDeRobots.eventos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests para el EventBus: registro y publicación de eventos.
 */
class EventBusTest {
    private EventBus bus;

    static class TestEvent implements Evento {};
    static class OtroEvento implements Evento {};

    @BeforeEach
    void setup() {
        bus = EventBus.getDefault();
        // Limpiar suscriptores accedido reflectivamente
        // No API pública para limpiar, así que creamos un nuevo bus (para tests)
        // Asumimos bus es singleton: reiniciar no invocado aquí
    }

    @Test
    void listenerRecibeEventoRegistrado() {
        AtomicInteger contador = new AtomicInteger();
        EventListener<TestEvent> listener = e -> contador.incrementAndGet();

        bus.register(TestEvent.class, listener);
        bus.post(new TestEvent());

        assertEquals(1, contador.get(), "El listener debe recibir un único evento");
    }

    @Test
    void multipleListenersRecibenEvento() {
        AtomicInteger c1 = new AtomicInteger();
        AtomicInteger c2 = new AtomicInteger();
        bus.register(TestEvent.class, e -> c1.incrementAndGet());
        bus.register(TestEvent.class, e -> c2.incrementAndGet());

        bus.post(new TestEvent());

        assertEquals(1, c1.get(), "Listener1 debe recibir el evento");
        assertEquals(1, c2.get(), "Listener2 debe recibir el evento");
    }

    @Test
    void noLanzaSiNoHayListeners() {
        // No debe lanzar excepción
        assertDoesNotThrow(() -> bus.post(new OtroEvento()));
    }

    @Test
    void noRecibeEventoDeOtroTipo() {
        AtomicInteger contador = new AtomicInteger();
        bus.register(TestEvent.class, e -> contador.incrementAndGet());
        bus.post(new OtroEvento());
        assertEquals(0, contador.get(), "Listener de TestEvent no debe recibir OtroEvento");
    }
}
