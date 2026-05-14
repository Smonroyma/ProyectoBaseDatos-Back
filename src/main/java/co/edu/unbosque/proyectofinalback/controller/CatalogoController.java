package co.edu.unbosque.proyectofinalback.controller;

import co.edu.unbosque.proyectofinalback.model.fraude.EstadoAlertaDTO;
import co.edu.unbosque.proyectofinalback.model.fraude.ReglaFraudeDTO;
import co.edu.unbosque.proyectofinalback.model.productos.EstadoProductoDTO;
import co.edu.unbosque.proyectofinalback.model.transaccion.CanalDTO;
import co.edu.unbosque.proyectofinalback.model.transaccion.EstadoTransaccionDTO;
import co.edu.unbosque.proyectofinalback.model.transaccion.TransaccionDTO;
import co.edu.unbosque.proyectofinalback.service.CatalogoService;
import co.edu.unbosque.proyectofinalback.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalogo")
@CrossOrigin(origins = "*")
@Tag(name = "Catologo", description = "Es el controlador de todos los objetos nos modificables que vienen " +
        "de la base de datos, como el canal, estado transaccion, estado producto, estado alerta y regla fraude.")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(summary = "Obtener todos los canales", description = "Retorna una lista de todos los canales de transacción disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de canales obtenida exitosamente")
    @GetMapping("/canales")
    public ResponseEntity<List<CanalDTO>> findAllCanales() {
        List<CanalDTO> canales = catalogoService.encontrarTodosLosCanales();
        return new ResponseEntity<>(canales, HttpStatus.OK);
    }

    @Operation(summary = "Obtener estados de transacción", description = "Retorna una lista de los posibles estados de una transacción.")
    @ApiResponse(responseCode = "200", description = "Lista de estados de transacción obtenida exitosamente")
    @GetMapping("/estadosTransaccion")
    public ResponseEntity<List<EstadoTransaccionDTO>> findAllEstadosTransaccion() {
        List<EstadoTransaccionDTO> estadosTransaccion = catalogoService.encontrarTodosEstadosTrans();
        return new ResponseEntity<>(estadosTransaccion, HttpStatus.OK);
    }

    @Operation(summary = "Obtener estados de productos", description = "Retorna una lista de los posibles estados de los productos.")
    @ApiResponse(responseCode = "200", description = "Lista de estados de productos obtenida exitosamente")
    @GetMapping("/estadosProductos")
    public ResponseEntity<List<EstadoProductoDTO>> findAllEstadosProductos(){
        List<EstadoProductoDTO> estadosProductos = catalogoService.encontrarTodosEstadosProductos();
        return new ResponseEntity<>(estadosProductos, HttpStatus.OK);
    }

    @Operation(summary = "Obtener estados de alerta", description = "Retorna una lista de los estados que puede tener una alerta de fraude.")
    @ApiResponse(responseCode = "200", description = "Lista de estados de alerta obtenida exitosamente")
    @GetMapping("/estadosAlerta")
    public ResponseEntity<List<EstadoAlertaDTO>> findAllEstadosAlerta(){
        List<EstadoAlertaDTO> estadosAlerta = catalogoService.encontrarTodosEstadosAlerta();
        return new ResponseEntity<>(estadosAlerta, HttpStatus.OK);
    }

    @Operation(summary = "Obtener reglas de fraude", description = "Retorna la lista de reglas configuradas para la detección de fraude.")
    @ApiResponse(responseCode = "200", description = "Lista de reglas de fraude obtenida exitosamente")
    @GetMapping("/reglasFraude")
    public ResponseEntity<List<ReglaFraudeDTO>> findAllReglaFraude(){
        List<ReglaFraudeDTO> reglasFraude = catalogoService.encontrarTodasReglasFraude();
        return new ResponseEntity<>(reglasFraude, HttpStatus.OK);
    }
}
