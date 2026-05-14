package co.edu.unbosque.proyectofinalback.util;

import co.edu.unbosque.proyectofinalback.util.exception.FraudeDetectadoException;
import co.edu.unbosque.proyectofinalback.util.exception.NoEncontradoException;
import co.edu.unbosque.proyectofinalback.util.exception.RecursoDuplicadoException;
import co.edu.unbosque.proyectofinalback.util.exception.ReglaNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //400
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaNegocio(ReglaNegocioException ex) {
        return construirRespuesta(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //404
    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(NoEncontradoException ex) {
        return construirRespuesta(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //403
    @ExceptionHandler(FraudeDetectadoException.class)
    public ResponseEntity<Map<String, Object>> manejarFraude(FraudeDetectadoException ex) {
        return construirRespuesta(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErroresGlobales(Exception ex) {
        return construirRespuesta("Error interno del servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //409
    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> manejarDuplicado(RecursoDuplicadoException ex) {
        return construirRespuesta(ex.getMessage(), HttpStatus.CONFLICT);
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(String mensaje, HttpStatus status) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("fecha", LocalDateTime.now());
        respuesta.put("estado", status.value());
        respuesta.put("error", status.getReasonPhrase());
        respuesta.put("mensaje", mensaje);

        return new ResponseEntity<>(respuesta, status);
    }

}
