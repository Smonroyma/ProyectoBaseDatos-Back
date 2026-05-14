package co.edu.unbosque.proyectofinalback.service;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.actores.ClienteDTO;
import co.edu.unbosque.proyectofinalback.model.auditoria.EventoAuditoria;
import co.edu.unbosque.proyectofinalback.model.enums.EstadoActor;
import co.edu.unbosque.proyectofinalback.model.enums.TipoResponsable;
import co.edu.unbosque.proyectofinalback.model.transaccion.Canal;
import co.edu.unbosque.proyectofinalback.model.transaccion.CanalDTO;
import co.edu.unbosque.proyectofinalback.repository.ClienteRepositorio;
import co.edu.unbosque.proyectofinalback.util.event.ClienteCreadoEvent;
import co.edu.unbosque.proyectofinalback.util.exception.NoEncontradoException;
import co.edu.unbosque.proyectofinalback.util.exception.ReglaNegocioException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteService {

    private final ApplicationEventPublisher eventPublisher;
    private final ClienteRepositorio clienteRepositorio;
    private final CatalogoService catalogoService;
    private final AuditoriaService auditoriaService;
    private final ModelMapper modelMapper;

    public ClienteService(ClienteRepositorio clienteRepositorio, CatalogoService catalogoService,
                          AuditoriaService auditoriaService, ModelMapper modelMapper,
                          ApplicationEventPublisher eventPublisher) {
        this.clienteRepositorio = clienteRepositorio;
        this.catalogoService = catalogoService;
        this.auditoriaService = auditoriaService;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ClienteDTO crear(ClienteDTO clienteDTO) {

        clienteDTO.setIdCliente(null);

        clienteDTO.setFechaAlta(LocalDateTime.now());

        if (clienteDTO.getEstadoCliente() == null) {
            clienteDTO.setEstadoCliente(EstadoActor.activo);
        }

        if (clienteRepositorio.existsClienteByNumeroDocumento(clienteDTO.getNumeroDocumento())) {
            throw new ReglaNegocioException("ERROR: Ya existe un cliente con el NoDoc: " + clienteDTO.getNumeroDocumento());
        }

        Cliente clienteGuardado = clienteRepositorio.saveAndFlush(modelMapper.map(clienteDTO, Cliente.class));

        eventPublisher.publishEvent(new ClienteCreadoEvent(clienteGuardado));

        return modelMapper.map(clienteGuardado, ClienteDTO.class);
    }

    @Transactional
    public boolean eliminarPorId(int id) {

        if (clienteRepositorio.existsClienteByIdCliente(id)) {
            Cliente cliente = clienteRepositorio.findClienteByIdCliente(id);
            cliente.setEstadoCliente(EstadoActor.bloqueado);
            clienteRepositorio.save(cliente);


            auditar(cliente, "Se desactivo un cliente", "Se desactivo un cliente");


            return true;
        } else {
            throw new NoEncontradoException("No se encontro el cliente con el id:" + id);
        }

    }

    @Transactional
    public boolean eliminarPorDocumento(String Documento) {

        if (clienteRepositorio.existsByNumeroDocumento(Documento)) {
            Cliente cliente = clienteRepositorio.findByNumeroDocumento(Documento);
            cliente.setEstadoCliente(EstadoActor.inactivo);
            clienteRepositorio.save(cliente);

            auditar(cliente, "Se desactivo un cliente", "Se desactivo un cliente");

            return true;
        } else {
            throw new NoEncontradoException("No se encontro el cliente con el documento:" + Documento);
        }


    }

    @Transactional
    public ClienteDTO actualizarPorId(ClienteDTO clienteDTO) {

        if (clienteRepositorio.existsClienteByIdCliente(clienteDTO.getIdCliente())) {

            Cliente cliente = clienteRepositorio.findClienteByIdCliente(clienteDTO.getIdCliente());

            cliente.setTipoDocumento(clienteDTO.getTipoDocumento());
            cliente.setNumeroDocumento(clienteDTO.getNumeroDocumento());
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setApellido(clienteDTO.getApellido());
            cliente.setDireccionPrincipal(clienteDTO.getDireccionPrincipal());
            cliente.setCiudad(clienteDTO.getCiudad());
            cliente.setTelefono(clienteDTO.getTelefono());
            cliente.setEmail(clienteDTO.getEmail());
            cliente.setEstadoCliente(clienteDTO.getEstadoCliente());

            if(cliente.getEstadoCliente() == null){
                cliente.setEstadoCliente(EstadoActor.activo);
            }

            auditar(cliente, "Se actualizacion los datos de un cliente", "El cliente realizo una actualizacion de datos");

            return modelMapper.map(clienteRepositorio.save(cliente), ClienteDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro el cliente con el id:" + clienteDTO.getIdCliente());
        }

    }

    @Transactional
    public ClienteDTO actualizarPorDocumento(ClienteDTO clienteDTO) {

        if (clienteRepositorio.existsByNumeroDocumento(clienteDTO.getNumeroDocumento())) {

            Cliente cliente = clienteRepositorio.findByNumeroDocumento(clienteDTO.getNumeroDocumento());

            cliente.setTipoDocumento(clienteDTO.getTipoDocumento());
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setApellido(clienteDTO.getApellido());
            cliente.setDireccionPrincipal(clienteDTO.getDireccionPrincipal());
            cliente.setCiudad(clienteDTO.getCiudad());
            cliente.setTelefono(clienteDTO.getTelefono());
            cliente.setEmail(clienteDTO.getEmail());
            cliente.setEstadoCliente(clienteDTO.getEstadoCliente());

            auditar(cliente, "Se actualizacion los datos de un cliente", "El cliente realizo una actualizacion de datos");

            return modelMapper.map(clienteRepositorio.save(cliente), ClienteDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro el cliente con el documento:" + clienteDTO.getNumeroDocumento());
        }

    }

    @Transactional
    public ClienteDTO actualizarEstado(int id, String estado) {

        if (clienteRepositorio.existsClienteByIdCliente(id)) {
            Cliente cliente = clienteRepositorio.findClienteByIdCliente(id);
            cliente.setEstadoCliente(EstadoActor.parseEstado(estado));

            auditar(cliente, "Se actualizo el estado del cliente a:" + estado, "Se acutalizo el estado de cliente");


            return modelMapper.map(cliente, ClienteDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro el cliente con el id:" + id);
        }

    }


    public ClienteDTO encontrarClienteId(int id) {

        if(clienteRepositorio.existsClienteByIdCliente(id)){
            return modelMapper.map(clienteRepositorio.findClienteByIdCliente(id), ClienteDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro el cliente con el id: " + id);
        }
    }

    public ClienteDTO encontrarClienteDocumento(String documento){

        if(clienteRepositorio.existsByNumeroDocumento(documento)){
            return modelMapper.map(clienteRepositorio.findByNumeroDocumento(documento), ClienteDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro el cliente con el documento: " + documento);
        }

    }

    public List<ClienteDTO> todosLosClientes(){

        return clienteRepositorio.findAll().stream().map(x -> modelMapper.map(x, ClienteDTO.class)).toList();

    }

    private void auditar(Cliente cliente,  String accion, String motivo){
        CanalDTO canalDto = catalogoService.encontrarCanalPorId(1);
        if (canalDto == null) {
            throw new NoEncontradoException("Error de configuración: No existe el canal con ID 1 para auditoría");
        }

        EventoAuditoria auditoria = EventoAuditoria.builder().
                cliente(cliente).
                canal(modelMapper.map(canalDto, Canal.class)).
                tipoResponsable(TipoResponsable.empleado).
                accion(accion).
                fechaHora(LocalDateTime.now()).
                motivo(motivo).build();

        auditoriaService.guardarEvento(auditoria);
    }

}
