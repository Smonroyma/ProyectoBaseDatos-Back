package co.edu.unbosque.proyectofinalback.util.event;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import lombok.Getter;

@Getter
public class ClienteCreadoEvent {

    private final Cliente cliente;

    public ClienteCreadoEvent(Cliente cliente) {
        this.cliente = cliente;
    }

}
