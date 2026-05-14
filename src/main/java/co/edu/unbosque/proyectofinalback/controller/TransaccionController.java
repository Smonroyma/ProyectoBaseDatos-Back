package co.edu.unbosque.proyectofinalback.controller;

import co.edu.unbosque.proyectofinalback.model.transaccion.TransaccionDTO;
import co.edu.unbosque.proyectofinalback.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transacciones")
@CrossOrigin(origins = "*")
@Tag(name = "Transacción", description = "API para la gestión de transacciones bancarias")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @Operation(summary = "Procesar una nueva transacción", description = "Realiza una transferencia de dinero entre dos cuentas")
    @ApiResponse(responseCode = "200", description = "Transacción procesada exitosamente")
    @PostMapping("/procesarTransaccion")
    public ResponseEntity<TransaccionDTO> procesarTransaccion(@RequestParam int idCuentaOrigen, @RequestParam int idCuentaDestino, @RequestParam BigDecimal monto, @RequestParam String motivo, @RequestParam String tipoTransaccion, @RequestParam int idCanal){
        TransaccionDTO transaccion = transaccionService.procesarTransaccion(idCuentaOrigen, idCuentaDestino, monto, motivo, tipoTransaccion, idCanal);
        return new ResponseEntity<>(transaccion, HttpStatus.OK);
    }

    @Operation(summary = "Obtener transacciones por cliente", description = "Retorna una lista de todas las transacciones asociadas a un ID de cliente")
    @ApiResponse(responseCode = "200", description = "Lista de transacciones obtenida correctamente")
    @GetMapping("/transaccionPorCliente")
    public ResponseEntity<List<TransaccionDTO>> transaccionPorCliente(@RequestParam int idCliente){
        List<TransaccionDTO> transaccion = transaccionService.transaccionPorCliente(idCliente);
        return new ResponseEntity<>(transaccion, HttpStatus.OK);
    }

    @Operation(summary = "Listar todas las transacciones", description = "Retorna el historial completo de transacciones del sistema")
    @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente")
    @GetMapping("/todasLasTransacciones")
    public ResponseEntity<List<TransaccionDTO>> todasLasTransacciones(){
        List<TransaccionDTO> transaccion = transaccionService.todasLasTransacciones();
        return new ResponseEntity<>(transaccion, HttpStatus.OK);
    }


}
