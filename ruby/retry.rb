class GuateoException < Exception; end

def servicio_penca
  puts "Servicio invocado"
  raise GuateoException if rand < 0.8
  puts "Servicio ejecutado exitosamente"
end

def solucion
  retrying 10.times, :when => GuateoException, :wait => 2 do
    servicio_penca
  end
end

def retrying(n_times, options = {})
  exception_class = options[:when] || Exception
  wait_time = options[:wait] || 0
  last_exception = nil
  n_times.each do
    begin
      return yield
    rescue exception_class => e
      last_exception = e
      sleep wait_time      
    end
  end
  raise last_exception
end

if __FILE__ == $0
    solucion
end

