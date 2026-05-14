package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoTarjeta {

    debito,
    credito;

    @JsonCreator
    public static TipoTarjeta parseTipotarjeta(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = tipo.trim().toLowerCase().replace(" ", "_");

        for (TipoTarjeta tipoValores : TipoTarjeta.values()) {
            if (tipoValores.name().equals(textoLimpio)) {
                return tipoValores;
            }
        }

        throw new IllegalArgumentException("El tipo de tarjeta: " + tipo + " no es valido");
    }
}
