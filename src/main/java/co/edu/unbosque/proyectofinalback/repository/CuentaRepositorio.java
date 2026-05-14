package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.actores.Cliente;
import co.edu.unbosque.proyectofinalback.model.productos.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepositorio extends JpaRepository<Cuenta, Integer> {
    Cuenta findByCliente(Cliente cliente);

    List<Cuenta> findAllByCliente(Cliente cliente);

    boolean existsCuentaByIdProducto(Integer idProducto);

    Cuenta findCuentaByIdProducto(Integer idProducto);
}
