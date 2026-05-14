package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoResponsable {

    sistema,
    empleado,
    cliente;

    @JsonCreator
    public static TipoResponsable tipoResponsable(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = tipo.trim().toLowerCase();

        for (TipoResponsable tipoResponsable : TipoResponsable.values()) {
            if (tipoResponsable.name().equals(textoLimpio)) {
                return tipoResponsable;
            }
        }

        throw new IllegalArgumentException("El estado '" + tipo + " no es valido");
    }

}
