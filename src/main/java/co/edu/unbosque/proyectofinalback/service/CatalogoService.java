package co.edu.unbosque.proyectofinalback.service;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoProductoEnum;
import co.edu.unbosque.proyectofinalback.model.enums.EstadoTransaccionEnum;
import co.edu.unbosque.proyectofinalback.model.fraude.*;
import co.edu.unbosque.proyectofinalback.model.productos.*;
import co.edu.unbosque.proyectofinalback.model.transaccion.*;
import co.edu.unbosque.proyectofinalback.repository.*;
import co.edu.unbosque.proyectofinalback.model.transaccion.EstadoTransaccionDTO;
import co.edu.unbosque.proyectofinalback.util.exception.NoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoService {

    private final CanalRepositorio canalRepositorio;
    private final EstadoTransaccionRepositorio estadoTransaccionRepositorio;
    private final EstadoProductoRepositorio estadoProductoRepositorio;
    private final ReglaFraudeRepositorio reglaFraudeRepositorio;
    private final EstadoAlertaRepositorio estadoAlertaRepositorio;
    private final ModelMapper modelMapper;

    public CatalogoService(CanalRepositorio canalRepositorio, EstadoTransaccionRepositorio estadoTransaccionRepositorio,
                           EstadoProductoRepositorio estadoProductoRepositorio, ReglaFraudeRepositorio reglaFraudeRepositorio,
                           EstadoAlertaRepositorio estadoAlertaRepositorio, ModelMapper modelMapper) {
        this.canalRepositorio = canalRepositorio;
        this.estadoTransaccionRepositorio = estadoTransaccionRepositorio;
        this.estadoProductoRepositorio = estadoProductoRepositorio;
        this.reglaFraudeRepositorio = reglaFraudeRepositorio;
        this.estadoAlertaRepositorio = estadoAlertaRepositorio;
        this.modelMapper = modelMapper;
    }


    /*
     Canal
     */

    public CanalDTO encontrarCanalPorId(int idCanal){

        if(canalRepositorio.existsCanalByIdCanal(idCanal)){
            return modelMapper.map(canalRepositorio.findById(idCanal), CanalDTO.class);
        }else{
            throw new NoEncontradoException("El canal con ID " + idCanal + " no existe en el sistema.");
        }

    }

    public CanalDTO encontrarCanalPorNombre(String nombre) {

        if(canalRepositorio.existsByNombre(nombre)){
            return modelMapper.map(canalRepositorio.findByNombre(nombre.trim().toLowerCase()), CanalDTO.class);
        } else {
            throw new NoEncontradoException("Nos existe el canal con el nombre: " + nombre);
        }
    }

    public List<CanalDTO> encontrarTodosLosCanales(){

        if(canalRepositorio.findAll().isEmpty()){
            throw new NoEncontradoException("No se encontraron canales en el sistema (VACIO)");
        } else {
            return canalRepositorio.findAll().stream().map(e -> modelMapper.map(e, CanalDTO.class)).toList();
        }

    }

    /*
    Estado Transaccion
     */
    public EstadoTransaccionDTO encontrarEstadoTransPorId(int id){

        return modelMapper.map(estadoTransaccionRepositorio.findById(id).orElseThrow(() -> new NoEncontradoException("No se encontro esa transaccion con el id: " + id)), EstadoTransaccionDTO.class);

    }

    public EstadoTransaccionDTO encontrarEstadoTransPorNombre(String nombre){

        EstadoTransaccionEnum estadoEnum = EstadoTransaccionEnum.parseEstado(nombre);
        if(estadoTransaccionRepositorio.existsByNombreEstadoTransaccion(estadoEnum)){
            return modelMapper.map(estadoTransaccionRepositorio.findByNombreEstadoTransaccion(estadoEnum), EstadoTransaccionDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro esa transaccion: " + nombre);
        }

    }

    public List<EstadoTransaccionDTO> encontrarTodosEstadosTrans() {

        if (estadoTransaccionRepositorio.findAll().isEmpty()) {
            throw new NoEncontradoException("No se encontraron estados de transacciones en el sistema (VACIO)");
        } else {
            return estadoTransaccionRepositorio.findAll().stream().map(e -> modelMapper.map(e, EstadoTransaccionDTO.class)).toList();
        }
    }

    /*
    Estado Producto
     */
    public EstadoProductoDTO encontrarEstadoProductoPorId(int id){

        return modelMapper.map(estadoProductoRepositorio.findById(id).orElseThrow(() -> new NoEncontradoException("No se encontro el estado del producto con el id: " + id)), EstadoProductoDTO.class);

    }

    public EstadoProductoDTO encontrarEstadoProductoPorNombre(String nombre){

        if(estadoProductoRepositorio.existsByNombreEstadoProducto(nombre)){
            return modelMapper.map(estadoProductoRepositorio.findByNombreEstadoProducto(EstadoProductoEnum.parseEstado(nombre).toString()), EstadoProductoDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro el estado del producto con ese: " + nombre);
        }

    }

    public List<EstadoProductoDTO> encontrarTodosEstadosProductos(){

        if(estadoProductoRepositorio.findAll().isEmpty()){
            throw new NoEncontradoException("No hay estados de productos en el sistema (VACIO)");
        } else {
            return estadoProductoRepositorio.findAll().stream().map(e -> modelMapper.map(e, EstadoProductoDTO.class)).toList();
        }

    }

    /*
    Estado Alerta
     */
    public EstadoAlertaDTO encontrarAlertaPorId(int id){

        if(estadoAlertaRepositorio.existsByIdEstadoAlerta(id)){
            return modelMapper.map(estadoAlertaRepositorio.findByIdEstadoAlerta(id), EstadoAlertaDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro la alerta con el id " + id);
        }
    }

    public EstadoAlertaDTO encontrarAlertaPorNombre(String nombre){

        if(estadoAlertaRepositorio.existsByNombreEstado(nombre)){
            return modelMapper.map(estadoAlertaRepositorio.findByNombreEstado(nombre.trim().toLowerCase().replace(" ", "_")), EstadoAlertaDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro la alerta con el nombre" + nombre);
        }

    }

    public List<EstadoAlertaDTO> encontrarTodosEstadosAlerta(){

        if(estadoAlertaRepositorio.findAll().isEmpty()){
            throw new NoEncontradoException("No hay estados de alerta en el sistema (VACIO)");
        } else {
            return estadoAlertaRepositorio.findAll().stream().map(e -> modelMapper.map(e, EstadoAlertaDTO.class)).toList();
        }

    }

    /*
    Regla Fraude
     */
    public ReglaFraudeDTO encontrarReglaFraudePorId(int id){
        if(reglaFraudeRepositorio.existsByIdRegla(id)){
            return modelMapper.map(reglaFraudeRepositorio.findByIdRegla(id), ReglaFraudeDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro la regla con el id: " + id);
        }
    }

    public ReglaFraudeDTO encontrarReglaFraudePorNombre(String nombre){
        if(reglaFraudeRepositorio.existsByNombre(nombre.trim().toLowerCase().replace(" ", "_"))){
            return modelMapper.map(reglaFraudeRepositorio.findByNombre(nombre), ReglaFraudeDTO.class);
        } else {
            throw new NoEncontradoException("No se encontro esa relga con este nombre: " +  nombre);
        }
    }

    public List<ReglaFraudeDTO> encontrarTodasReglasFraude() {

        if (reglaFraudeRepositorio.findAll().isEmpty()){
            throw new NoEncontradoException("No hay reglas de fraude en el sistema (VACIO)");
    } else{
            return reglaFraudeRepositorio.findAll().stream().map(e -> modelMapper.map(e, ReglaFraudeDTO.class)).toList();
        }

    }

}
