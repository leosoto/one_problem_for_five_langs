function servicioPenca() {
    print("Servicio invocado");
    if (Math.random() < 0.8) {
	throw "GuateoException";
    }
    print("Servicio ejecutado exitosamente");
}

function solucion() {
    retry({times: 10, when: "GuateoException", wait: 2}, servicioPenca);
}

function retry(options, callback) {
    var lastException = null;
    var times = options.times || 10;
    var exception = options.when;
    var wait = options.wait;
    while (times-- > 0) {
        try {
            return callback();
	} catch (e) {
	    if (!exception || exception == e) {
		sleep(wait);
		lastException = e;
	    } else {
		throw e;
	    }
	}
    }
    throw lastException;
}

// No sleep funcion in JS so we implement it with a busy loop
function sleep(seconds) {
    var start = new Date().getTime();
    while(true) {
	if (new Date().getTime() - start > (seconds * 1000)) break;
    }
}

solucion();
