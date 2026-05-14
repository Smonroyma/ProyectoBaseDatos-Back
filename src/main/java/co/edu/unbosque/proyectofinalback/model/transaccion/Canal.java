package co.edu.unbosque.proyectofinalback.model.transaccion;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "canal")
public class Canal {

    @Id
    @Column(name = "id_canal")
    private Integer idCanal;

    private String nombre;
    private String descripcion;

}
