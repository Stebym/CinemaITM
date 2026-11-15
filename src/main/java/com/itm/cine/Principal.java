package com.itm.cine;

import com.itm.cine.modelo.Cine;
import com.itm.cine.modelo.Pelicula;
import com.itm.cine.vista.VentanaCine;

// Punto de entrada del programa: crea el cine con datos de prueba y abre la ventana
public class Principal {

    // Crea el cine con tres funciones y algunas boletas ya vendidas para probar de una vez
    public static Cine crearCineConDatos() {
        Cine cine = new Cine("Cinema ITM");

        cine.agregarFuncion(new Pelicula("Intensamente 2", "Animada", 96, 0), 1400);
        cine.agregarFuncion(new Pelicula("Dune Parte Dos", "Ciencia ficcion", 166, 12), 1730);
        cine.agregarFuncion(new Pelicula("Oppenheimer", "Drama", 180, 15), 2030);

        cine.venderBoleto(1400, "A1", "Carlos Gomez", "102030");
        cine.venderBoleto(1400, "A2", "Laura Restrepo", "405060");
        cine.venderBoleto(1400, "C3", "Santiago Palacio", "708090");
        cine.venderBoleto(1730, "D6", "Daniela Perez", "112233");
        cine.venderBoleto(1730, "B4", "Carlos Gomez", "102030");
        cine.venderBoleto(2030, "C1", "Andres Rios", "554433");

        // Se cancela una boleta para que el reporte muestre que las canceladas no suman
        cine.cancelarBoleto(1006);

        return cine;
    }

    // Arranca el programa con la ventana grafica
    public static void main(String[] args) {
        Cine cine = crearCineConDatos();
        VentanaCine ventana = new VentanaCine(cine);
        ventana.setVisible(true);
    }
}
