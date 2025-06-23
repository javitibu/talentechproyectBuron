package com.talento.articulo.controller; // ¡Paquete actualizado!

import com.talento.articulo.excepciones.EstockInsuficiente; // ¡Importamos tu excepción!
import com.talento.articulo.model.Pedido;
import com.talento.articulo.model.LineaPedido;
import com.talento.articulo.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Para devolver mensajes de error personalizados
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos") // Define la URL base para los pedidos
public class PedidoController {

    private final PedidoService pedidoService;

  
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // --- Endpoints para Gestión de Pedidos ---

    // GET /api/pedidos
    // Corresponde a 'listarPedidos()' de tu GestionSistema
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarTodosLosPedidos();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    // GET /api/pedidos/{id}
    // Obtener detalles de un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
        return pedido.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST /api/pedidos
    // Corresponde a 'crearPedido()' de tu GestionSistema
    // El cliente (frontend) enviará una lista de LineaPedido, donde cada LineaPedido
    // solo necesita el 'id' del Producto y la 'cantidad' deseada.
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody List<LineaPedido> lineasDelRequest) {
        try {
            Pedido nuevoPedido = pedidoService.crearPedido(lineasDelRequest);
            return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED); // Retorna 201 Created
        } catch (EstockInsuficiente e) {
            // Si la excepción EstockInsuficiente se lanza, devolvemos un 400 Bad Request
            // con un mapa que contiene el mensaje de error para que el cliente lo entienda.
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            // Para otras validaciones (ej. pedido vacío, producto no encontrado, cantidad <= 0)
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Para cualquier otro error inesperado en el servidor
            return new ResponseEntity<>(Map.of("error", "Error interno del servidor al crear pedido: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT /api/pedidos/{id}/estado
    // Permite actualizar solo el estado de un pedido. Se espera un JSON como {"estado": "CONFIRMADO"}
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstadoPedido(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado"); // Obtenemos el valor de la clave "estado"
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si no se proporciona estado, 400 Bad Request
        }
        Optional<Pedido> pedido = pedidoService.actualizarEstadoPedido(id, nuevoEstado);
        return pedido.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE /api/pedidos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        if (pedidoService.eliminarPedido(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content (éxito sin cuerpo de respuesta)
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}