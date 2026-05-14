package co.edu.unbosque.proyectofinalback.model.fraude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReglaFraudeDTO {

    private Integer idRegla;
    private String nombre;
    private String tipoRegla;
    private String umbral;
    private int ventanaMinutos;
    private int maxItentos;
    private String activa;

}
