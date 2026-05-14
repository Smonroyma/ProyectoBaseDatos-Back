package co.edu.unbosque.proyectofinalback.model.productos;

import co.edu.unbosque.proyectofinalback.model.enums.Moneda;
import co.edu.unbosque.proyectofinalback.model.enums.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CuentaDTO extends ProductoFinancieroDTO {

    private TipoCuenta tipoCuenta;
    private BigDecimal saldoCuenta;
    private Moneda moneda;

}
