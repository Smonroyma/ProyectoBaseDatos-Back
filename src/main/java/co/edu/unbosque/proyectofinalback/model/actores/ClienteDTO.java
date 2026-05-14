package co.edu.unbosque.proyectofinalback.model.actores;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoActor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private Integer idCliente;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private String apellido;
    private String direccionPrincipal;
    private String ciudad;
    private String telefono;
    private String email;
    private EstadoActor estadoCliente;
    private LocalDateTime fechaAlta;

}
