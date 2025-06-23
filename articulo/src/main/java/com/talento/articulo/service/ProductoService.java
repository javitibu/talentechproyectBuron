package com.talento.articulo.service;

import com.talento.articulo.model.Producto;
import com.talento.articulo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

        public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Listar todos los productos
    public List<Producto> listarTodosLosProductos() {
        return productoRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Crear un nuevo producto
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Actualizar un producto existente
    public Optional<Producto> actualizarProducto(Long id, Producto productoDetalles) {
        return productoRepository.findById(id).map(productoExistente -> {
            // Actualizar solo los campos que vienen en productoDetalles

            if (productoDetalles.getNombre() != null) {
                productoExistente.setNombre(productoDetalles.getNombre());
            }
            if (productoDetalles.getDescripcion() != null) {
                productoExistente.setDescripcion(productoDetalles.getDescripcion());
            }

            if (productoDetalles.getPrecio() != 0.0) { // Comprueba si el precio en los detalles no es 0.0
                productoExistente.setPrecio(productoDetalles.getPrecio());
            }
            if (productoDetalles.getStock() != 0) { // Comprueba si el stock en los detalles no es 0
                productoExistente.setStock(productoDetalles.getStock());
            }

            if (productoDetalles.getCategoria() != null) {
                productoExistente.setCategoria(productoDetalles.getCategoria());
            }
            if (productoDetalles.getImagenUrl() != null) {
                productoExistente.setImagenUrl(productoDetalles.getImagenUrl());
            }

            return productoRepository.save(productoExistente);
        });
    }


    // Eliminar un producto
    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}