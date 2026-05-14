package co.edu.unbosque.proyectofinalback.model.productos;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.enums.TipoTarjeta;
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
@Table(name = "tarjeta")
public class Tarjeta extends ProductoFinanciero {


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tarjeta")
    private TipoTarjeta tipoTarjeta;

    @Column(name = "numero_enmascarado")
    private String numeroEnmascarado;

    @Column(name = "fecha_vencimiento")
    private String fechaVencimiento;

    private BigDecimal cupo;

    @Builder
    public Tarjeta(Integer idProducto, Cliente cliente, String numeroProducto, LocalDateTime fechaCreacion,
                   EstadoProducto estadoProducto, TipoTarjeta tipoTarjeta, String numeroEnmascarado,
                   String fechaVencimiento, BigDecimal cupo) {
        super(idProducto, cliente, numeroProducto, fechaCreacion, estadoProducto);
        this.tipoTarjeta = tipoTarjeta;
        this.numeroEnmascarado = numeroEnmascarado;
        this.fechaVencimiento = fechaVencimiento;
        this.cupo = cupo;
    }
}
