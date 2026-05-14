package co.edu.unbosque.proyectofinalback.model.transaccion;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.productos.ProductoFinanciero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionDTO {

    private Integer idTransaccion;
    private Cliente cliente;
    private ProductoFinanciero productoOrigen;
    private ProductoFinanciero productoDestino;
    private EstadoTransaccion estadoTransaccion;
    private Canal canal;
    private Transaccion transaccionOriginal;
    private BigDecimal monto;
    private LocalDateTime fechaHora;
    private String motivo;
    private boolean fallida;

}
