package co.edu.unbosque.proyectofinalback.model.fraude;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "regla_fraude")
public class ReglaFraude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regla")
    private Integer idRegla;

    private String nombre;

    @Column(name = "tipo_regla")
    private String tipoRegla;
    private BigDecimal umbral;

    @Column(name = "ventana_minutos")
    private Integer ventanaMinutos;

    @Column(name = "max_intentos")
    private Integer maxItentos;
    private String activa;
}
