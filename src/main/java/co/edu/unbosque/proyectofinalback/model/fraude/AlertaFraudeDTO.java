package co.edu.unbosque.proyectofinalback.model.fraude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaFraudeDTO {

    private Integer idAlerta;
    private ReglaFraude idRegla;
    private EstadoAlerta idEstadoAlerta;
    private LocalDateTime fechaHora;
    private String severidad;
    private String descripcion;

}
