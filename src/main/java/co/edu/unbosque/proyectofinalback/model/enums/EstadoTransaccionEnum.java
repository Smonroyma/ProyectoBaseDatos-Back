package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EstadoTransaccionEnum {

    pendiente,
    aplicada,
    rechazada,
    reversada;

    @JsonCreator
    public static EstadoTransaccionEnum parseEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = estado.trim().toLowerCase();

        for (EstadoTransaccionEnum estadoValores : EstadoTransaccionEnum.values()) {
            if (estadoValores.name().equals(textoLimpio)) {
                return estadoValores;
            }
        }

        throw new IllegalArgumentException("El estado '" + estado + " no es valido");
    }

}
