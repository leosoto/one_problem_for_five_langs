import static java.lang.System.out;
import static java.lang.Math.random;

public class RetryDecente {
    
    static class GuateoException extends RuntimeException {};
    
    static void servicioPenca() throws GuateoException {
	out.println("Servicio invocado");
	if (random() < 0.8) {
	    throw new GuateoException();
	}
	out.println("Servicio ejecutado exitosamente");
    }

    static void solucion() {
	retry(10, GuateoException.class, 2000, new Runnable() {
	    public void run() {
		servicioPenca();
	    }
	});
    }

    static void retry(int times, Class<? extends RuntimeException> exceptionClass, 
		      int sleep_millis, Runnable what) {
	RuntimeException lastException = null;
	while (times-- > 0) {
	    try {
		what.run();
		return;
	    } catch (RuntimeException e) {
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

    static void sleep(int millis) {
	try {
	    Thread.sleep(millis);
	} catch (InterruptedException e) {};    
    }
    
    public static void main(String[] args) {
	solucion();
    }
}