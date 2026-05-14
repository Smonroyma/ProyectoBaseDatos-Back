package co.edu.unbosque.proyectofinalback.controller;

import co.edu.unbosque.proyectofinalback.model.actores.ClienteDTO;
import co.edu.unbosque.proyectofinalback.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Cliente", description = "API para la gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Crear un nuevo cliente", description = "Registra un cliente en la base de datos")
    @PostMapping("/crear")
    public ResponseEntity<ClienteDTO> save(@RequestBody ClienteDTO clienteDTO){
        ClienteDTO cliente = clienteService.crear(clienteDTO);
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar cliente por ID", description = "Elimina de forma lógica o física un cliente usando su ID primario")
    @PostMapping("/eliminarPorId/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id){
        boolean eliminado = clienteService.eliminarPorId(id);
        return new ResponseEntity<>(eliminado, HttpStatus.GONE);
    }

    @Operation(summary = "Eliminar cliente por documento", description = "Elimina un cliente usando su número de documento")
    @PostMapping("/eliminarPorDocumento/{documento}")
    public ResponseEntity<Boolean> deleteByDocument(@PathVariable String documento){
        boolean eliminado = clienteService.eliminarPorDocumento(documento);
        return new ResponseEntity<>(eliminado, HttpStatus.GONE);
    }

    @Operation(summary = "Actualizar cliente por ID", description = "Actualiza los datos de un cliente existente buscando por su ID")
    @PutMapping("/actualizarPorId")
    public ResponseEntity<ClienteDTO> updateById(@RequestBody ClienteDTO clienteDTO){
        ClienteDTO cliente = clienteService.actualizarPorId(clienteDTO);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar cliente por documento", description = "Actualiza los datos de un cliente buscando por su número de documento")
    @PostMapping("/actualizarPorDocumento")
    public ResponseEntity<ClienteDTO> updateByDocument(@RequestBody ClienteDTO clienteDTO){
        ClienteDTO cliente = clienteService.actualizarPorDocumento(clienteDTO);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar estado del cliente", description = "Cambia el estado (Activo/Inactivo) de un cliente")
    @PostMapping("/actualizarEstado")
    public ResponseEntity<ClienteDTO> updateStatus(@RequestParam Integer id, @RequestParam String estado){
        ClienteDTO cliente = clienteService.actualizarEstado(id, estado);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna la información de un cliente específico por su ID")
    @GetMapping("/encontrarClienteId")
    public ResponseEntity<ClienteDTO> findById(@RequestParam Integer id){
        ClienteDTO cliente = clienteService.encontrarClienteId(id);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @Operation(summary = "Buscar cliente por documento", description = "Retorna la información de un cliente específico por su número de documento")
    @GetMapping("/econtrarClienteDocumento")
    public ResponseEntity<ClienteDTO> findByDocument(@RequestParam String documento){
        ClienteDTO cliente = clienteService.encontrarClienteDocumento(documento);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos los clientes", description = "Retorna una lista con todos los clientes registrados")
    @GetMapping("/todosLosClientes")
    public ResponseEntity<List<ClienteDTO>> findAll(){
        List<ClienteDTO> clientes = clienteService.todosLosClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }


}
