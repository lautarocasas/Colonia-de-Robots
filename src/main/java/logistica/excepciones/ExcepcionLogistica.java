package main.java.logistica.excepciones;

import java.util.logging.Logger;

/**
 * Excepción base para toda la capa de carga/parsing.
 * Registra en log los mensajes de error.
 */
public class ExcepcionLogistica extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1508142324408395485L;
	private static final Logger LOGGER = Logger.getLogger(ExcepcionLogistica.class.getName());

    public ExcepcionLogistica(String msg) {
        super(msg);
        LOGGER.severe(msg);
    }

    public ExcepcionLogistica(String msg, Throwable cause) {
        super(msg, cause);
        LOGGER.severe(msg);
    }
}

