package co.edu.unbosque.proyectofinalback.model.fraude;


import co.edu.unbosque.proyectofinalback.model.transaccion.Transaccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alerta_transaccion")
@IdClass(AlertaTransaccion.class)
public class AlertaTransaccion {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_alerta")
    private AlertaFraude alerta;

    @ManyToOne
    @JoinColumn(name = "id_transaccion")
    private Transaccion transaccion;

    @Column(name = "fecha_asociacion")
    private LocalDateTime fechaAsociacion;

}
