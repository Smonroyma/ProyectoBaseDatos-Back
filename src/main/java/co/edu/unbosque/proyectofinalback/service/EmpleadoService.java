package co.edu.unbosque.proyectofinalback.service;

import co.edu.unbosque.proyectofinalback.model.actores.Empleado;
import co.edu.unbosque.proyectofinalback.model.actores.EmpleadoDTO;
import co.edu.unbosque.proyectofinalback.model.enums.EstadoActor;
import co.edu.unbosque.proyectofinalback.repository.EmpleadoRepositorio;
import co.edu.unbosque.proyectofinalback.util.event.ClienteCreadoEvent;
import co.edu.unbosque.proyectofinalback.util.event.EmpleadoCreadoEvent;
import co.edu.unbosque.proyectofinalback.util.exception.NoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepositorio empleadoRepositorio;
    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;

    public EmpleadoService(EmpleadoRepositorio empleadoRepositorio,
                           ModelMapper modelMapper, ApplicationEventPublisher eventPublisher) {
        this.empleadoRepositorio = empleadoRepositorio;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    public EmpleadoDTO crear(EmpleadoDTO empleadoDTO) {

        Empleado empleado = Empleado.builder()
                .nombre(empleadoDTO.getNombre())
                .apellido(empleadoDTO.getApellido())
                .cargo(empleadoDTO.getCargo())
                .estadoEmpleado(EstadoActor.activo)
                .fechaAlta(LocalDateTime.now())
                .build();

        Empleado empleadoGuardado = empleadoRepositorio.save(empleado);

        eventPublisher.publishEvent(new EmpleadoCreadoEvent(empleadoGuardado));

        return modelMapper.map(empleado, EmpleadoDTO.class);

    }

    public boolean eliminar(int idEmpleado) {

        Empleado empleado = empleadoRepositorio.findById(idEmpleado)
                .orElseThrow(() -> new NoEncontradoException("No se encontro un empleado con el id: " + idEmpleado));

        empleado.setEstadoEmpleado(EstadoActor.inactivo);

        empleadoRepositorio.save(empleado);
        return true;
    }

    public EmpleadoDTO actualizar(EmpleadoDTO empleadoDTO) {

        Empleado empleado = empleadoRepositorio.findById(empleadoDTO.getIdEmpleado())
                .orElseThrow(() -> new NoEncontradoException("No se encontro un emeplado con el id: " + empleadoDTO.getIdEmpleado()));

        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setApellido(empleadoDTO.getApellido());
        empleado.setCargo(empleadoDTO.getCargo());

        return modelMapper.map(empleadoRepositorio.save(empleado), EmpleadoDTO.class);

    }

    public EmpleadoDTO actualizarEstado(int idEmpleado, String estado) {

        Empleado empleado = empleadoRepositorio.findById(idEmpleado)
                .orElseThrow(() -> new NoEncontradoException("No se encontro un emeplado con el id: " + idEmpleado));

        empleado.setEstadoEmpleado(EstadoActor.parseEstado(estado));
        return modelMapper.map(empleadoRepositorio.save(empleado), EmpleadoDTO.class);
    }

    public EmpleadoDTO encontrarEmpleadoId(int idEmpleado) {

        return modelMapper.map(empleadoRepositorio.findById(idEmpleado)
                .orElseThrow(() -> new NoEncontradoException("No se encontro un emeplado con el id: " + idEmpleado)), EmpleadoDTO.class);

    }

    public List<EmpleadoDTO> encontrarTodos() {

        return empleadoRepositorio.findAll().stream().map(x -> modelMapper.map(x, EmpleadoDTO.class)).toList();

    }


}
