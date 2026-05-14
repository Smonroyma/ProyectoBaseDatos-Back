package co.edu.unbosque.proyectofinalback.model.productos;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoFinancieroDTO {

    private Integer idProducto;
    private Cliente cliente;
    private String numeroProducto;
    private LocalDateTime fechaCreacion;
    private EstadoProducto EstadoProducto;

}
