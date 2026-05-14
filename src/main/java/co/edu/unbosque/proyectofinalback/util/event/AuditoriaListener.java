package co.edu.unbosque.proyectofinalback.util.event;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.actores.Empleado;
import co.edu.unbosque.proyectofinalback.model.auditoria.EventoAuditoria;
import co.edu.unbosque.proyectofinalback.model.enums.TipoResponsable;
import co.edu.unbosque.proyectofinalback.model.productos.Credito;
import co.edu.unbosque.proyectofinalback.model.productos.Cuenta;
import co.edu.unbosque.proyectofinalback.model.productos.Tarjeta;
import co.edu.unbosque.proyectofinalback.model.transaccion.Canal;
import co.edu.unbosque.proyectofinalback.service.AuditoriaService;
import co.edu.unbosque.proyectofinalback.service.CatalogoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
public class AuditoriaListener {

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CatalogoService catalogoService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void procesarAuditoriaCliente(ClienteCreadoEvent event) {
        Cliente cliente = event.getCliente();

        EventoAuditoria auditoria = EventoAuditoria.builder()
                .cliente(cliente)
                .canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class))
                .tipoResponsable(TipoResponsable.cliente)
                .accion("Se creó un cliente")
                .fechaHora(LocalDateTime.now())
                .motivo("Registro nuevo en BosqueBank")
                .build();

        auditoriaService.guardarEvento(auditoria);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void procesarAuditoriaEmpleado(EmpleadoCreadoEvent event) {

        Empleado empleado = event.getEmpleado();

        EventoAuditoria auditoria = EventoAuditoria.builder()
                .empleado(empleado)
                .canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class))
                .tipoResponsable(TipoResponsable.empleado)
                .accion("Se creó un empleado")
                .fechaHora(LocalDateTime.now())
                .motivo("Registro nuevo en BosqueBank")
                .build();

        auditoriaService.guardarEvento(auditoria);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void procesarAuditoriaCredito(CreditoEvent event){

        Credito credito = event.getCredito();

        EventoAuditoria auditoria = EventoAuditoria.builder()
                .cliente(credito.getCliente())
                .producto(credito)
                .canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class))
                .tipoResponsable(TipoResponsable.cliente)
                .accion("Se creó un credito")
                .fechaHora(LocalDateTime.now())
                .motivo("Registro nuevo en BosqueBank")
                .build();

        auditoriaService.guardarEvento(auditoria);

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void procesarAuditoriaCuenta(CuentaEvent event){

        Cuenta cuenta = event.getCuenta();

        EventoAuditoria auditoria = EventoAuditoria.builder()
                .cliente(cuenta.getCliente())
                .producto(cuenta)
                .canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class))
                .tipoResponsable(TipoResponsable.cliente)
                .accion("Se creó un cuenta")
                .fechaHora(LocalDateTime.now())
                .motivo("Registro nuevo en BosqueBank")
                .build();

        auditoriaService.guardarEvento(auditoria);

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void procesarAuditoriaTarjeta(TarjetaEvent event){

        Tarjeta tarjeta = event.getTarjeta();

        EventoAuditoria auditoria = EventoAuditoria.builder()
                .cliente(tarjeta.getCliente())
                .producto(tarjeta)
                .canal(modelMapper.map(catalogoService.encontrarCanalPorId(1), Canal.class))
                .tipoResponsable(TipoResponsable.cliente)
                .accion("Se creó un cuenta")
                .fechaHora(LocalDateTime.now())
                .motivo("Registro nuevo en BosqueBank")
                .build();

        auditoriaService.guardarEvento(auditoria);

    }

}
