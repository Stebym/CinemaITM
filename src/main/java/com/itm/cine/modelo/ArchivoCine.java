package com.itm.cine.modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

// Guarda y carga la cartelera y las boletas del cine en un archivo de texto
public class ArchivoCine {

    // Escribe una linea por cada funcion (F) y por cada boleta (B), con los datos separados por ;
    public static String guardar(Cine cine, String ruta) {
        try {
            PrintWriter escritor = new PrintWriter(ruta);

            for (int i = 0; i < cine.getCantidadFunciones(); i++) {
                Funcion f = cine.getFuncion(i);
                Pelicula p = f.getPelicula();
                escritor.println("F;" + f.getHorario() + ";" + p.getTitulo() + ";" + p.getGenero()
                        + ";" + p.getDuracionMinutos() + ";" + p.getEdadMinima());
            }

            for (int i = 0; i < cine.getCantidadBoletos(); i++) {
                Boleto b = cine.getBoleto(i);
                escritor.println("B;" + b.getNumero() + ";" + b.getCliente().getNombre() + ";"
                        + b.getCliente().getDocumento() + ";" + b.getFuncion().getHorario() + ";"
                        + b.getSilla().getCodigo() + ";" + b.estaActivo());
            }

            escritor.close();
            return "Datos guardados en el archivo " + ruta;
        } catch (FileNotFoundException e) {
            return "Error: no se pudo crear el archivo " + ruta;
        }
    }

    // Lee el archivo linea por linea y arma un cine nuevo, devuelve null si no existe o esta danado
    public static Cine cargar(String ruta) {
        try {
            Cine cine = new Cine("Cinema ITM");
            Scanner lector = new Scanner(new File(ruta));

            while (lector.hasNextLine()) {
                String[] datos = lector.nextLine().split(";");

                if (datos[0].equals("F")) {
                    int horario = Integer.parseInt(datos[1]);
                    int duracion = Integer.parseInt(datos[4]);
                    int edad = Integer.parseInt(datos[5]);
                    cine.agregarFuncion(new Pelicula(datos[2], datos[3], duracion, edad), horario);
                }

                if (datos[0].equals("B")) {
                    int numero = Integer.parseInt(datos[1]);
                    int horario = Integer.parseInt(datos[4]);
                    boolean activo = Boolean.parseBoolean(datos[6]);
                    cine.cargarBoleto(numero, datos[2], datos[3], horario, datos[5], activo);
                }
            }

            lector.close();
            return cine;
        } catch (Exception e) {
            // Entra aqui si el archivo no existe o si una linea tiene datos que no son validos
            return null;
        }
    }
}
