package co.edu.unbosque.proyectofinalback.model.productos;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoProductoEnum;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estado_producto")
public class EstadoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_producto")
    private Integer idEstadoProducto;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_estado")
    private EstadoProductoEnum nombreEstadoProducto;

}
