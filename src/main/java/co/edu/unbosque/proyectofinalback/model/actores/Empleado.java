package co.edu.unbosque.proyectofinalback.model.actores;

import java.time.LocalDateTime;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoActor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "nombres")
    private String nombre;

    @Column(name = "apellidos")
    private String apellido;

    private String cargo;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado_empleado")
    private EstadoActor estadoEmpleado;

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

}
