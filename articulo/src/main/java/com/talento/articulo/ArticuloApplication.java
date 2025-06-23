package com.talento.articulo; // Este es paquete base

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication // Esta anotación ya incluye @ComponentScan por defecto

public class ArticuloApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticuloApplication.class, args);
    }

}
