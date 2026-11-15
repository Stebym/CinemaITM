package com.itm.cine.modelo;

// Guarda los datos de la persona que compra una boleta
public class Cliente {

    private String nombre;
    private String documento;

    // Crea el cliente con su nombre y su documento
    public Cliente(String nombre, String documento) {
        this.nombre = nombre;
        this.documento = documento;
    }

    // Devuelve el nombre del cliente
    public String getNombre() {
        return nombre;
    }

    // Devuelve el documento del cliente
    public String getDocumento() {
        return documento;
    }

    // Devuelve el nombre con el documento
    public String ficha() {
        return nombre + " (" + documento + ")";
    }
}
