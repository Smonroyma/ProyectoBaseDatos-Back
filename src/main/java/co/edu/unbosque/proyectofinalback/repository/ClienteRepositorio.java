package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {
    boolean existsByNumeroDocumento(String numeroDocumento);

    void deleteByNumeroDocumento(String numeroDocumento);

    Cliente findByNumeroDocumento(String numeroDocumento);

    boolean existsClienteByIdCliente(Integer idCliente);

    Cliente findClienteByIdCliente(Integer idCliente);

    boolean existsClienteByNumeroDocumento(String numeroDocumento);
}
