package main.java.logistica.excepciones;


public class EstructuraInvalidaException extends ExcepcionLogistica {
    
	private static final long serialVersionUID = 327043725270675789L;

	public EstructuraInvalidaException(String msg, Throwable cause) { 
    	super(msg, cause); 
	}
}