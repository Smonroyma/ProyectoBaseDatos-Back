package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.fraude.EstadoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoAlertaRepositorio extends JpaRepository<EstadoAlerta, Integer> {

    boolean existsByNombreEstado(String nombreEstado);

    EstadoAlerta findByNombreEstado(String nombreEstado);

    boolean existsByIdEstadoAlerta(Integer idEstadoAlerta);

    EstadoAlerta findByIdEstadoAlerta(Integer idEstadoAlerta);
}