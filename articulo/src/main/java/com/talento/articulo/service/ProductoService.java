package com.talento.articulo.service;

import com.talento.articulo.model.Producto;
import com.talento.articulo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Puedes quitar @Autowired aquí si quieres, Spring lo inyecta automáticamente en el constructor único
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
        // Aquí podrías añadir validaciones adicionales antes de guardar
        return productoRepository.save(producto);
    }

    // Actualizar un producto existente
    public Optional<Producto> actualizarProducto(Long id, Producto productoDetalles) {
        return productoRepository.findById(id).map(productoExistente -> {
            // Actualizar solo los campos que vienen en productoDetalles
            // No comparamos con 'null' para primitivos, ya que nunca lo son.
            // Si un campo se establece en su valor por defecto (0 para int/double, false para boolean)
            // se asume que ese es el valor deseado.

            if (productoDetalles.getNombre() != null) {
                productoExistente.setNombre(productoDetalles.getNombre());
            }
            if (productoDetalles.getDescripcion() != null) {
                productoExistente.setDescripcion(productoDetalles.getDescripcion());
            }
            // Para primitivos, si quieres una actualización condicional,
            // la lógica sería más compleja (ej. usando DTOs o Optional<WrapperType> en DTO).
            // Pero si el JSON envía 0 o un valor, lo tomamos como el nuevo valor.
            // El problema de '!= null' ya no existe porque productoDetalles.getPrecio() devuelve double, no Double.
            // Si quisieras que el precio o stock puedan ser nulos en la request, tendrías que cambiarlos a Double/Integer
            // en un DTO o en la entidad misma (menos común).

            // Lógica para precio y stock (ahora sin el error de 'null' porque son primitivos)
            // Si el valor en productoDetalles es diferente de 0.0 (precio) o 0 (stock), lo actualizamos.
            // Esto asume que 0.0 o 0 no son valores válidos para actualizar, o que si son enviados,
            // es el valor deseado.
            if (productoDetalles.getPrecio() != 0.0) { // Comprueba si el precio en los detalles no es 0.0
                productoExistente.setPrecio(productoDetalles.getPrecio());
            }
            if (productoDetalles.getStock() != 0) { // Comprueba si el stock en los detalles no es 0
                productoExistente.setStock(productoDetalles.getStock());
            }

            // ¡Nuevos campos!
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