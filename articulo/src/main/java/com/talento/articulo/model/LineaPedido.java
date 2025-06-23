package com.talento.articulo.model;

import jakarta.persistence.*; // Importa todas las anotaciones de JPA
import lombok.Getter;       // Lombok, para getters
import lombok.Setter;       // Lombok, para setters
import lombok.NoArgsConstructor; // Lombok, para constructor sin argumentos
import lombok.AllArgsConstructor; // Lombok, para constructor con todos los argumentos

@Entity // clase como una entidad de JPA (se mapeará a una tabla de BD)
@Table(name = "linea_pedido") // Define el nombre de la tabla en la base de datos
@Getter //Lombok para generar automáticamente los getters
@Setter //Lombok para generar automáticamente los setters
@NoArgsConstructor //Lombok para generar un constructor sin argumentos (requerido por JPA)
@AllArgsConstructor //Lombok para generar un constructor con todos los argumentos
public class LineaPedido {

    @Id //campo clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID
    private Long id; // ID de la línea de pedido

    // Relación ManyToOne: Muchas LineasPedido pueden apuntar a un solo Producto
    // @ManyToOne indica la relación. FetchType.EAGER carga el producto inmediatamente.
    // @JoinColumn especifica la columna en la tabla linea_pedido que almacena el ID del producto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false) // 'nullable = false' indica que siempre debe haber un producto
    private Producto producto;

    @Column(nullable = false) // 'nullable = false' indica que la cantidad no puede ser nula
    private int cantidad;

    // Relación ManyToOne: Muchas LineasPedido pueden pertenecer a un solo Pedido
    // Esta es la parte "propietaria" de la relación bidireccional con Pedido.
    @ManyToOne(fetch = FetchType.LAZY) // FetchType.LAZY para cargar el pedido solo cuando sea necesario
    @JoinColumn(name = "pedido_id", nullable = false) // Columna para el ID del pedido
    private Pedido pedido; // El pedido al que pertenece esta línea


    // Constructor con los campos específicos del pedido (sin ID ni Pedido, que se asignan después)
    // para cuando cro una LineaPedido desde el controlador.
    public LineaPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // Método para calcular el subtotal
    public double getSubtotal() {
        if (producto != null) {
            return producto.getPrecio() * cantidad;
        }
        return 0.0;
    }

    // Nota: Los getters y setters los genera Lombok automáticamente gracias a @Getter y @Setter.
    // Si no uso Lombok, los tendria que escribir manualmente
}