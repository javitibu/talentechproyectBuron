package com.talento.articulo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "producto")
@Getter // Genero automáticamente los getters
@Setter // Genero automáticamente los setters
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