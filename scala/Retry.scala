/*
Solucion simple y elegante en Scala. Aunque el lenguaje sea estatico, tener
closures y poder indicar el nombre de los parametros hace toda la diferencia
para que la solucion sea legible.

Notar como Scala permite:

 - Usar clases Java como Math
 - Definir un singleton usando object en lugar de class
 - Invocar a metodos sin parametros sin usar parentesis
 - Recibir funciones como parametros y pasar funciones anonimas de manera muy
   elegante
 - Tipar estrictamente las funciones al definirlas (ver retry) pero infiriendo
   los valores adecuados para los parametros genericos de tipo al usarlas,
   sin especificarlos explicitamente.
 - No usar puntos en ciertos metodos (e.g, isAssignableFrom, until)
 - Puntos y coma mas o menos opcionales
*/


import java.lang.Math

class GuateoException extends Exception

object Retry {
  def servicioPenca {
    println("Servicio invocado")
    if (Math.random < 0.8) {
      throw new GuateoException
    }
    println("Servicio ejecutado exitosamente")
  }

  def solucion {
    retry(times=10, when=classOf[GuateoException], wait=2000) {
      servicioPenca
    }
  }

  def retry[R, E <: Exception](times: Int, when: Class[E], wait: Int)(body: => R): R = {
    var lastException:Throwable = null
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
    throw lastException
  }

  def main(args: Array[String]) = solucion
}
