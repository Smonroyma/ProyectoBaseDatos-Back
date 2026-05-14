package co.edu.unbosque.proyectofinalback.controller;

import co.edu.unbosque.proyectofinalback.model.actores.Empleado;
import co.edu.unbosque.proyectofinalback.model.actores.EmpleadoDTO;
import co.edu.unbosque.proyectofinalback.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleados")
@CrossOrigin(origins = "*")
@Tag(name = "Empleado", description = "API para la gestión de empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Operation(summary = "Crear un nuevo empleado", description = "Registra un nuevo empleado en la base de datos")
    @ApiResponse(responseCode = "21", description = "Empleado creado exitosamente")
    @PostMapping("/crear")
    public ResponseEntity<EmpleadoDTO> crear(@RequestBody EmpleadoDTO empleadoDTO){
        EmpleadoDTO empleado = empleadoService.crear(empleadoDTO);
        return new ResponseEntity<>(empleado, HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un empleado", description = "Elimina un empleado existente mediante su ID")
    @ApiResponse(responseCode = "410", description = "Empleado eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    @PostMapping("/eliminar")
    public ResponseEntity<Boolean> eliminar(@RequestParam int idEmpleado){
        boolean eliminado = empleadoService.eliminar(idEmpleado);
        return new ResponseEntity<>(eliminado, HttpStatus.GONE);
    }

    @Operation(summary = "Actualizar un empleado", description = "Actualiza la información de un empleado existente")
    @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente")
    @PostMapping("/actualizar")
    public ResponseEntity<EmpleadoDTO> actualizar(@RequestBody EmpleadoDTO empleadoDTO){
        EmpleadoDTO empleado = empleadoService.actualizar(empleadoDTO);
        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar estado del empleado", description = "Cambia el estado (Activo/Inactivo) de un empleado")
    @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente")
    @PostMapping("/actualizarEstado")
    public ResponseEntity<EmpleadoDTO> actualizarEstado(@RequestParam int idEmpleado, @RequestParam String estado) {
        EmpleadoDTO empleado = empleadoService.actualizarEstado(idEmpleado, estado);
        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @Operation(summary = "Buscar empleado por ID", description = "Obtiene los detalles de un empleado específico")
    @ApiResponse(responseCode = "200", description = "Empleado encontrado")
    @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    @GetMapping("/encontrarEmpleadoId")
    public ResponseEntity<EmpleadoDTO> encontrarEmpleadoId(@RequestParam int idEmpleado){
        EmpleadoDTO empleado = empleadoService
                .encontrarEmpleadoId(idEmpleado);
        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos los empleados", description = "Retorna una lista de todos los empleados registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/encontrarTodos")
    public ResponseEntity<List<EmpleadoDTO>> encontrarTodos(){
        List<EmpleadoDTO> empleados = empleadoService.encontrarTodos();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

}
