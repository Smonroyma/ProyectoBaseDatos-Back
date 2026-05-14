package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoCuenta {

    ahorro,
    corriente;

    @JsonCreator
    public static TipoCuenta parseTipoCuenta(String tipoCuenta) {
        if (tipoCuenta == null || tipoCuenta.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = tipoCuenta.trim().toLowerCase();

        for (TipoCuenta v : TipoCuenta.values()) {
            if (v.name().equals(textoLimpio)) {
                return v;
            }
        }

        throw new IllegalArgumentException("Moneda no reconocida: " + tipoCuenta);
    }

}
