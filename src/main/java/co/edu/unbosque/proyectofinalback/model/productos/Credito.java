package co.edu.unbosque.proyectofinalback.model.productos;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.enums.TipoCredito;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "credito")
public class Credito extends ProductoFinanciero {

   
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_credito")
    private TipoCredito tipoCredito;

   
    @Column(name = "monto_aprobado")
    private BigDecimal montoAprobado;

    @Column(name = "saldo_pendiente")
    private BigDecimal saldoPendiente;

    @Column(name = "tasa_interes")
    private double tasaInteres;

    @Column(name = "fecha_desembolso")
    private LocalDateTime fechaDesembolso;

    @Builder
    public Credito(Integer idProducto, Cliente cliente, String numeroProducto, LocalDateTime fechaCreacion,
                   EstadoProducto estadoProducto, TipoCredito tipoCredito, BigDecimal montoAprobado,
                   BigDecimal saldoPendiente, double tasaInteres, LocalDateTime fechaDesembolso) {
        super(idProducto, cliente, numeroProducto, fechaCreacion, estadoProducto);
        this.tipoCredito = tipoCredito;
        this.montoAprobado = montoAprobado;
        this.saldoPendiente = saldoPendiente;
        this.tasaInteres = tasaInteres;
        this.fechaDesembolso = fechaDesembolso;
    }
}
