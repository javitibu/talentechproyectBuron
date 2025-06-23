package com.talento.articulo.repository; // ¡Paquete actualizado!

import com.talento.articulo.model.LineaPedido; // Importa tu clase LineaPedido
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Long> {
    // Métodos CRUD básicos se obtienen automáticamente.
}