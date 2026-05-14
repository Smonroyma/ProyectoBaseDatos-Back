package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.transaccion.Canal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanalRepositorio extends JpaRepository<Canal, Integer> {

    boolean existsByNombre(String nombre);
    Canal findByNombre(String nombre);


    List<Object> findCanalByIdCanal(Integer idCanal);

    boolean existsCanalByIdCanal(Integer idCanal);
}
