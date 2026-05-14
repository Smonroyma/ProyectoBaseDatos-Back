package co.edu.unbosque.proyectofinalback.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificacionService {

    private final JavaMailSender mailSender;
    private static final String asunto = "SEGURIDAD BOSQUEBANK 🌲";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public NotificacionService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void enviarNotificacionBloqueo(String correoDestino, String razon, String monto, String destinatario, LocalDateTime fechaHora) {
        String mensaje = String.format(
                "TRANSACCION BLOQUEADA - Esta transacción se ha cancelado por: %s\n\n" +
                "Detalles de la operación:\n" +
                "------------------------------------------\n" +
                "Motivo: %s\n" +
                "Monto: %s\n" +
                "Enviado a: %s\n" +
                "Hora exacta: %s\n" +
                "------------------------------------------\n\n" +
                "Si no reconoce esta actividad, por favor comuníquese de inmediato con nuestras líneas de atención.",
                razon, razon, monto, destinatario, fechaHora.format(FORMATTER)
        );

        enviarCorreo(correoDestino, asunto, mensaje);
    }

    private void enviarCorreo(String correoDestino, String asunto, String mensaje) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("bosquebank@gmail.com");
            email.setTo(correoDestino);
            email.setSubject(asunto);
            email.setText(mensaje);

            mailSender.send(email);
            System.out.println("Notificación de seguridad enviada a: " + correoDestino);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}
