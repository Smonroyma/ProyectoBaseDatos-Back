package co.edu.unbosque.proyectofinalback.service;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.auditoria.EventoAuditoria;
import co.edu.unbosque.proyectofinalback.model.enums.EstadoActor;
import co.edu.unbosque.proyectofinalback.model.enums.Severidad;
import co.edu.unbosque.proyectofinalback.model.enums.TipoResponsable;
import co.edu.unbosque.proyectofinalback.model.fraude.ReglaFraude;
import co.edu.unbosque.proyectofinalback.model.productos.Cuenta;
import co.edu.unbosque.proyectofinalback.model.transaccion.Canal;
import co.edu.unbosque.proyectofinalback.model.transaccion.EstadoTransaccion;
import co.edu.unbosque.proyectofinalback.model.transaccion.Transaccion;
import co.edu.unbosque.proyectofinalback.repository.ReglaFraudeRepositorio;
import co.edu.unbosque.proyectofinalback.util.exception.FraudeDetectadoException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FraudeService {

    private final NotificacionService notificacionService;
    private final ReglaFraudeRepositorio reglaFraudeRepositorio;
    private final @Lazy TransaccionService transaccionService;
    private final CatalogoService catalogoService;
    private final ModelMapper modelMapper;
    private final AuditoriaService auditoriaService;

    private final TransaccionAuditoriaService transaccionAuditoriaService;

    public FraudeService(NotificacionService notificacionService, ReglaFraudeRepositorio reglaFraudeRepositorio,
                         @Lazy TransaccionService transaccionService, CatalogoService catalogoService,
                         AuditoriaService auditoriaService, ModelMapper modelMapper,
                         TransaccionAuditoriaService transaccionAuditoriaService) {
        this.notificacionService = notificacionService;
        this.reglaFraudeRepositorio = reglaFraudeRepositorio;
        this.transaccionService = transaccionService;
        this.catalogoService = catalogoService;
        this.modelMapper = modelMapper;
        this.auditoriaService = auditoriaService;
        this.transaccionAuditoriaService = transaccionAuditoriaService;
    }

    public void revisarTransaccion(Cliente cliente, Cuenta cuenta, Cuenta cuentaDestino, BigDecimal monto, int canalId, Transaccion transaccion) {

        Cliente clienteDestino = cuentaDestino.getCliente();
        String emailCliente = cliente.getEmail();
        String nombreClienteDestino = clienteDestino.getNombre();
        String montoString = String.valueOf(monto);
        LocalDateTime hora = LocalDateTime.now();

        if (clienteDestino.getEstadoCliente().equals(EstadoActor.inactivo)||
                clienteDestino.getEstadoCliente().equals(EstadoActor.suspendido) ||
                clienteDestino.getEstadoCliente().equals(EstadoActor.bloqueado)) {

            auditar(cliente, "Se detecto una accion no permitida", "El cliente no puede ejecutar transacciones su estado es: " + clienteDestino.getEstadoCliente());

            bloquearYLanzar(emailCliente, montoString, nombreClienteDestino, hora,
                    "La cuenta del destinatario se encuentra: " + clienteDestino.getEstadoCliente());
        }

        List<ReglaFraude> reglasActivas = new ArrayList<>();
        reglasActivas.addAll(reglaFraudeRepositorio.findAllByActiva("true"));
        reglasActivas.addAll(reglaFraudeRepositorio.findAllByActiva("TRUE"));

        for (ReglaFraude regla : reglasActivas) {

            LocalDateTime tiempoLimite = regla.getVentanaMinutos() != null
                    ? hora.minusMinutes(regla.getVentanaMinutos())
                    : hora;

            switch (regla.getTipoRegla()) {

                case "monto_umbral":
                    if (monto.compareTo(regla.getUmbral()) > 0) {

                        // Reemplazo
                        transaccionAuditoriaService.registrarAlerta(regla, transaccion, 3, "monto_umbral", Severidad.baja,
                                "El monto de " + monto + " supera el umbral máximo de seguridad");

                        auditar(cliente, "Se detecto un fraude", "El monto de " + monto + " supera el umbral máximo de seguridad");

                        bloquearYLanzar(emailCliente, montoString, nombreClienteDestino, hora,
                                "El monto de " + monto + " supera el umbral máximo de seguridad de " + regla.getUmbral());
                    }
                    break;

                case "frecuencia":
                    long transaccionesExitosas = transaccionService.frecuencia(cuenta, tiempoLimite,
                            modelMapper.map(catalogoService.encontrarEstadoTransPorNombre("aplicada"), EstadoTransaccion.class));

                    if (transaccionesExitosas >= regla.getMaxItentos()) {

                        transaccionAuditoriaService.registrarAlerta(regla, transaccion, 2, "frecuencia", Severidad.media,
                                "Se excedió la frecuencia permitida de transacciones en corto tiempo");

                        auditar(cliente, "Se detecto un fraude", "Se excedio la frecuencia permitida de transacciones en corto tiempo");

                        bloquearYLanzar(emailCliente, montoString, nombreClienteDestino, hora,
                                "Se excedió la frecuencia permitida de transacciones en corto tiempo.");
                    }
                    break;

                case "multiples_destinos":
                    long destinosDiferentes = transaccionService.multiplesDestinos(cuenta, tiempoLimite);

                    if (destinosDiferentes >= regla.getMaxItentos()) {

                        transaccionAuditoriaService.registrarAlerta(regla, transaccion, 2, "multiples_destinos", Severidad.media,
                                "Múltiples destinos distintos detectados en un lapso muy corto");

                        auditar(cliente, "Se detecto un fraude", "Multiples destinos distintos detectados en un lapso muy corto");

                        bloquearYLanzar(emailCliente, montoString, nombreClienteDestino, hora,
                                "Múltiples destinos distintos detectados en un lapso muy corto.");
                    }
                    break;

                case "intentos_fallidos":
                    long intentosFallidos = transaccionService.intentosFallidos(cuenta, tiempoLimite,
                            modelMapper.map(catalogoService.encontrarEstadoTransPorNombre("rechazada"), EstadoTransaccion.class));

                    if (intentosFallidos >= regla.getMaxItentos()) {

                        transaccionAuditoriaService.registrarAlerta(regla, transaccion, 3, "intentos_fallidos", Severidad.media,
                                "Demasiados intentos de transacción fallidos recientemente");

                        auditar(cliente, "Se detecto un fraude", "Demasiados intentos de transaccion fallidos recientemente");

                        bloquearYLanzar(emailCliente, montoString, nombreClienteDestino, hora,
                                "Demasiados intentos de transacción fallidos recientemente.");
                    }
                    break;

                case "canal_inusual":
                    if (catalogoService.encontrarCanalPorId(canalId) == null) {

                        transaccionAuditoriaService.registrarAlerta(regla, transaccion, 2, "canal_inusual", Severidad.critica,
                                "Intento de transacción desde un canal no reconocido (ID: " + canalId + ")");

                        auditar(cliente, "Se detecto un fraude", "Intento de transaccion desde un canal no reconocido (ID: " + canalId + ")");

                        bloquearYLanzar(emailCliente, montoString, nombreClienteDestino, hora,
                                "Intento de transacción desde un canal no reconocido o inhabilitado (Canal ID: " + canalId + ").");
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private void bloquearYLanzar(String email, String montoStr, String destinatario, LocalDateTime hora, String mensaje) {
        notificacionService.enviarNotificacionBloqueo(email, "FRAUDE DETECTADO - " + mensaje, montoStr, destinatario, hora);
        throw new FraudeDetectadoException("TRANSACCION CANCELADA - " + mensaje);
    }

    private void auditar(Cliente cliente, String accion, String motivo){

        EventoAuditoria eventoAuditoria = EventoAuditoria.builder()
                .cliente(cliente)
                .canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class))
                .tipoResponsable(TipoResponsable.sistema)
                .accion(accion)
                .fechaHora(LocalDateTime.now())
                .motivo(motivo)
                .build();

        auditoriaService.guardarEvento(eventoAuditoria);
    }
}