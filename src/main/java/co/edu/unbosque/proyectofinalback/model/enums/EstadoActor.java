package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EstadoActor {

    activo,
    inactivo,
    bloqueado,
    investigacion,
    suspendido;

    @JsonCreator
    public static EstadoActor parseEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = estado.trim().toLowerCase();

        for (EstadoActor estadoValores : EstadoActor.values()) {
            if (estadoValores.name().equals(textoLimpio)) {
                return estadoValores;
            }
        }

        throw new IllegalArgumentException("El estado '" + estado + " no es valido");
    }
}
