package com.itm.cine;

import com.itm.cine.modelo.ArchivoCine;
import com.itm.cine.modelo.Boleto;
import com.itm.cine.modelo.Cine;
import com.itm.cine.modelo.Funcion;
import com.itm.cine.modelo.Pelicula;
import java.util.Scanner;

// Menu de consola de respaldo, hace lo mismo que la ventana pero escribiendo en la terminal
public class MenuConsola {

    public static final String ARCHIVO = "cine_datos.txt";

    // Muestra el menu y repite hasta que se escoja 0
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cine cine = Principal.crearCineConDatos();
        int opcion;

        do {
            IO.println();
            IO.println("CINEMA ITM");
            IO.println("1. Ver cartelera");
            IO.println("2. Agregar funcion");
            IO.println("3. Quitar funcion");
            IO.println("4. Ver mapa de sillas de una funcion");
            IO.println("5. Vender boleta");
            IO.println("6. Cancelar boleta");
            IO.println("7. Buscar boletas por documento");
            IO.println("8. Sillas libres por zona");
            IO.println("9. Reporte de recaudo y ocupacion");
            IO.println("10. Boletas ordenadas por valor");
            IO.println("11. Guardar en archivo");
            IO.println("12. Cargar de archivo");
            IO.println("0. Salir");
            opcion = leerEntero(sc, "Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    verCartelera(cine);
                    break;
                case 2:
                    agregarFuncion(sc, cine);
                    break;
                case 3:
                    IO.println(cine.quitarFuncion(leerEntero(sc, "Horario de la funcion a quitar (ej. 1430): ")));
                    break;
                case 4:
                    verMapa(sc, cine);
                    break;
                case 5:
                    venderBoleta(sc, cine);
                    break;
                case 6:
                    IO.println(cine.cancelarBoleto(leerEntero(sc, "Numero de la boleta a cancelar: ")));
                    break;
                case 7:
                    buscarPorDocumento(sc, cine);
                    break;
                case 8:
                    sillasLibres(sc, cine);
                    break;
                case 9:
                    reporte(cine);
                    break;
                case 10:
                    listarOrdenadas(cine);
                    break;
                case 11:
                    IO.println(ArchivoCine.guardar(cine, ARCHIVO));
                    break;
                case 12:
                    Cine cargado = ArchivoCine.cargar(ARCHIVO);
                    if (cargado == null) {
                        IO.println("Error: no se encontro el archivo " + ARCHIVO + " o esta danado.");
                    } else {
                        cine = cargado;
                        IO.println("Datos cargados del archivo " + ARCHIVO);
                    }
                    break;
                case 0:
                    IO.println("Gracias por usar Cinema ITM");
                    break;
                default:
                    IO.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    // Lee un numero entero y vuelve a preguntar si escriben letras, asi el programa no se cierra
    public static int leerEntero(Scanner sc, String mensaje) {
        IO.print(mensaje);
        while (!sc.hasNextInt()) {
            sc.nextLine();
            IO.print("Debe escribir un numero entero: ");
        }
        int numero = sc.nextInt();
        sc.nextLine(); // se limpia el enter que queda despues del numero
        return numero;
    }

    // Lee una linea completa de texto
    public static String leerTexto(Scanner sc, String mensaje) {
        IO.print(mensaje);
        return sc.nextLine();
    }

    // Muestra todas las funciones de la cartelera
    public static void verCartelera(Cine cine) {
        if (cine.getCantidadFunciones() == 0) {
            IO.println("No hay funciones en la cartelera.");
            return;
        }
        for (int i = 0; i < cine.getCantidadFunciones(); i++) {
            Funcion f = cine.getFuncion(i);
            IO.println(f.horarioTexto() + " - " + f.getPelicula().ficha());
        }
    }

    // Pide los datos de la pelicula y el horario y se los pasa al cine para que los valide
    public static void agregarFuncion(Scanner sc, Cine cine) {
        String titulo = leerTexto(sc, "Titulo de la pelicula: ");
        String genero = leerTexto(sc, "Genero: ");
        int duracion = leerEntero(sc, "Duracion en minutos: ");
        int edad = leerEntero(sc, "Edad minima: ");
        int horario = leerEntero(sc, "Horario (ej. 1430 para las 2:30 pm): ");
        IO.println(cine.agregarFuncion(new Pelicula(titulo, genero, duracion, edad), horario));
    }

    // Pide el horario y busca la funcion, devuelve null y avisa si no existe
    public static Funcion pedirFuncion(Scanner sc, Cine cine) {
        verCartelera(cine);
        int horario = leerEntero(sc, "Horario de la funcion (ej. 1430): ");
        Funcion f = cine.buscarFuncion(horario);
        if (f == null) {
            IO.println("Error: no hay ninguna funcion a esa hora.");
        }
        return f;
    }

    // Dibuja la sala: [L] libre general, [V] libre VIP, [X] ocupada
    public static void verMapa(Scanner sc, Cine cine) {
        Funcion f = pedirFuncion(sc, cine);
        if (f == null) {
            return;
        }
        IO.println("            PANTALLA");
        IO.println("      1   2   3   4   5   6");
        char[] letras = {'A', 'B', 'C', 'D'};
        for (int i = 0; i < Funcion.FILAS; i++) {
            IO.print(letras[i] + "   ");
            for (int j = 0; j < Funcion.COLUMNAS; j++) {
                if (f.getSilla(i, j).estaOcupada()) {
                    IO.print("[X] ");
                } else if (f.getSilla(i, j).getTipo().equals("VIP")) {
                    IO.print("[V] ");
                } else {
                    IO.print("[L] ");
                }
            }
            IO.println();
        }
        IO.println("[L] libre general  [V] libre VIP  [X] ocupada");
    }

    // Pide la funcion, la silla y los datos del cliente y le pide al cine que venda
    public static void venderBoleta(Scanner sc, Cine cine) {
        verCartelera(cine);
        int horario = leerEntero(sc, "Horario de la funcion (ej. 1430): ");
        String silla = leerTexto(sc, "Codigo de la silla (ej. B3): ");
        String nombre = leerTexto(sc, "Nombre del cliente: ");
        String documento = leerTexto(sc, "Documento del cliente: ");
        IO.println(cine.venderBoleto(horario, silla, nombre, documento));
    }

    // Muestra todas las boletas que compro un cliente
    public static void buscarPorDocumento(Scanner sc, Cine cine) {
        String documento = leerTexto(sc, "Documento del cliente: ");
        Boleto[] encontradas = cine.buscarPorDocumento(documento);
        if (encontradas.length == 0) {
            IO.println("No hay boletas para el documento " + documento);
            return;
        }
        for (int i = 0; i < encontradas.length; i++) {
            IO.println(encontradas[i].ficha());
        }
    }

    // Muestra cuantas sillas libres quedan en cada zona de una funcion
    public static void sillasLibres(Scanner sc, Cine cine) {
        Funcion f = pedirFuncion(sc, cine);
        if (f == null) {
            return;
        }
        IO.println("Libres en zona general: " + f.contarLibres("GENERAL") + " de 12");
        IO.println("Libres en zona VIP: " + f.contarLibres("VIP") + " de 12");
    }

    // Muestra la ocupacion de cada funcion y el dinero recaudado
    public static void reporte(Cine cine) {
        for (int i = 0; i < cine.getCantidadFunciones(); i++) {
            Funcion f = cine.getFuncion(i);
            double porcentaje = Math.round(f.porcentajeOcupacion() * 100) / 100.0;
            IO.println(f.ficha() + ": " + f.contarOcupadas() + " de 24 sillas ocupadas (" + porcentaje + "%)");
        }
        IO.println("Total recaudado (solo boletas activas): $" + (int) cine.totalRecaudado());
    }

    // Muestra todas las boletas de mayor a menor valor
    public static void listarOrdenadas(Cine cine) {
        Boleto[] ordenadas = cine.ordenarPorValor();
        if (ordenadas.length == 0) {
            IO.println("No hay boletas vendidas.");
            return;
        }
        for (int i = 0; i < ordenadas.length; i++) {
            IO.println(ordenadas[i].ficha());
        }
    }
}
