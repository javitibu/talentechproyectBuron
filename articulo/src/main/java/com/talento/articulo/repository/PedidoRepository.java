package com.talento.articulo.repository;

import com.talento.articulo.model.Pedido; // Importo clase Pedido
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Métodos CRUD básicos se obtienen automáticamente.
}