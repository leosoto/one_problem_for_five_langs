import time, random

class GuateoException(Exception): pass

def servicio_penca():
  print "Servicio invocado"
  if random.random() < 0.8:
      raise GuateoException()
  print "Servicio ejecutado exitosamente"


def solucion():
    retry(servicio_penca, when=GuateoException, times=10, wait=2)

def retry(callable, when, times, wait):
    last_exception = None
    while times > 0:
        times -= 1
        try:
            return callable()
        except when, exception:
            time.sleep(wait)
            last_exception = exception
    raise last_exception

if __name__ == '__main__':
    solucion()
