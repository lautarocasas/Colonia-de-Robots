package main.java.coloniaDeRobots.eventos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Listener que imprime eventos al log de consola.
 */
public class ConsoleLoggerListener implements EventListener<Evento> {
    private static final Logger LOG = Logger.getLogger(ConsoleLoggerListener.class.getName());
    //private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onEvent(Evento evento) {
    	System.out.println("onEvent");
        //String ts = LocalDateTime.now().format(fmt);
    	System.out.println(evento.toString()+" evento ");
    	System.out.println(evento.getClass().toString()+" evento ");
        LOG.info(() -> String.format("Evento: %s",evento.toString()));
    }
}
