package co.edu.unbosque.proyectofinalback.util.event;

import co.edu.unbosque.proyectofinalback.model.productos.Cuenta;
import co.edu.unbosque.proyectofinalback.model.productos.Tarjeta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CuentaEvent {

    private Cuenta cuenta;


}
