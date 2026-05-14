package co.edu.unbosque.proyectofinalback.repository;

import co.edu.unbosque.proyectofinalback.model.productos.ProductoFinanciero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoFinancieroRepositorio extends JpaRepository<ProductoFinanciero, Integer> {
    boolean existsByNumeroProducto(String numeroProducto);
}
