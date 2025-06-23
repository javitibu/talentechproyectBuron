package com.talento.articulo.repository; 

import com.talento.articulo.model.Producto; // Importo clase Producto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Métodos CRUD básicos se obtienen automáticamente.
}