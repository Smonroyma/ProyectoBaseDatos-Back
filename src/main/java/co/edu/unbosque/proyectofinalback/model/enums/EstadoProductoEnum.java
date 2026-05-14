package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EstadoProductoEnum {

    activo,
    bloqueado,
    cancelado;

    @JsonCreator
    public static EstadoProductoEnum parseEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = estado.trim()
                .toLowerCase();

        for (EstadoProductoEnum v : EstadoProductoEnum.values()) {
            if (v.name().equals(textoLimpio)) {
                return v;
            }
        }

        throw new IllegalArgumentException("Estado no reconocido: " + estado);
    }
}
