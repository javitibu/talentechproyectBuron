package com.talento.articulo.model;

import jakarta.persistence.*; // Asegúrate de importar todas las anotaciones de JPA
import lombok.Getter;       // Importa Lombok Getter
import lombok.Setter;       // Importa Lombok Setter
import lombok.NoArgsConstructor; // Importa Lombok NoArgsConstructor
import lombok.AllArgsConstructor; // Importa Lombok AllArgsConstructor
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // Declara esta clase como una entidad de JPA
@Table(name = "pedido") // Define el nombre de la tabla en la base de datos
@Getter // Genera automáticamente todos los métodos getter
@Setter // Genera automáticamente todos los métodos setter
@NoArgsConstructor // Genera un constructor sin argumentos (vacío), requerido por JPA
@AllArgsConstructor // Genera un constructor con todos los argumentos (útil para pruebas/inicialización)
public class Pedido {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la auto-generación del ID
    private Long id; // El ID de los pedidos

    @Column(nullable = false) // Esta columna no puede ser nula en la BD
    private LocalDateTime fechaCreacion; // Fecha y hora de creación del pedido

    private String estado; // Estado del pedido (ej: "PENDIENTE", "CONFIRMADO")

    // Relación OneToMany: Un Pedido puede tener muchas LineasPedido
    // mappedBy = "pedido": Indica que el campo 'pedido' en la clase LineaPedido es el dueño de la relación.
    // cascade = CascadeType.ALL: Operaciones como guardar/eliminar en Pedido se propagan a LineaPedido.
    // orphanRemoval = true: Si una LineaPedido es removida de la lista, se elimina de la DB.
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaPedido> lineas = new ArrayList<>(); // Inicializar para evitar NullPointerException

    // Constructor específico para iniciar un pedido con un estado
    // Si usas @NoArgsConstructor y @AllArgsConstructor, Lombok ya te da flexibilidad.
    // Este constructor es útil si quieres un constructor personalizado sin el ID.
    // Ojo: Si usas @AllArgsConstructor, este constructor manual es redundante
    // a menos que quieras lógica específica aquí.
    public Pedido(String estado) {
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now(); // Asigna la fecha de creación al instante
    }

    // Método de utilidad para agregar líneas a un pedido
    // Es crucial para establecer la relación bidireccional correctamente
    public void agregarLinea(LineaPedido linea) {
        this.lineas.add(linea);
        linea.setPedido(this); // Establece la referencia inversa de LineaPedido a este Pedido
    }

    // Método de utilidad para remover líneas (opcional, si necesitas esta lógica)
    public void removerLinea(LineaPedido linea) {
        this.lineas.remove(linea);
        linea.setPedido(null); // Rompe la relación inversa
    }
}