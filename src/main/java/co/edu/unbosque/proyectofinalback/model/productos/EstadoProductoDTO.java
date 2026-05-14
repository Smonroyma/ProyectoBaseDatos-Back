package co.edu.unbosque.proyectofinalback.model.productos;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoProductoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoProductoDTO {

    private Integer idEstadoProducto;
    private EstadoProductoEnum nombreEstadoProducto;

}
