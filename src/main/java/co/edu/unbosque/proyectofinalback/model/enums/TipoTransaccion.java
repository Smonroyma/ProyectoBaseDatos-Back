package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoTransaccion {

    deposito,
    retiro,
    transferencia,
    compra_tarjeta,
    pago,
    anulacion,
    reverso;

    @JsonCreator
    public static TipoTransaccion tipoTransaccion(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = tipo.trim().toLowerCase().replace(" ", "_");

        for (TipoTransaccion tipoValores : TipoTransaccion.values()) {
            if (tipoValores.name().equals(textoLimpio)) {
                return tipoValores;
            }
        }

        throw new IllegalArgumentException("El estado '" + tipo + " no es valido");
    }

}
