package co.edu.unbosque.proyectofinalback.model.fraude;

import java.time.LocalDateTime;

import co.edu.unbosque.proyectofinalback.model.enums.Severidad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alerta_fraude")
public class AlertaFraude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Integer alerta;

    @ManyToOne
    @JoinColumn(name = "id_regla")
    private ReglaFraude regla;

    @ManyToOne
    @JoinColumn(name = "id_estado_alerta")
    private EstadoAlerta estadoAlerta;

    @Column(name = "fecha_alerta")
    private LocalDateTime fechaHora;

    @Column(name = "tipo_alerta")
    private String tipoAlerta;


    @Enumerated(EnumType.STRING)
    private Severidad severidad;

    private String descripcion;

}
