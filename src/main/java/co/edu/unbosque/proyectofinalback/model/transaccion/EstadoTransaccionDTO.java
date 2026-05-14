package co.edu.unbosque.proyectofinalback.model.transaccion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoTransaccionDTO {

    private String idEstadoTransaccion;
    private EstadoTransaccion nombreEstadoTransaccion;

}
