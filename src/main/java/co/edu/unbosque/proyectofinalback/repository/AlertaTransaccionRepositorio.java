package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.fraude.AlertaTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaTransaccionRepositorio extends JpaRepository<AlertaTransaccion, Integer> {
}
