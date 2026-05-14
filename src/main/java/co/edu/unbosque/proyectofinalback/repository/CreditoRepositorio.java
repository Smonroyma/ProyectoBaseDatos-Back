package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.productos.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditoRepositorio extends JpaRepository<Credito, Integer> {
    Credito findCreditosByIdProducto(String idProducto);

    void deleteCreditoByIdProducto(String idProducto);

    List<Credito> findAllByCliente(Cliente cliente);

    boolean existsCreditoByIdProducto(Integer idProducto);

    Credito findCreditosByIdProducto(Integer idProducto);
}
