package co.edu.unbosque.proyectofinalback.util.event;

import co.edu.unbosque.proyectofinalback.model.actores.Empleado;
import co.edu.unbosque.proyectofinalback.model.productos.Cuenta;
import co.edu.unbosque.proyectofinalback.model.productos.Tarjeta;
import lombok.Getter;

@Getter
public class EmpleadoCreadoEvent {

    private final Empleado empleado;

    public EmpleadoCreadoEvent(Empleado empleado) {
        this.empleado = empleado;
    }

}
