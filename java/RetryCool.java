/*
Basado en RetryDecenteGenerico. Usa method chaining para dejar explicito a que
corresponden los parametros 10, GuateoException.class y 2000.

Todo seria mucho mas bonito si el argumento de execute se pudiera escribir sin
las lineas extras por el boilerplate de instanciar una clase anonima con un solo
metodo para simular el paso de una funcion como parametro. Pero para arreglar
eso habra que esperar a Java 7 y las closures.
*/

import static java.lang.System.out;
import static java.lang.Math.random;

public class RetryCool {
    
    static class GuateoException extends Exception {};
    
    static void servicioPenca() throws GuateoException {
	out.println("Servicio invocado");
	if (random() < 0.8) {
	    throw new GuateoException();
	}
	out.println("Servicio ejecutado exitosamente");
    }

    static void solucion() throws Exception {
	Retry.times(10).when(GuateoException.class).wait(2000).execute(
            new Retry.Runnable() {
	        public Object run() throws GuateoException{ 
		    servicioPenca();
		    return null;
		}
	    }
	);
    }

    static class Retry {
	static interface Runnable {
	    Object run() throws Exception;
	}

	static Retry times(int times) {
	    return new Retry(times);
	}
	
	int times; int wait; Class<? extends Exception> exceptionClass;

	Retry(int times) {
	    this.times = times;
	}

	Retry wait(int wait) {
	    this.wait = wait;
	    return this;
	}

	Retry when(Class<? extends Exception> exceptionClass) {
	    this.exceptionClass = exceptionClass;
	    return this;
	}

	Object execute(Retry.Runnable what) throws Exception {
	    Exception lastException = null;
	    while (times-- > 0) {
		try {
		    return what.run();
		} catch (Exception e) {
		    if (exceptionClass.isAssignableFrom(e.getClass())) {
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