/*
Basado en RetryDecente, pero funciona con cualquier Exception y retornando
valores.

Problema: No es claro a que corresponden los parametros 10 y 2000. 

Mejoras: Ver RetryCool que resuelve dicho problema.
*/

import static java.lang.System.out;
import static java.lang.Math.random;

public class RetryDecenteGenerico {
    
    static class GuateoException extends Exception {};
    
    static void servicioPenca() throws GuateoException {
	out.println("Servicio invocado");
	if (random() < 0.8) {
	    throw new GuateoException();
	}
	out.println("Servicio ejecutado exitosamente");
    }

    static void solucion() throws Exception{
	Retry.retry(10, GuateoException.class, 2000, new Retry.Runnable() {
	    public Object run() throws GuateoException {
		servicioPenca();
		return null;
	    }
	});
    }

    static class Retry {
	static interface Runnable {
	    Object run() throws Exception;
	}
	
        static Object retry(int times, 
			    Class<? extends Exception> exceptionClass, 
			    int sleep_millis, Runnable what) throws Exception {
	    Exception lastException = null;
	    while (times-- > 0) {
		try {
		    return what.run();
		} catch (Exception e) {
		    if (exceptionClass.isAssignableFrom(e.getClass())) {
			sleep(sleep_millis);
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