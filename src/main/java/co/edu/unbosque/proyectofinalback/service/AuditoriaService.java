package co.edu.unbosque.proyectofinalback.service;

import co.edu.unbosque.proyectofinalback.model.auditoria.EventoAuditoria;
import co.edu.unbosque.proyectofinalback.repository.EventoAuditoriaRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaService {

    private final EventoAuditoriaRepositorio auditoriaRepositorio;

    public AuditoriaService(EventoAuditoriaRepositorio auditoriaRepositorio) {
        this.auditoriaRepositorio = auditoriaRepositorio;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void guardarEvento(EventoAuditoria evento) {
        auditoriaRepositorio.save(evento);
    }

}
