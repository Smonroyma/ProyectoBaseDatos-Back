package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.productos.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarjetaRepositorio extends JpaRepository<Tarjeta, Integer> {
    boolean existsByNumeroEnmascarado(String numeroEnmascarado);

    void deleteByNumeroEnmascarado(String numeroEnmascarado);

    Tarjeta findTarjetaByIdProducto(String idProducto);

    Tarjeta findByNumeroEnmascarado(String numeroEnmascarado);

    boolean existsByIdProducto(Integer idProducto);

    Tarjeta findTarjetaByIdProducto(Integer idProducto);
}
