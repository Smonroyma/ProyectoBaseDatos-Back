package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.fraude.ReglaFraude;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReglaFraudeRepositorio extends JpaRepository<ReglaFraude, Integer> {
    boolean existsByNombre(String nombre);

    ReglaFraude findByNombre(String nombre);

    List<ReglaFraude> findByActiva(String activa);

    boolean existsByIdRegla(Integer idRegla);

    List<Object> findByIdRegla(Integer idRegla);

    Collection<? extends ReglaFraude> findAllByActiva(String activa);
}
