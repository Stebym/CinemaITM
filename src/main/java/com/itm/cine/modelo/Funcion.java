package com.itm.cine.modelo;

// Una funcion es una pelicula a una hora, con su propia matriz de 24 sillas
public class Funcion {

    public static final int FILAS = 4;
    public static final int COLUMNAS = 6;

    private Pelicula pelicula;
    private int horario;
    private Silla[][] sillas;

    // Crea la funcion y arma las 24 sillas: filas A y B general, filas C y D VIP
    public Funcion(Pelicula pelicula, int horario, double precioBase) {
        this.pelicula = pelicula;
        this.horario = horario;
        this.sillas = new Silla[FILAS][COLUMNAS];

        char[] letras = {'A', 'B', 'C', 'D'};
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                String codigo = "" + letras[i] + (j + 1);
                if (i < 2) {
                    sillas[i][j] = new Silla(codigo, i, j, precioBase);
                } else {
                    sillas[i][j] = new SillaVip(codigo, i, j, precioBase);
                }
            }
        }
    }

    // Devuelve la pelicula de la funcion
    public Pelicula getPelicula() {
        return pelicula;
    }

    // Devuelve el horario como numero, por ejemplo 1430
    public int getHorario() {
        return horario;
    }

    // Devuelve el horario con formato de hora, por ejemplo 14:30
    public String horarioTexto() {
        int horas = horario / 100;
        int minutos = horario % 100;
        String texto = horas + ":";
        if (minutos < 10) {
            texto = texto + "0";
        }
        return texto + minutos;
    }

    // Devuelve la silla que esta en una fila y una columna
    public Silla getSilla(int fila, int columna) {
        return sillas[fila][columna];
    }

    // Busca una silla por su codigo recorriendo la matriz, devuelve null si no existe
    public Silla buscarSilla(String codigo) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (sillas[i][j].getCodigo().equalsIgnoreCase(codigo)) {
                    return sillas[i][j];
                }
            }
        }
        return null;
    }

    // Cuenta las sillas libres de un tipo (GENERAL o VIP)
    public int contarLibres(String tipo) {
        int libres = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (!sillas[i][j].estaOcupada() && sillas[i][j].getTipo().equals(tipo)) {
                    libres++;
                }
            }
        }
        return libres;
    }

    // Cuenta todas las sillas ocupadas de la funcion
    public int contarOcupadas() {
        int ocupadas = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (sillas[i][j].estaOcupada()) {
                    ocupadas++;
                }
            }
        }
        return ocupadas;
    }

    // Calcula el porcentaje de ocupacion, se usa 100.0 para que la division no sea entera
    public double porcentajeOcupacion() {
        return contarOcupadas() * 100.0 / (FILAS * COLUMNAS);
    }

    // Devuelve la hora con el titulo de la pelicula
    public String ficha() {
        return horarioTexto() + " - " + pelicula.getTitulo();
    }
}
