package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.productos.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoProductoRepositorio extends JpaRepository<EstadoProducto, Integer> {
    boolean existsByNombreEstadoProducto(String nombreEstadoProducto);

    EstadoProducto findByNombreEstadoProducto(String nombreEstadoProducto);

    boolean existsEstadoProductoByIdEstadoProducto(Integer idEstadoProducto);

    List<Object> findEstadoProductoByIdEstadoProducto(Integer idEstadoProducto);
}
