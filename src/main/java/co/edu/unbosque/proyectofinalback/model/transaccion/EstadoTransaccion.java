package co.edu.unbosque.proyectofinalback.model.transaccion;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoTransaccionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "estado_transaccion")
public class EstadoTransaccion {

    @Id
    @Column(name = "id_estado_transaccion")
    private Integer idEstadoTransaccion;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_estado")
    private EstadoTransaccionEnum nombreEstadoTransaccion;

}
