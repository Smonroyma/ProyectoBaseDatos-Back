package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.enums.EstadoTransaccionEnum;
import co.edu.unbosque.proyectofinalback.model.transaccion.EstadoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoTransaccionRepositorio extends JpaRepository<EstadoTransaccion, Integer> {

    boolean existsEstadoTransaccionByIdEstadoTransaccion(Integer idEstadoTransaccion);

    Object findByIdEstadoTransaccion(Integer idEstadoTransaccion);

    Object findByNombreEstadoTransaccion(EstadoTransaccionEnum nombreEstadoTransaccion);

    boolean existsByNombreEstadoTransaccion(EstadoTransaccionEnum nombreEstadoTransaccion);
}
