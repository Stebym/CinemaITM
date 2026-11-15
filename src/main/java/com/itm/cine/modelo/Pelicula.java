package com.itm.cine.modelo;

// Guarda los datos de una pelicula que se puede proyectar en la sala
public class Pelicula {

    private String titulo;
    private String genero;
    private int duracionMinutos;
    private int edadMinima;

    // Crea la pelicula con todos sus datos
    public Pelicula(String titulo, String genero, int duracionMinutos, int edadMinima) {
        this.titulo = titulo;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.edadMinima = edadMinima;
    }

    // Devuelve el titulo de la pelicula
    public String getTitulo() {
        return titulo;
    }

    // Devuelve el genero de la pelicula
    public String getGenero() {
        return genero;
    }

    // Devuelve cuantos minutos dura la pelicula
    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    // Devuelve la edad minima para ver la pelicula
    public int getEdadMinima() {
        return edadMinima;
    }

    // Devuelve un resumen de la pelicula en una sola linea
    public String ficha() {
        return titulo + " - " + genero + " - " + duracionMinutos + " min - +" + edadMinima;
    }
}
