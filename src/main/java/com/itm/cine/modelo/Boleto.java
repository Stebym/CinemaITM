package com.itm.cine.modelo;

// Representa la venta de una silla de una funcion a un cliente
public class Boleto {

    private int numero;
    private Cliente cliente;
    private Funcion funcion;
    private Silla silla;
    private double valor;
    private boolean activo;

    // Crea el boleto activo con todos sus datos
    public Boleto(int numero, Cliente cliente, Funcion funcion, Silla silla, double valor) {
        this.numero = numero;
        this.cliente = cliente;
        this.funcion = funcion;
        this.silla = silla;
        this.valor = valor;
        this.activo = true;
    }

    // Devuelve el numero del boleto
    public int getNumero() {
        return numero;
    }

    // Devuelve el cliente que compro el boleto
    public Cliente getCliente() {
        return cliente;
    }

    // Devuelve la funcion del boleto
    public Funcion getFuncion() {
        return funcion;
    }

    // Devuelve la silla del boleto
    public Silla getSilla() {
        return silla;
    }

    // Devuelve lo que se pago por el boleto
    public double getValor() {
        return valor;
    }

    // Dice si el boleto sigue activo
    public boolean estaActivo() {
        return activo;
    }

    // Cancela el boleto, devuelve false si ya estaba cancelado
    public boolean cancelar() {
        if (!activo) {
            return false;
        }
        activo = false;
        return true;
    }

    // Devuelve el estado del boleto en texto
    public String estadoTexto() {
        if (activo) {
            return "ACTIVO";
        }
        return "CANCELADO";
    }

    // Devuelve un resumen del boleto en una sola linea
    public String ficha() {
        return "#" + numero + " - " + cliente.ficha() + " - " + funcion.ficha()
                + " - silla " + silla.getCodigo() + " - $" + (int) valor + " - " + estadoTexto();
    }
}
