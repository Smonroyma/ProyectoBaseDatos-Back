package co.edu.unbosque.proyectofinalback.controller;

import co.edu.unbosque.proyectofinalback.model.productos.CreditoDTO;
import co.edu.unbosque.proyectofinalback.model.productos.CuentaDTO;
import co.edu.unbosque.proyectofinalback.model.productos.ProductoFinanciero;
import co.edu.unbosque.proyectofinalback.model.productos.TarjetaDTO;
import co.edu.unbosque.proyectofinalback.service.ProductoFinancieroService;
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
@RequestMapping("/productos-financieros")
@CrossOrigin(origins = "*")
@Tag(name = "Producto Financiero", description = "Controlador para la gestión de Créditos, Tarjetas y Cuentas")
public class ProductoFinancieroController {

    @Autowired
    private ProductoFinancieroService productoFinancieroService;

    //Credito

    @Operation(summary = "Crear un nuevo crédito", description = "Registra un nuevo crédito para un cliente específico")
    @ApiResponse(responseCode = "201", description = "Crédito creado exitosamente")
    @PostMapping("/credito/crear")
    public ResponseEntity<CreditoDTO> saveCredito(@RequestBody CreditoDTO creditoDTO, @RequestParam int idCliente, @RequestParam String responsable) {
        CreditoDTO credito = productoFinancieroService.crearCredito(creditoDTO, idCliente);
        return new ResponseEntity<>(credito, HttpStatus.CREATED);
    }

    @Operation(summary = "Pagar crédito", description = "Realiza un abono o pago total a un crédito existente")
    @ApiResponse(responseCode = "200", description = "Pago procesado correctamente")
    @PostMapping("/credito/pagarCredito")
    public ResponseEntity<CreditoDTO> pagarCredito(@RequestParam int idCredito, @RequestParam BigDecimal montoPago) {
        CreditoDTO credito = productoFinancieroService.pagarCredito(idCredito, montoPago);
        return new ResponseEntity<>(credito, HttpStatus.OK);
    }

    @Operation(summary = "Ajustar monto de crédito", description = "Modifica el monto total de un crédito")
    @ApiResponse(responseCode = "200", description = "Monto ajustado exitosamente")
    @PostMapping("/credito/ajusteMontoCredito")
    public ResponseEntity<CreditoDTO> ajusteMontoCredito(@RequestParam int idCredito, @RequestParam BigDecimal nuevoMonto, @RequestParam String responsable) {
        CreditoDTO credito = productoFinancieroService.ajusteMontoCredito(idCredito, nuevoMonto, responsable);
        return new ResponseEntity<>(credito, HttpStatus.OK);
    }

    @Operation(summary = "Cambiar estado del crédito", description = "Actualiza el estado (activo, inactivo, etc.) de un crédito")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @PostMapping("/credito/cambiarEstadoProducto")
    public ResponseEntity<CreditoDTO> cambiarEstadoProducto(@RequestParam int idCredito, @RequestParam int estadoProducto, @RequestParam String responsable) {
        CreditoDTO credito = productoFinancieroService.cambiarEstadoProducto(idCredito, estadoProducto
                , responsable);
        return new ResponseEntity<>(credito, HttpStatus.OK);
    }

    @Operation(summary = "Obtener crédito por ID", description = "Retorna la información detallada de un crédito")
    @ApiResponse(responseCode = "200", description = "Crédito encontrado")
    @GetMapping("/credito/obtenerCredito")
    public ResponseEntity<CreditoDTO> obtenerCredito(@RequestParam int idCredito) {
        CreditoDTO credito = productoFinancieroService.obtenerCredito(idCredito);
        return new ResponseEntity<>(credito, HttpStatus.OK);
    }

    @Operation(summary = "Listar créditos por cliente", description = "Obtiene todos los créditos asociados a un ID de cliente")
    @ApiResponse(responseCode = "200", description = "Lista de créditos obtenida")
    @GetMapping("/credito/obtenerTodosLosCreditoPorCliente")
    public ResponseEntity<List<CreditoDTO>> obtenerTodosLosCreditosPorCliente(@RequestParam int idCliente) {
        List<CreditoDTO> credito = productoFinancieroService.obtenerTodosCreditosPorCliente(idCliente);
        return new ResponseEntity<>(credito, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos los créditos", description = "Obtiene la lista global de todos los créditos en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista completa obtenida")
    @GetMapping("/credito/obtenerTodosLosCredito")
    public ResponseEntity<List<CreditoDTO>> findAllCreditos() {
        List<CreditoDTO> credito = productoFinancieroService.obtenerTodosLosCreditos();
        return new ResponseEntity<>(credito, HttpStatus.OK);
    }

    //Tarjeta
    @Operation(summary = "Crear tarjeta", description = "Asigna una nueva tarjeta de crédito a un cliente")
    @ApiResponse(responseCode = "201", description = "Tarjeta creada exitosamente")
    @PostMapping("/tarjeta/crearTarjeta")
    public ResponseEntity<TarjetaDTO> saveTarjeta(@RequestParam int idCliente, @RequestParam String tipoTarjeta, @RequestParam BigDecimal cupo) throws Exception {
        TarjetaDTO tarjeta = productoFinancieroService.creartarjeta(idCliente, tipoTarjeta, cupo);
        return new ResponseEntity<>(tarjeta, HttpStatus.CREATED);
    }

    @Operation(summary = "Desactivar tarjeta por ID", description = "Marca una tarjeta como inactiva usando su ID")
    @ApiResponse(responseCode = "410", description = "Tarjeta desactivada (Gone)")
    @PostMapping("/tarjeta/desactivarId")
    public ResponseEntity<Boolean> desactivarTarjetaId(@RequestParam int idTarjeta, @RequestParam String responsable) {
        boolean eliminado = productoFinancieroService.desactivarTarjetaID(idTarjeta, responsable);
        return new ResponseEntity<>(eliminado, HttpStatus.GONE);
    }

    @Operation(summary = "Desactivar tarjeta por número", description = "Marca una tarjeta como inactiva usando el número físico")
    @ApiResponse(responseCode = "410", description = "Tarjeta desactivada (Gone)")
    @PostMapping("/tarjeta/desactivarNumero")
    public ResponseEntity<Boolean> desactivarTarjetaId(@RequestParam String numeroTarjeta, @RequestParam String responsable) throws Exception {
        boolean eliminado = productoFinancieroService.desactivarTarjetaNumero(numeroTarjeta, responsable);
        return new ResponseEntity<>(eliminado, HttpStatus.GONE);
    }

    @Operation(summary = "Actualizar estado de tarjeta", description = "Cambia el estado operativo de una tarjeta")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @PostMapping("/tarjeta/actualizacionEstado")
    public ResponseEntity<TarjetaDTO> actualizarEstado(@RequestParam int idEstado, @RequestParam int idTarjeta, @RequestParam String responsable) {
        TarjetaDTO tarjeta = productoFinancieroService.actualizacionEstado(idEstado, idTarjeta, responsable);
        return new ResponseEntity<>(tarjeta, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar cupo de tarjeta", description = "Modifica el límite de crédito de la tarjeta")
    @ApiResponse(responseCode = "200", description = "Cupo actualizado")
    @PostMapping("/tarjeta/actualizacionCupo")
    public ResponseEntity<TarjetaDTO> actualizarCupo(@RequestParam int idTarjeta, @RequestParam BigDecimal cupo, @RequestParam int idEmpleado) {
        TarjetaDTO tarjeta = productoFinancieroService.actualizacionCupo(idTarjeta, cupo, idEmpleado);
        return new ResponseEntity<>(tarjeta, HttpStatus.OK);
    }

    @Operation(summary = "Renovar fecha de vencimiento", description = "Extiende la validez de la tarjeta")
    @ApiResponse(responseCode = "200", description = "Fecha renovada")
    @PostMapping("/tarjeta/renovarFechaVencimiento")
    public ResponseEntity<TarjetaDTO> renovarFechaVencimiento(@RequestParam int idTarjeta, @RequestParam int idEmpleado) {
        TarjetaDTO tarjeta = productoFinancieroService.renovarFechaVencimiento(idTarjeta, idEmpleado);
        return new ResponseEntity<>(tarjeta, HttpStatus.OK);
    }

    @Operation(summary = "Buscar tarjeta por ID", description = "Obtiene los datos de una tarjeta específica")
    @ApiResponse(responseCode = "200", description = "Tarjeta encontrada")
    @GetMapping("/tarjeta/encontrartarjetaPorId")
    public ResponseEntity<TarjetaDTO> encontrarTarjetaPorId(@RequestParam int idTarjeta) throws Exception {
        TarjetaDTO tarjeta = productoFinancieroService.encontrarTarjetaPorId(idTarjeta);
        return new ResponseEntity<>(tarjeta, HttpStatus.OK);
    }

    @Operation(summary = "Listar todas las tarjetas", description = "Obtiene el listado de todas las tarjetas registradas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida")
    @GetMapping("/tarjeta/listarTarjetas")
    public ResponseEntity<List<TarjetaDTO>> findAllTarjetas() throws Exception {
        List<TarjetaDTO> tarjetaDTO = productoFinancieroService.listarTarjetas();
        return new ResponseEntity<>(tarjetaDTO, HttpStatus.OK);

    }

    //Cuenta
    @Operation(summary = "Crear cuenta", description = "Abre una nueva cuenta (ahorros/corriente) para un cliente")
    @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente")
    @PostMapping("/cuenta/crearCuenta")
    public ResponseEntity<CuentaDTO> saveCuenta(@RequestParam int idCliente, @RequestParam String moneda, @RequestParam String tipoCuenta) {
        CuentaDTO cuenta = productoFinancieroService.crearCuenta(idCliente, tipoCuenta, moneda);
        return new ResponseEntity<>(cuenta, HttpStatus.CREATED);
    }

    @Operation(summary = "Desactivar cuenta", description = "Inactiva una cuenta bancaria")
    @ApiResponse(responseCode = "200", description = "Cuenta desactivada")
    @PostMapping("/cuenta/desactivarCuenta")
    public ResponseEntity<Boolean> desactivarCuenta(@RequestParam int idProducto) {
        Boolean cuenta = productoFinancieroService.desactivarCuenta(idProducto);
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar estado de cuenta", description = "Cambia el estado de una cuenta específica")
    @ApiResponse(responseCode = "200", description = "Estado de cuenta actualizado")
    @PostMapping("/cuenta/actualizarEstado")
    public ResponseEntity<CuentaDTO> actualizarEstadoCuenta(@RequestParam int idProducto, @RequestParam int idEstado, @RequestParam String responsable) {
        CuentaDTO cuenta = productoFinancieroService.actualizarEstado(idProducto, idEstado, responsable);
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }

    @Operation(summary = "Hacer un abono a la cuenta", description = "Se suma una plata a la cuenta del usuario")
    @ApiResponse(responseCode = "200", description = "Abono realizado exitosamente")
    @PostMapping("/cuenta/abonoCuenta")
    public ResponseEntity<CuentaDTO> abonoCuenta(@RequestParam int idCuenta, @RequestParam BigDecimal montoAbono) {
        CuentaDTO cuenta = productoFinancieroService.abono(idCuenta, montoAbono);
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }

    @Operation(summary = "Obtener cuenta por ID", description = "Retorna la información de una cuenta")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada")
    @GetMapping("/cuenta/obtenerCuenta")
    public ResponseEntity<CuentaDTO> obtenerCuenta(@RequestParam int idCuenta) {
        CuentaDTO cuenta = productoFinancieroService.obtenerCuenta(idCuenta);
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }

    @Operation(summary = "Listar cuentas por cliente", description = "Obtiene todas las cuentas de un cliente")
    @ApiResponse(responseCode = "200", description = "Lista de cuentas obtenida")
    @GetMapping("/cuenta/obtenerTodasCuentasPorCliente")
    public ResponseEntity<List<CuentaDTO>> obtenerCuentaPorCliente(@RequestParam int idCliente) {
        List<CuentaDTO> cuenta = productoFinancieroService.obtenerTodasCuentasPorCliente(idCliente);
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }

    @Operation(summary = "Listar todas las cuentas", description = "Obtiene todas las cuentas registradas en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista completa de cuentas obtenida")
    @GetMapping("/cuenta/todasLasCuentas")
    public ResponseEntity<List<CuentaDTO>> todasLasCuentas() {
        List<CuentaDTO> cuenta = productoFinancieroService.todasLasCuentas();
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos los productos financieros", description = "Obtener todas los productos financieros del sistema")
    @ApiResponse(responseCode = "200", description = "Lista compleata de productos financieros")
    @GetMapping("/todoslosproductos")
    public ResponseEntity<List<ProductoFinanciero>> todosLosProductos() {
        List<ProductoFinanciero> productos = productoFinancieroService.todosLosProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

}