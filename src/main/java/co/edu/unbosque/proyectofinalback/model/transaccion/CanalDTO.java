package co.edu.unbosque.proyectofinalback.model.transaccion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanalDTO {

    private Integer idCanal;
    private String nombre;
    private String descripcion;

}
