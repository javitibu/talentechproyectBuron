package com.talento.articulo.repository; // ¡Paquete actualizado!

import com.talento.articulo.model.Producto; // Importa tu clase Producto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Métodos CRUD básicos se obtienen automáticamente.
}