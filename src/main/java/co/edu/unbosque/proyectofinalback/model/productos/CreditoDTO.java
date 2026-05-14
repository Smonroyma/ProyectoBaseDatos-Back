package co.edu.unbosque.proyectofinalback.model.productos;

import co.edu.unbosque.proyectofinalback.model.enums.TipoCredito;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditoDTO extends ProductoFinancieroDTO {

    private TipoCredito tipoCredito;
    private BigDecimal montoAprobado;
    private BigDecimal saldoPendiente;
    private double tasaInteres;
    private LocalDateTime fechaDesembolso;

}
