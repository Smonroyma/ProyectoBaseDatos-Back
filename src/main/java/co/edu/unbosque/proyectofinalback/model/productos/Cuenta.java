package co.edu.unbosque.proyectofinalback.model.productos;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.enums.Moneda;
import co.edu.unbosque.proyectofinalback.model.enums.TipoCuenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cuenta")
public class Cuenta extends ProductoFinanciero {

    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta")
    private TipoCuenta tipoCuenta;

   
    @Column(name = "saldo_actual")
    private BigDecimal saldoCuenta;

    
    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @Builder
    public Cuenta(Integer idProducto, Cliente cliente, String numeroProducto, LocalDateTime fechaCreacion,
                  EstadoProducto estadoProducto, TipoCuenta tipoCuenta, BigDecimal saldoCuenta, Moneda moneda) {
        super(idProducto, cliente, numeroProducto, fechaCreacion, estadoProducto);
        this.tipoCuenta = tipoCuenta;
        this.saldoCuenta = saldoCuenta;
        this.moneda = moneda;
    }

}
