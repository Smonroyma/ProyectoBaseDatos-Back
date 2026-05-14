package co.edu.unbosque.proyectofinalback.model.auditoria;

import java.time.LocalDateTime;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.actores.Empleado;
import co.edu.unbosque.proyectofinalback.model.enums.TipoResponsable;
import co.edu.unbosque.proyectofinalback.model.fraude.AlertaFraude;
import co.edu.unbosque.proyectofinalback.model.productos.ProductoFinanciero;
import co.edu.unbosque.proyectofinalback.model.transaccion.Canal;
import co.edu.unbosque.proyectofinalback.model.transaccion.Transaccion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evento_auditoria")
public class EventoAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Integer idEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_canal")
    private Canal canal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private ProductoFinanciero producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_transaccion")
    private Transaccion transaccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alerta")
    private AlertaFraude alerta;

   
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_responsable")
    private TipoResponsable tipoResponsable;

    private String accion;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private String motivo;

}
