package com.talento.articulo.model;

import jakarta.persistence.*;
import lombok.Getter; // ¡ASEGÚRATE DE ESTO!
import lombok.Setter; // ¡ASEGÚRATE DE ESTO!
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "producto")
@Getter // Genera automáticamente los getters
@Setter // Genera automáticamente los setters
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private double precio;
    private String descripcion;
    @Column(nullable = false)
    private int stock;
    private String categoria;
    private String imagenUrl;
}