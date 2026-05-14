package co.edu.unbosque.proyectofinalback.model.auditoria;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.actores.Empleado;
import co.edu.unbosque.proyectofinalback.model.enums.TipoResponsable;
import co.edu.unbosque.proyectofinalback.model.fraude.AlertaFraude;
import co.edu.unbosque.proyectofinalback.model.productos.ProductoFinanciero;
import co.edu.unbosque.proyectofinalback.model.transaccion.Canal;
import co.edu.unbosque.proyectofinalback.model.transaccion.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoAuditoriaDTO {

    private Integer idEvento;
    private Cliente idCliente;
    private Empleado idEmpleado;
    private Canal idCanal;
    private ProductoFinanciero idProducto;
    private Transaccion idTransaccion;
    private AlertaFraude idAlerta;
    private TipoResponsable tipoResponsable;
    private String accion;
    private LocalDateTime fechaHora;
    private String motivo;

}
