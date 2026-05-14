package co.edu.unbosque.proyectofinalback.model.transaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.enums.TipoTransaccion;
import co.edu.unbosque.proyectofinalback.model.productos.ProductoFinanciero;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaccion")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Integer idTransaccion;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_producto_origen")
    private ProductoFinanciero productoOrigen;

    @ManyToOne
    @JoinColumn(name = "id_producto_destino")
    private ProductoFinanciero productoDestino;

    @ManyToOne
    @JoinColumn(name = "id_estado_transaccion")
    private EstadoTransaccion estadoTransaccion;

    @ManyToOne
    @JoinColumn(name = "id_canal")
    private Canal canal;

    @ManyToOne
    @JoinColumn(name = "id_transaccion_original")
    private Transaccion transaccionOriginal;

    
    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipoTransaccion;

    private BigDecimal monto;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private String motivo;

    
    @Column(name = "es_fallida")
    private boolean fallida;

}
