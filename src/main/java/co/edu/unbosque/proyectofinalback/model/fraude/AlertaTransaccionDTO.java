package co.edu.unbosque.proyectofinalback.model.fraude;

import co.edu.unbosque.proyectofinalback.model.transaccion.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertaTransaccionDTO {

    private Integer idAlerta;
    private Transaccion idTransaccion;
    private LocalDateTime fechaAsociacion;

}
