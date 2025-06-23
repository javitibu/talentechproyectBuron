package com.talento.articulo.service;

import com.talento.articulo.model.Pedido;
import com.talento.articulo.model.Producto;
import com.talento.articulo.model.LineaPedido;
import com.talento.articulo.repository.PedidoRepository;
import com.talento.articulo.repository.ProductoRepository;
import com.talento.articulo.excepciones.EstockInsuficiente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    // Puedes quitar @Autowired aquí, como hemos visto antes, Spring lo inyecta automáticamente
    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    // --- Métodos de Gestión de Pedidos ---

    public List<Pedido> listarTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido crearPedido(List<LineaPedido> lineasSolicitadas) throws EstockInsuficiente {
        // Validación inicial del pedido
        if (lineasSolicitadas == null || lineasSolicitadas.isEmpty()) {
            throw new IllegalArgumentException("El pedido no puede estar vacío.");
        }

        Pedido nuevoPedido = new Pedido("PENDIENTE"); // Estado inicial del pedido
        nuevoPedido.setFechaCreacion(LocalDateTime.now()); // Establecer la fecha y hora actual

        // Iterar sobre las líneas de pedido "solicitadas" (del frontend)
        for (LineaPedido lineaSolicitada : lineasSolicitadas) {
            // Validar que el producto y la cantidad sean válidos en la solicitud
            if (lineaSolicitada.getProducto() == null || lineaSolicitada.getProducto().getId() == null) {
                throw new IllegalArgumentException("Cada línea de pedido debe especificar un ID de producto.");
            }
            if (lineaSolicitada.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad para el producto ID " + lineaSolicitada.getProducto().getId() + " debe ser positiva.");
            }

            // 1. Obtener el Producto real de la base de datos
            // Esto asegura que trabajamos con el estado actual del producto (precio, stock)
            Optional<Producto> productoOpt = productoRepository.findById(lineaSolicitada.getProducto().getId());

            if (productoOpt.isEmpty()) {
                throw new IllegalArgumentException("Producto con ID " + lineaSolicitada.getProducto().getId() + " no encontrado.");
            }

            Producto productoReal = productoOpt.get(); // El producto existe y lo obtenemos

            // 2. Verificar el stock
            if (lineaSolicitada.getCantidad() > productoReal.getStock()) {
                throw new EstockInsuficiente("Stock insuficiente para el producto: " + productoReal.getNombre() + ". Disponible: " + productoReal.getStock() + ", Solicitado: " + lineaSolicitada.getCantidad());
            }

            // 3. Disminuir stock y guardar el producto actualizado
            productoReal.setStock(productoReal.getStock() - lineaSolicitada.getCantidad());
            productoRepository.save(productoReal);

            // 4. Crear la LineaPedido final (entidad JPA)
            // Usamos el constructor sin argumentos gracias a @NoArgsConstructor en LineaPedido
            LineaPedido lineaFinal = new LineaPedido();
            lineaFinal.setProducto(productoReal); // Asocia el producto real (de la DB) a la línea
            lineaFinal.setCantidad(lineaSolicitada.getCantidad());

            // 5. Agregar la línea al pedido principal
            // Este método en Pedido.java se encarga de establecer la relación bidireccional (linea.setPedido(this))
            nuevoPedido.agregarLinea(lineaFinal);
        }

        // 6. Guardar el pedido principal (esto guardará todas las líneas gracias a CascadeType.ALL en Pedido)
        return pedidoRepository.save(nuevoPedido);
    }

    public Optional<Pedido> actualizarEstadoPedido(Long id, String nuevoEstado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        });
    }

    @Transactional
    public boolean eliminarPedido(Long id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            // Lógica para devolver el stock al eliminar/cancelar un pedido
            for (LineaPedido linea : pedido.getLineas()) {
                // Hay que cargar el producto de nuevo para asegurar que su estado sea el más reciente
                // y evitar problemas de LazyInitializationException si el producto no se cargó con el pedido.
                productoRepository.findById(linea.getProducto().getId()).ifPresent(producto -> {
                    producto.setStock(producto.getStock() + linea.getCantidad());
                    productoRepository.save(producto);
                });
            }
            pedidoRepository.delete(pedido);
            return true;
        }
        return false;
    }
}