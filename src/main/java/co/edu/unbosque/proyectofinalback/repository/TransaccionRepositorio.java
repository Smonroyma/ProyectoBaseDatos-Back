package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.productos.ProductoFinanciero;
import co.edu.unbosque.proyectofinalback.model.transaccion.EstadoTransaccion;
import co.edu.unbosque.proyectofinalback.model.transaccion.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface TransaccionRepositorio extends JpaRepository<Transaccion, Integer> {
    long countByProductoOrigenAndFechaHoraAfter(ProductoFinanciero productoOrigen, LocalDateTime fechaHoraAfter);

    List<Transaccion> findByIdTransaccion(Integer idTransaccion);

    long countByProductoOrigenAndFechaHoraAfterAndEstadoTransaccion(ProductoFinanciero productoOrigen, LocalDateTime fechaHoraAfter, EstadoTransaccion estadoTransaccion);

    Collection<Object> findByCliente(Cliente cliente);
}
