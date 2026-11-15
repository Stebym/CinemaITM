package com.itm.cine.modelo;

// Silla de zona VIP, hereda todo de Silla y cambia el precio y el tipo
public class SillaVip extends Silla {

    // Crea la silla VIP usando el constructor de Silla
    public SillaVip(String codigo, int fila, int columna, double precioBase) {
        super(codigo, fila, columna, precioBase);
    }

    // La silla VIP es de tipo VIP
    @Override
    public String getTipo() {
        return "VIP";
    }

    // La silla VIP paga el precio base mas un recargo del 30%
    @Override
    public double calcularPrecio() {
        return getPrecioBase() + getPrecioBase() * 30 / 100;
    }
}
