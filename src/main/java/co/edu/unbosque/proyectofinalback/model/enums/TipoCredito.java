package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoCredito {

    consumo,
    libre_inversion,
    vehiculo,
    hipotecario,
    educativo; 

    @JsonCreator
    public static TipoCredito parseTipoCredito(String tipoCredito) {
        if (tipoCredito == null || tipoCredito.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = tipoCredito.trim()
                .toLowerCase().replace(" ", "_");

        for (TipoCredito v : TipoCredito.values()) {
            if (v.name().equals(textoLimpio)) {
                return v;
            }
        }

        throw new IllegalArgumentException("Estado no reconocido: " + tipoCredito);
    }

}
