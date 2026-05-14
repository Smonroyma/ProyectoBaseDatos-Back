package co.edu.unbosque.proyectofinalback.service;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoProductoEnum;
import co.edu.unbosque.proyectofinalback.model.enums.Severidad;
import co.edu.unbosque.proyectofinalback.model.fraude.AlertaFraude;
import co.edu.unbosque.proyectofinalback.model.fraude.AlertaTransaccion;
import co.edu.unbosque.proyectofinalback.model.fraude.ReglaFraude;
import co.edu.unbosque.proyectofinalback.model.transaccion.EstadoTransaccion;
import co.edu.unbosque.proyectofinalback.model.transaccion.EstadoTransaccionDTO;
import co.edu.unbosque.proyectofinalback.model.transaccion.Transaccion;
import co.edu.unbosque.proyectofinalback.repository.AlertaFraudeRepositorio;
import co.edu.unbosque.proyectofinalback.repository.AlertaTransaccionRepositorio;
import co.edu.unbosque.proyectofinalback.repository.EstadoAlertaRepositorio;
import co.edu.unbosque.proyectofinalback.repository.TransaccionRepositorio;
import co.edu.unbosque.proyectofinalback.service.CatalogoService;
import co.edu.unbosque.proyectofinalback.util.exception.NoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Service
public class TransaccionAuditoriaService {

    private final TransaccionRepositorio transaccionRepositorio;
    private final CatalogoService catalogoService;
    private final ModelMapper modelMapper;
    private final AlertaFraudeRepositorio alertaFraudeRepositorio;
    private final AlertaTransaccionRepositorio alertaTransaccionRepositorio;
    private final EstadoAlertaRepositorio estadoAlertaRepositorio;

    public TransaccionAuditoriaService(TransaccionRepositorio transaccionRepositorio, CatalogoService catalogoService,
                                       ModelMapper modelMapper, AlertaFraudeRepositorio alertaFraudeRepositorio,
                                       AlertaTransaccionRepositorio alertaTransaccionRepositorio, EstadoAlertaRepositorio estadoAlertaRepositorio) {
        this.transaccionRepositorio = transaccionRepositorio;
        this.catalogoService = catalogoService;
        this.modelMapper = modelMapper;
        this.alertaFraudeRepositorio = alertaFraudeRepositorio;
        this.alertaTransaccionRepositorio = alertaTransaccionRepositorio;
        this.estadoAlertaRepositorio = estadoAlertaRepositorio;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarTransaccionInicio(Transaccion transaccion) {
        transaccionRepositorio.save(transaccion);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarRechazo(int id, String motivo){

        transaccionRepositorio.findById(id).ifPresent(trans -> {
            trans.setMotivo(motivo);
            trans.setEstadoTransaccion(modelMapper.map(catalogoService.encontrarEstadoTransPorId(3), EstadoTransaccion.class));
            trans.setFallida(true);
            transaccionRepositorio.save(trans);
        } );

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarAlerta(ReglaFraude regla, Transaccion transaccion, int idEstado, String tipo, Severidad sev, String desc) {

        AlertaFraude alerta = AlertaFraude.builder()
                .regla(regla)
                .estadoAlerta(estadoAlertaRepositorio.findById(idEstado)
                        .orElseThrow(() -> new NoEncontradoException("No se encontro el estado de alerta con id: " + idEstado)))
                .fechaHora(LocalDateTime.now())
                .tipoAlerta(tipo)
                .severidad(sev)
                .descripcion(desc)
                .build();
        alertaFraudeRepositorio.save(alerta);

        AlertaTransaccion alertaTransaccion = AlertaTransaccion.builder()
                .alerta(alerta)
                .transaccion(transaccion)
                .fechaAsociacion(LocalDateTime.now())
                .build();
        alertaTransaccionRepositorio.save(alertaTransaccion);
    }


}
