import java.lang.Math;

class GuateoException extends Exception;

object Retry {
  def servicioPenca {
    println("Servicio invocado");
    if (Math.random < 0.8) {
      throw new GuateoException
    }
    println("Servicio ejecutado exitosamente");
  }

  def solucion {
    retry(times=10, when=classOf[GuateoException], wait=2000) {
      servicioPenca
    }
  }

  def retry[R, E <: Exception](times: Int, when: Class[E], wait: Int)(body: => R): R = {
    var lastException:Throwable = null;
    for (i <- 0 until times) {
      try {
	return body
      } catch {
	case e  => {
	  if (when isAssignableFrom e.getClass()) {
	    lastException = e
	    Thread.sleep(wait)
	  } else {
	    throw e
	  }
	}
      }
    }
    throw lastException;
  }

  def main(args: Array[String]) = solucion;
}
