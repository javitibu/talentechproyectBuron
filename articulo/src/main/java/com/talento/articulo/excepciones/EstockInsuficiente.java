package com.talento.articulo.excepciones;;
// Si no hay productoo
public class EstockInsuficiente extends Exception {
    public EstockInsuficiente(String mensaje) {
        super(mensaje);
    }
}
