import static java.lang.System.out;
import static java.lang.Math.random;

public class RetryPenca {
    
    static class GuateoException extends Exception {};
    
    static void servicioPenca() throws GuateoException {
	out.println("Servicio invocado");
	if (random() < 0.8) {
	    throw new GuateoException();
	}
	out.println("Servicio ejecutado exitosamente");
    }

    static void solucion() throws GuateoException {
	int times = 10;
	int wait = 2 * 1000; // 2 segundos
	GuateoException lastException = null;
	while (times-- > 0) {
	    try {
		servicioPenca(); 
		return;
	    } catch (GuateoException e) {
		lastException = e;
		sleep(wait);
	    }
	}
	throw lastException;
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