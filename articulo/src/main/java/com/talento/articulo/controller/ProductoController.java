package com.talento.articulo.controller; // Confirma que este paquete sea correcto

import com.talento.articulo.model.Producto;         // Confirma que este paquete sea EXACTO
import com.talento.articulo.repository.ProductoRepository; // Confirma que este paquete sea EXACTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/productos") // La URL base para este controlador
@CrossOrigin(origins = "http://localhost:8081") // Permite peticiones desde EL FRONTEND
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Endpoint para CREAR un nuevo producto.
     * Mapea peticiones POST a la URL: http://localhost:8081/api/productos
     * @param producto El objeto Producto enviado en el cuerpo de la petición (JSON)
     * @return ResponseEntity con el producto creado y estado HTTP 201 (Created)
     */
    @PostMapping // esto es para manejar POST!
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        // Guarda el producto en la base de datos
        Producto savedProducto = productoRepository.save(producto);
        // Devuelve el producto guardado con un estado HTTP 201 (Created)
        return new ResponseEntity<>(savedProducto, HttpStatus.CREATED);
    }

    /**
     * Endpoint para OBTENER todos los productos.
     * Mapea peticiones GET a la URL: http://localhost:8081/api/productos
     * @return Una lista de todos los productos en la base de datos
     */
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    /**
     * Endpoint para OBTENER un producto por su ID.
     * Mapea peticiones GET a la URL: http://localhost:8081/api/productos/{id}
     * @param id El ID del producto a buscar, tomado de la URL
     * @return ResponseEntity con el producto si es encontrado, o estado HTTP 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        return optionalProducto.map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
                               .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * Endpoint para ACTUALIZAR un producto existente por su ID.
     * Mapea peticiones PUT a la URL: http://localhost:8081/api/productos/{id}
     * @param id El ID del producto a actualizar, tomado de la URL
     * @param productoDetails Objeto Producto con los nuevos datos, enviado en el cuerpo de la petición (JSON)
     * @return ResponseEntity con el producto actualizado y estado HTTP 200 (OK), o estado 404 (Not Found)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);

        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setNombre(productoDetails.getNombre());
            producto.setPrecio(productoDetails.getPrecio());
            producto.setDescripcion(productoDetails.getDescripcion());
            producto.setStock(productoDetails.getStock());
            producto.setCategoria(productoDetails.getCategoria());
            producto.setImagenUrl(productoDetails.getImagenUrl());
            
            Producto updatedProducto = productoRepository.save(producto);
            return new ResponseEntity<>(updatedProducto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para ELIMINAR un producto por su ID.
     * Mapea peticiones DELETE a la URL: http://localhost:8081/api/productos/{id}
     * @param id El ID del producto a eliminar, tomado de la URL
     * @return ResponseEntity con estado HTTP 204 (No Content) si la eliminación fue exitosa, o 404 (Not Found)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}