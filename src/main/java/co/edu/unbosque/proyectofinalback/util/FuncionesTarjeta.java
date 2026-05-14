package co.edu.unbosque.proyectofinalback.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;

public class FuncionesTarjeta {

    private static final String ALGORITMO = "AES";
    private static final String LLAVE = "1234567890123456";


    public static String crearNumero(String tipo) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        if (tipo.equalsIgnoreCase("debito")) {
            sb.append("2207");
        } else {
            sb.append("2208");
        }

        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public static String enmascarar(String numeroTarjeta) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(LLAVE.getBytes(), ALGORITMO);
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encriptado = cipher.doFinal(numeroTarjeta.getBytes());
        return Base64.getEncoder().encodeToString(encriptado);
    }

    public static String desenmascarar(String numeroTarjeta) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(LLAVE.getBytes(), ALGORITMO);
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(numeroTarjeta));
        return new String(original);
    }

    public static String generarVencimiento() {
        LocalDate fechaVence = LocalDate.now().plusYears(5);
        DateTimeFormatter fecha = DateTimeFormatter.ofPattern("MM/yy");
        return fechaVence.format(fecha);
    }

}
