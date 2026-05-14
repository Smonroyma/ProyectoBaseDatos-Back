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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_documento")
    private String numeroDocumento;

 
    @Column(name = "nombres")
    private String nombre;

   
    @Column(name = "apellidos")
    private String apellido;

    @Column(name = "direccion_principal")
    private String direccionPrincipal;

    private String ciudad;
    private String telefono;
    private String email;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cliente")
    private EstadoActor estadoCliente;

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

}
