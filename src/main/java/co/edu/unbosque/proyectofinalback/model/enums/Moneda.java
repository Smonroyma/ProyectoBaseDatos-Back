package co.edu.unbosque.proyectofinalback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Moneda {

    USD, // Dólar estadounidense
    EUR, // Euro
    JPY, // Yen japonés
    GBP, // Libra esterlina
    CNY, // Yuan chino
    CAD, // Dólar canadiense
    CHF, // Franco suizo
    AUD, // Dólar australiano
    KWD, // Dinar kuwaití
    INR, // Rupia india
    BRL, // Real brasileño
    MXN, // Peso mexicano
    KRW, // Won surcoreano
    HKD, // Dólar de Hong Kong
    BHD, // Dinar bahreiní
    NOK, // Corona noruega
    OMR, // Rial omaní
    NZD, // Dólar neozelandés
    RUB, // Rublo ruso
    COP; // Peso colombiano

    @JsonCreator
    public static Moneda parseMoneda(String moneda) {
        if (moneda == null || moneda.trim().isEmpty()) {
            return null;
        }

        String textoLimpio = moneda.trim()
                .toUpperCase();

        for (Moneda v : Moneda.values()) {
            if (v.name().equals(textoLimpio)) {
                return v;
            }
        }

        throw new IllegalArgumentException("Moneda no reconocida: " + moneda);
    }

}
