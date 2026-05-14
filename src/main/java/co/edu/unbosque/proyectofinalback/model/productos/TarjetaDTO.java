package co.edu.unbosque.proyectofinalback.model.productos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TarjetaDTO extends ProductoFinancieroDTO {

    private String tipoTarjeta;
    private String numeroEnmascarado;
    private String fechaVencimiento;
    private BigDecimal cupo;

}
