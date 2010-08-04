/*
Una solucion al estilo de los frameworks Java. En vez de proveer una función que
hace lo requerido en base a parametros, se provee un framework que hace lo
requerido y que permite modificar algunas piezas fijas. 

Problemas: 

 - Quema la clase base (ver <http://www.artima.com/intv/dotnet.html>).

 - En general menos flexible que simplemente llamar a un metodo (por ejemplo, si
ahorael servicio requiriera parametros habria que extender en framework. En
cambio, las 3 soluciones funcionales no se verian afectadas)
*/

import static java.lang.System.out;
import static java.lang.Math.random;

public class RetryFramework {
    
    static class GuateoException extends Exception {};
    
    static void servicioPenca() throws GuateoException {
	out.println("Servicio invocado");
	if (random() < 0.8) {
	    throw new GuateoException();
	}
	out.println("Servicio ejecutado exitosamente");
    }

    static class SolucionRetryable extends Retryable {
	protected Object run() throws GuateoException {
	    servicioPenca();
	    return null;
	}
    }

    static void solucion() throws Exception {
	SolucionRetryable s = new SolucionRetryable();
	s.times = 10; s.when = GuateoException.class; s.wait = 2000;
	s.start();
    }

    static abstract class Retryable {
	public int times;
	public Class<? extends Exception> when;
	public int wait;
	abstract protected Object run() throws Exception;
	public Object start() throws Exception {
	    Exception lastException = null;
	    while (times-- > 0) {
		try {
		    return run();
		} catch (Exception e) {
		    if (when.isAssignableFrom(e.getClass())) {
			sleep(wait);
			lastException = e;
		    } else {
			throw e;
		    }
		}
	    }
	    throw lastException;	
	}
    }
    
    static void sleep(int millis) {
	try {
	    Thread.sleep(millis);
	} catch (InterruptedException e) {};    
    }
    
    public static void main(String[] args) throws Exception {
	solucion();
    }
}