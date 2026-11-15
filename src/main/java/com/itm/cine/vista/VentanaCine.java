package com.itm.cine.vista;

import com.itm.cine.modelo.ArchivoCine;
import com.itm.cine.modelo.Boleto;
import com.itm.cine.modelo.Cine;
import com.itm.cine.modelo.Funcion;
import com.itm.cine.modelo.Pelicula;
import com.itm.cine.modelo.Silla;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

// Ventana principal: muestra la cartelera, el mapa de sillas y los botones de cada opcion.
// La ventana solo pide datos y muestra resultados, las reglas estan en las clases del modelo.
public class VentanaCine extends JFrame {

    public static final String ARCHIVO = "cine_datos.txt";

    private static final Color COLOR_LIBRE = new Color(56, 142, 60);
    private static final Color COLOR_VIP = new Color(200, 150, 20);
    private static final Color COLOR_OCUPADA = new Color(198, 40, 40);

    private Cine cine;
    private JComboBox<String> comboFunciones;
    private JButton[][] botonesSillas;
    private JLabel lblPelicula;
    private JLabel lblLibres;
    private JLabel lblOcupacion;
    private JLabel lblRecaudo;
    private JTextArea areaResultados;

    // Arma la ventana con sus cuatro partes y muestra la primera funcion
    public VentanaCine(Cine cine) {
        this.cine = cine;
        setTitle("Cinema ITM - Venta de boletas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelSala(), BorderLayout.CENTER);
        add(crearPanelOpciones(), BorderLayout.EAST);
        add(crearPanelResultados(), BorderLayout.SOUTH);

        actualizarCartelera();
        pack();
        setLocationRelativeTo(null);
    }

    // Parte de arriba: lista de funciones y botones para agregar o quitar funciones
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel titulo = new JLabel("CINEMA ITM");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        panel.add(titulo);

        panel.add(new JLabel("   Funcion:"));
        comboFunciones = new JComboBox<>();
        comboFunciones.setPreferredSize(new Dimension(260, 28));
        comboFunciones.addActionListener(e -> actualizarSala());
        panel.add(comboFunciones);

        JButton btnAgregar = new JButton("Agregar funcion");
        btnAgregar.addActionListener(e -> agregarFuncion());
        panel.add(btnAgregar);

        JButton btnQuitar = new JButton("Quitar funcion");
        btnQuitar.addActionListener(e -> quitarFuncion());
        panel.add(btnQuitar);

        return panel;
    }

    // Parte del centro: la pantalla y la matriz de 4 x 6 botones que son las sillas
    private JPanel crearPanelSala() {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel pantalla = new JLabel("PANTALLA", SwingConstants.CENTER);
        pantalla.setOpaque(true);
        pantalla.setBackground(Color.DARK_GRAY);
        pantalla.setForeground(Color.WHITE);
        pantalla.setFont(new Font("SansSerif", Font.BOLD, 14));
        pantalla.setPreferredSize(new Dimension(100, 30));
        panel.add(pantalla, BorderLayout.NORTH);

        JPanel grilla = new JPanel(new GridLayout(Funcion.FILAS, Funcion.COLUMNAS, 6, 6));
        botonesSillas = new JButton[Funcion.FILAS][Funcion.COLUMNAS];
        for (int i = 0; i < Funcion.FILAS; i++) {
            for (int j = 0; j < Funcion.COLUMNAS; j++) {
                JButton boton = new JButton();
                boton.setPreferredSize(new Dimension(75, 50));
                boton.setFont(new Font("SansSerif", Font.BOLD, 14));
                boton.setForeground(Color.WHITE);
                boton.setOpaque(true);
                boton.setFocusPainted(false);

                // Se guardan la fila y la columna para saber que silla se toco
                int fila = i;
                int columna = j;
                boton.addActionListener(e -> clicEnSilla(fila, columna));

                botonesSillas[i][j] = boton;
                grilla.add(boton);
            }
        }
        panel.add(grilla, BorderLayout.CENTER);

        lblPelicula = new JLabel(" ", SwingConstants.CENTER);
        panel.add(lblPelicula, BorderLayout.SOUTH);
        return panel;
    }

    // Parte derecha: datos de la funcion, convenciones de colores y botones de consultas
    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        lblLibres = new JLabel();
        lblOcupacion = new JLabel();
        lblRecaudo = new JLabel();
        panel.add(lblLibres);
        panel.add(lblOcupacion);
        panel.add(lblRecaudo);

        panel.add(crearConvencion("Libre general  $12000", COLOR_LIBRE));
        panel.add(crearConvencion("Libre VIP  $15600", COLOR_VIP));
        panel.add(crearConvencion("Ocupada (clic para ver o cancelar)", COLOR_OCUPADA));

        JButton btnBuscar = new JButton("Buscar boletas por documento");
        btnBuscar.addActionListener(e -> buscarPorDocumento());
        panel.add(btnBuscar);

        JButton btnCancelar = new JButton("Cancelar boleta por numero");
        btnCancelar.addActionListener(e -> cancelarPorNumero());
        panel.add(btnCancelar);

        JButton btnReporte = new JButton("Reporte de recaudo y ocupacion");
        btnReporte.addActionListener(e -> mostrarReporte());
        panel.add(btnReporte);

        JButton btnOrdenadas = new JButton("Boletas ordenadas por valor");
        btnOrdenadas.addActionListener(e -> mostrarOrdenadas());
        panel.add(btnOrdenadas);

        JButton btnGuardar = new JButton("Guardar en archivo");
        btnGuardar.addActionListener(e -> guardar());
        panel.add(btnGuardar);

        JButton btnCargar = new JButton("Cargar de archivo");
        btnCargar.addActionListener(e -> cargar());
        panel.add(btnCargar);

        return panel;
    }

    // Crea una etiqueta con un cuadro de color para explicar que significa cada color
    private JLabel crearConvencion(String texto, Color color) {
        JLabel etiqueta = new JLabel("  " + texto);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(color);
        etiqueta.setForeground(Color.WHITE);
        return etiqueta;
    }

    // Parte de abajo: area de texto donde se muestran los listados y los mensajes
    private JScrollPane crearPanelResultados() {
        areaResultados = new JTextArea(9, 60);
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));
        return scroll;
    }

    // Devuelve la funcion escogida en la lista, o null si la cartelera esta vacia
    private Funcion funcionSeleccionada() {
        int posicion = comboFunciones.getSelectedIndex();
        if (posicion == -1) {
            return null;
        }
        return cine.getFuncion(posicion);
    }

    // Vuelve a llenar la lista de funciones con lo que tiene el cine
    private void actualizarCartelera() {
        comboFunciones.removeAllItems();
        for (int i = 0; i < cine.getCantidadFunciones(); i++) {
            comboFunciones.addItem(cine.getFuncion(i).ficha());
        }
        actualizarSala();
    }

    // Pinta cada boton segun su silla y actualiza los datos de la funcion
    private void actualizarSala() {
        Funcion f = funcionSeleccionada();
        for (int i = 0; i < Funcion.FILAS; i++) {
            for (int j = 0; j < Funcion.COLUMNAS; j++) {
                JButton boton = botonesSillas[i][j];
                if (f == null) {
                    boton.setText("-");
                    boton.setBackground(Color.GRAY);
                    boton.setEnabled(false);
                } else {
                    Silla silla = f.getSilla(i, j);
                    boton.setText(silla.getCodigo());
                    boton.setEnabled(true);
                    if (silla.estaOcupada()) {
                        boton.setBackground(COLOR_OCUPADA);
                    } else if (silla.getTipo().equals("VIP")) {
                        boton.setBackground(COLOR_VIP);
                    } else {
                        boton.setBackground(COLOR_LIBRE);
                    }
                }
            }
        }

        if (f == null) {
            lblPelicula.setText("No hay funciones en la cartelera");
            lblLibres.setText("Libres: -");
            lblOcupacion.setText("Ocupacion: -");
        } else {
            lblPelicula.setText(f.horarioTexto() + "  |  " + f.getPelicula().ficha());
            lblLibres.setText("Libres: general " + f.contarLibres("GENERAL") + "  VIP " + f.contarLibres("VIP"));
            double porcentaje = Math.round(f.porcentajeOcupacion() * 100) / 100.0;
            lblOcupacion.setText("Ocupacion: " + f.contarOcupadas() + " de 24 (" + porcentaje + "%)");
        }
        lblRecaudo.setText("Recaudo total: $" + (int) cine.totalRecaudado());
    }

    // Muestra el mensaje que devolvio el modelo, en rojo si es un error
    private void mostrarMensaje(String mensaje) {
        areaResultados.append(mensaje + "\n");
        if (mensaje.startsWith("Error")) {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Cinema ITM", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Convierte un texto en numero entero, devuelve -1 si esta vacio o tiene algo que no es digito
    private int convertirNumero(String texto) {
        if (texto == null || texto.trim().isEmpty() || texto.trim().length() > 9) {
            return -1;
        }
        String limpio = texto.trim();
        for (int i = 0; i < limpio.length(); i++) {
            if (!Character.isDigit(limpio.charAt(i))) {
                return -1;
            }
        }
        return Integer.parseInt(limpio);
    }

    // Si la silla esta libre se vende, si esta ocupada se muestra quien la tiene y se puede cancelar
    private void clicEnSilla(int fila, int columna) {
        Funcion f = funcionSeleccionada();
        if (f == null) {
            return;
        }
        Silla silla = f.getSilla(fila, columna);

        if (!silla.estaOcupada()) {
            venderSilla(f, silla);
            return;
        }

        Boleto boleto = cine.buscarBoletoActivo(f, silla);
        if (boleto == null) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "La silla " + silla.getCodigo() + " esta ocupada.\n" + boleto.ficha()
                + "\n\nDesea cancelar esta boleta?", "Silla ocupada", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            mostrarMensaje(cine.cancelarBoleto(boleto.getNumero()));
            actualizarSala();
        }
    }

    // Pide el nombre y el documento del cliente y le pide al cine que venda la silla
    private void venderSilla(Funcion f, Silla silla) {
        String encabezado = "Silla " + silla.getCodigo() + " (" + silla.getTipo() + ") - $"
                + (int) silla.calcularPrecio() + "\n" + f.ficha() + "\n\n";

        String nombre = JOptionPane.showInputDialog(this, encabezado + "Nombre del cliente:");
        if (nombre == null) {
            return; // se presiono Cancelar
        }
        String documento = JOptionPane.showInputDialog(this, encabezado + "Documento del cliente:");
        if (documento == null) {
            return;
        }

        mostrarMensaje(cine.venderBoleto(f.getHorario(), silla.getCodigo(), nombre, documento));
        actualizarSala();
    }

    // Pide los datos de la nueva funcion; las validaciones las hace el cine
    private void agregarFuncion() {
        String titulo = JOptionPane.showInputDialog(this, "Titulo de la pelicula:");
        if (titulo == null) {
            return;
        }
        String genero = JOptionPane.showInputDialog(this, "Genero:");
        if (genero == null) {
            return;
        }
        int duracion = convertirNumero(JOptionPane.showInputDialog(this, "Duracion en minutos:"));
        int edad = convertirNumero(JOptionPane.showInputDialog(this, "Edad minima (0 a 18):"));
        int horario = convertirNumero(JOptionPane.showInputDialog(this, "Horario (ej. 1430 para las 2:30 pm):"));

        if (duracion == -1 || edad == -1 || horario == -1) {
            mostrarMensaje("Error: la duracion, la edad y el horario deben ser numeros enteros.");
            return;
        }

        String mensaje = cine.agregarFuncion(new Pelicula(titulo, genero, duracion, edad), horario);
        mostrarMensaje(mensaje);
        if (!mensaje.startsWith("Error")) {
            actualizarCartelera();
            comboFunciones.setSelectedIndex(cine.getCantidadFunciones() - 1);
        }
    }

    // Quita la funcion escogida, el cine no deja si tiene boletas activas
    private void quitarFuncion() {
        Funcion f = funcionSeleccionada();
        if (f == null) {
            mostrarMensaje("Error: no hay funciones para quitar.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "Desea quitar la funcion " + f.ficha() + "?",
                "Quitar funcion", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            mostrarMensaje(cine.quitarFuncion(f.getHorario()));
            actualizarCartelera();
        }
    }

    // Muestra todas las boletas de un cliente, sirve para saber quien reservo
    private void buscarPorDocumento() {
        String documento = JOptionPane.showInputDialog(this, "Documento del cliente:");
        if (documento == null) {
            return;
        }
        if (documento.trim().isEmpty()) {
            mostrarMensaje("Error: debe escribir un documento.");
            return;
        }

        Boleto[] encontradas = cine.buscarPorDocumento(documento);
        areaResultados.setText("Boletas del documento " + documento.trim() + ":\n");
        if (encontradas.length == 0) {
            areaResultados.append("No hay boletas para ese documento.\n");
        }
        for (int i = 0; i < encontradas.length; i++) {
            areaResultados.append(encontradas[i].ficha() + "\n");
        }
    }

    // Pide el numero de una boleta y la cancela
    private void cancelarPorNumero() {
        String texto = JOptionPane.showInputDialog(this, "Numero de la boleta a cancelar:");
        if (texto == null) {
            return;
        }
        int numero = convertirNumero(texto);
        if (numero == -1) {
            mostrarMensaje("Error: el numero de la boleta debe ser un numero entero.");
            return;
        }
        mostrarMensaje(cine.cancelarBoleto(numero));
        actualizarSala();
    }

    // Muestra la ocupacion de cada funcion y el total recaudado
    private void mostrarReporte() {
        areaResultados.setText("REPORTE DE RECAUDO Y OCUPACION\n");
        for (int i = 0; i < cine.getCantidadFunciones(); i++) {
            Funcion f = cine.getFuncion(i);
            double porcentaje = Math.round(f.porcentajeOcupacion() * 100) / 100.0;
            areaResultados.append(f.ficha() + ": " + f.contarOcupadas() + " de 24 ocupadas (" + porcentaje
                    + "%) - libres general " + f.contarLibres("GENERAL") + ", VIP " + f.contarLibres("VIP") + "\n");
        }
        areaResultados.append("Total recaudado (solo boletas activas): $" + (int) cine.totalRecaudado() + "\n");
    }

    // Muestra todas las boletas de mayor a menor valor
    private void mostrarOrdenadas() {
        Boleto[] ordenadas = cine.ordenarPorValor();
        areaResultados.setText("BOLETAS ORDENADAS POR VALOR (mayor a menor)\n");
        if (ordenadas.length == 0) {
            areaResultados.append("No hay boletas vendidas.\n");
        }
        for (int i = 0; i < ordenadas.length; i++) {
            areaResultados.append(ordenadas[i].ficha() + "\n");
        }
    }

    // Guarda la cartelera y las boletas en el archivo de texto
    private void guardar() {
        mostrarMensaje(ArchivoCine.guardar(cine, ARCHIVO));
    }

    // Carga la cartelera y las boletas del archivo y reemplaza los datos actuales
    private void cargar() {
        Cine cargado = ArchivoCine.cargar(ARCHIVO);
        if (cargado == null) {
            mostrarMensaje("Error: no se encontro el archivo " + ARCHIVO + " o esta danado.");
            return;
        }
        cine = cargado;
        actualizarCartelera();
        mostrarMensaje("Datos cargados del archivo " + ARCHIVO);
    }
}
