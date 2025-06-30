package main.java.coloniaDeRobots.eventos;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests para ConsoleLoggerListener: captura de logs de eventos.
 */
class ConsoleLoggerListenerTest {
    private Logger logger;
    private TestHandler handler;
    private ConsoleLoggerListener listener;

    static class DummyEvent implements Evento {
        private final String msg;
        DummyEvent(String msg) { this.msg = msg; }
        @Override public String toString() { return msg; }
    }

    static class TestHandler extends Handler {
        StringBuilder registros = new StringBuilder();
        @Override public void publish(LogRecord record) {
            registros.append(record.getMessage()).append("\n");
        }
        @Override public void flush() {}
        @Override public void close() throws SecurityException {}
    }

    @BeforeEach
    void setup() {
        listener = new ConsoleLoggerListener();
        logger = Logger.getLogger(ConsoleLoggerListener.class.getName());
        handler = new TestHandler();
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
    }

    @AfterEach
    void teardown() {
        logger.removeHandler(handler);
    }

    @Test
    void imprimeLogAlRecibirEvento() {
        DummyEvent evento = new DummyEvent("prueba-evento");
        listener.onEvent(evento);
        String salida = handler.registros.toString();
        assertTrue(salida.contains("Evento: prueba-evento"),
            "Debe registrar el mensaje del evento en el log");
    }
}
