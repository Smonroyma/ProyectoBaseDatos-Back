package co.edu.unbosque.proyectofinalback.model.actores;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoActor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {

    private Integer idEmpleado;
    private String nombre;
    private String apellido;
    private String cargo;
    private EstadoActor estadoEmpleado;
    private LocalDateTime fechaAlta;

}
