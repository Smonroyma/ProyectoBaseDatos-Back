package co.edu.unbosque.proyectofinalback.model.fraude;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoProductoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoAlertaDTO {

    private Integer idEstadoAlerta;
    private String nombreEstado;

}
