package com.talento.articulo.repository; // ¡Paquete actualizado!

import com.talento.articulo.model.Pedido; // Importa tu clase Pedido
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Métodos CRUD básicos se obtienen automáticamente.
}