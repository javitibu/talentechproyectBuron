package com.talento.articulo; // Este es tu paquete base

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.ComponentScan; // No suele ser necesario si todo está dentro del paquete base

@SpringBootApplication // Esta anotación ya incluye @ComponentScan por defecto
// Si tus componentes (repository, service, model, controller, excepciones)
// están DENTRO de com.talento.articulo o sus subpaquetes, esta es suficiente.
// Si por alguna razón los tienes fuera (lo cual no deberías), necesitarías:
// @ComponentScan(basePackages = {"com.talento.articulo", "otra.ruta.si.fuera.necesario"})
public class ArticuloApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticuloApplication.class, args);
    }

}
