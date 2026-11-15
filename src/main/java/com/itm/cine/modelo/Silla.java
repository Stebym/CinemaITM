package com.itm.cine.modelo;

// Representa una silla de zona general de la sala
public class Silla {

    private String codigo;
    private int fila;
    private int columna;
    private boolean ocupada;
    private double precioBase;

    // Crea la silla libre con su posicion y su precio base
    public Silla(String codigo, int fila, int columna, double precioBase) {
        this.codigo = codigo;
        this.fila = fila;
        this.columna = columna;
        this.precioBase = precioBase;
        this.ocupada = false;
    }

    // Devuelve el codigo de la silla, por ejemplo B3
    public String getCodigo() {
        return codigo;
    }

    // Devuelve la fila de la silla en la matriz (0 a 3)
    public int getFila() {
        return fila;
    }

    // Devuelve la columna de la silla en la matriz (0 a 5)
    public int getColumna() {
        return columna;
    }

    // Devuelve el precio base sin recargos
    public double getPrecioBase() {
        return precioBase;
    }

    // Dice si la silla esta ocupada
    public boolean estaOcupada() {
        return ocupada;
    }

    // Ocupa la silla, devuelve false si ya estaba ocupada
    public boolean ocupar() {
        if (ocupada) {
            return false;
        }
        ocupada = true;
        return true;
    }

    // Libera la silla, devuelve false si ya estaba libre
    public boolean liberar() {
        if (!ocupada) {
            return false;
        }
        ocupada = false;
        return true;
    }

    // Devuelve el tipo de silla
    public String getTipo() {
        return "GENERAL";
    }

    // Calcula lo que cuesta la silla, la general paga el precio base
    public double calcularPrecio() {
        return precioBase;
    }
}
