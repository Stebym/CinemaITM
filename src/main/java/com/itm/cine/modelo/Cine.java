package com.itm.cine.modelo;

// Administra la cartelera de funciones y las boletas vendidas del cine
public class Cine {

    public static final double PRECIO_BASE = 12000;

    private String nombre;
    private Funcion[] funciones = new Funcion[5];
    private int cantidadFunciones = 0;
    private Boleto[] boletos = new Boleto[100];
    private int cantidadBoletos = 0;
    private int siguienteNumero = 1001;

    // Crea el cine vacio con su nombre
    public Cine(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve el nombre del cine
    public String getNombre() {
        return nombre;
    }

    // Devuelve cuantas funciones hay en la cartelera
    public int getCantidadFunciones() {
        return cantidadFunciones;
    }

    // Devuelve la funcion que esta en una posicion de la cartelera
    public Funcion getFuncion(int posicion) {
        return funciones[posicion];
    }

    // Devuelve cuantas boletas se han vendido, activas y canceladas
    public int getCantidadBoletos() {
        return cantidadBoletos;
    }

    // Devuelve la boleta que esta en una posicion del arreglo
    public Boleto getBoleto(int posicion) {
        return boletos[posicion];
    }

    // Busca una funcion por su horario, devuelve null si no existe
    public Funcion buscarFuncion(int horario) {
        for (int i = 0; i < cantidadFunciones; i++) {
            if (funciones[i].getHorario() == horario) {
                return funciones[i];
            }
        }
        return null;
    }

    // Revisa que el horario sea una hora valida entre 0000 y 2359
    public boolean horarioValido(int horario) {
        int horas = horario / 100;
        int minutos = horario % 100;
        return horario >= 0 && horas <= 23 && minutos <= 59;
    }

    // Agrega una funcion a la cartelera si cumple todas las reglas
    public String agregarFuncion(Pelicula pelicula, int horario) {
        if (cantidadFunciones == funciones.length) {
            return "Error: la cartelera esta llena, maximo " + funciones.length + " funciones.";
        }
        if (pelicula == null || pelicula.getTitulo() == null || pelicula.getTitulo().trim().isEmpty()) {
            return "Error: la pelicula debe tener titulo.";
        }
        if (!horarioValido(horario)) {
            return "Error: el horario " + horario + " no es una hora valida.";
        }
        if (buscarFuncion(horario) != null) {
            return "Error: ya hay una funcion a esa hora, solo tenemos una sala.";
        }

        Funcion nueva = new Funcion(pelicula, horario, PRECIO_BASE);
        funciones[cantidadFunciones] = nueva;
        cantidadFunciones++;
        return "Funcion agregada: " + nueva.ficha();
    }

    // Cuenta las boletas activas que tiene una funcion
    public int contarBoletosActivos(Funcion funcion) {
        int activos = 0;
        for (int i = 0; i < cantidadBoletos; i++) {
            if (boletos[i].getFuncion() == funcion && boletos[i].estaActivo()) {
                activos++;
            }
        }
        return activos;
    }

    // Quita una funcion de la cartelera corriendo las siguientes una posicion a la izquierda
    public String quitarFuncion(int horario) {
        int posicion = -1;
        for (int i = 0; i < cantidadFunciones; i++) {
            if (funciones[i].getHorario() == horario) {
                posicion = i;
            }
        }
        if (posicion == -1) {
            return "Error: no hay ninguna funcion a esa hora.";
        }
        if (contarBoletosActivos(funciones[posicion]) > 0) {
            return "Error: la funcion tiene boletas activas, primero hay que cancelarlas.";
        }

        String ficha = funciones[posicion].ficha();
        for (int i = posicion; i < cantidadFunciones - 1; i++) {
            funciones[i] = funciones[i + 1];
        }
        funciones[cantidadFunciones - 1] = null;
        cantidadFunciones--;
        return "Funcion quitada: " + ficha;
    }

    // Vende una boleta validando la funcion, la silla y los datos del cliente
    public String venderBoleto(int horario, String codigoSilla, String nombreCliente, String documento) {
        if (cantidadBoletos == boletos.length) {
            return "Error: ya no se pueden registrar mas boletas.";
        }
        Funcion funcion = buscarFuncion(horario);
        if (funcion == null) {
            return "Error: no hay ninguna funcion a esa hora.";
        }
        if (codigoSilla == null) {
            return "Error: falta el codigo de la silla.";
        }
        Silla silla = funcion.buscarSilla(codigoSilla.trim());
        if (silla == null) {
            return "Error: la silla " + codigoSilla + " no existe en la sala.";
        }
        if (silla.estaOcupada()) {
            return "Error: la silla " + silla.getCodigo() + " ya esta ocupada en esa funcion.";
        }
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            return "Error: falta el nombre del cliente.";
        }
        if (documento == null || documento.trim().isEmpty()) {
            return "Error: falta el documento del cliente.";
        }

        silla.ocupar();
        Cliente cliente = new Cliente(nombreCliente.trim(), documento.trim());
        Boleto boleto = new Boleto(siguienteNumero, cliente, funcion, silla, silla.calcularPrecio());
        boletos[cantidadBoletos] = boleto;
        cantidadBoletos++;
        siguienteNumero++;
        return "Boleto vendido: " + boleto.ficha();
    }

    // Busca una boleta por su numero, devuelve null si no existe
    public Boleto buscarBoleto(int numero) {
        for (int i = 0; i < cantidadBoletos; i++) {
            if (boletos[i].getNumero() == numero) {
                return boletos[i];
            }
        }
        return null;
    }

    // Cancela una boleta activa y libera su silla
    public String cancelarBoleto(int numero) {
        Boleto boleto = buscarBoleto(numero);
        if (boleto == null) {
            return "Error: no existe la boleta #" + numero + ".";
        }
        if (!boleto.estaActivo()) {
            return "Error: la boleta #" + numero + " ya estaba cancelada.";
        }

        boleto.cancelar();
        boleto.getSilla().liberar();
        return "Boleta #" + numero + " cancelada, la silla " + boleto.getSilla().getCodigo() + " quedo libre.";
    }

    // Devuelve las boletas de un cliente en un arreglo del tamano exacto
    public Boleto[] buscarPorDocumento(String documento) {
        int encontrados = 0;
        for (int i = 0; i < cantidadBoletos; i++) {
            if (boletos[i].getCliente().getDocumento().equals(documento.trim())) {
                encontrados++;
            }
        }

        Boleto[] resultado = new Boleto[encontrados];
        int k = 0;
        for (int i = 0; i < cantidadBoletos; i++) {
            if (boletos[i].getCliente().getDocumento().equals(documento.trim())) {
                resultado[k] = boletos[i];
                k++;
            }
        }
        return resultado;
    }

    // Suma el valor de todas las boletas activas
    public double totalRecaudado() {
        double total = 0;
        for (int i = 0; i < cantidadBoletos; i++) {
            if (boletos[i].estaActivo()) {
                total = total + boletos[i].getValor();
            }
        }
        return total;
    }

    // Devuelve una copia de las boletas ordenada de mayor a menor valor con burbuja
    public Boleto[] ordenarPorValor() {
        Boleto[] copia = new Boleto[cantidadBoletos];
        for (int i = 0; i < cantidadBoletos; i++) {
            copia[i] = boletos[i];
        }

        for (int i = 0; i < cantidadBoletos - 1; i++) {
            for (int j = 0; j < cantidadBoletos - 1 - i; j++) {
                if (copia[j].getValor() < copia[j + 1].getValor()) {
                    Boleto temp = copia[j];
                    copia[j] = copia[j + 1];
                    copia[j + 1] = temp;
                }
            }
        }
        return copia;
    }
}
