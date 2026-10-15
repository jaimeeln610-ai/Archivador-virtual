package com.example;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.*;

public class GestorUI {
    private GestorDatos gd = new GestorDatos();
    private JFrame frame = new JFrame();
    private JButton botonTarea;
    private JButton botonTrabajo;
    private JPanel panelArriba;
    private JPanel panelCentro;
    private JLabel labelSaludo;
    private JPanel panelFooter;
    private JLabel labelFooter;
    private JPanel panelIzq;
    private JButton agregarTarea;
    private JButton modificarTarea;
    private JButton finalizarTarea;
    private JButton nuevoTrabajo;
    private JButton cargarTrabajo;
    private JTable tablaTareas;
    private DefaultTableModel dtm;
    private JTextPane textPane = new JTextPane();
    private int contadorLibretas = 1;
    private JPanel panelAgregar = new JPanel();
    private JPanel panelFinalizar = new JPanel();
    private JPanel panelModificar = new JPanel();
    private Boolean negritaActiva = false;
    private Boolean cursivaActiva = false;

    public GestorUI() {
        File carpeta = new File("C:\\Archivador");

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public void setVisible() {

        // Pantalla inicio

        botonTarea = new JButton("Tareas");
        botonTarea.setFont(new Font("Arial", Font.BOLD, 100));
        botonTrabajo = new JButton("Trabajo");
        botonTrabajo.setFont(new Font("Arial", Font.BOLD, 100));
        panelCentro = new JPanel();
        panelCentro.setLayout(new GridLayout(1, 2, 10, 20));
        panelArriba = new JPanel();
        panelArriba.setLayout(new FlowLayout());
        labelSaludo = new JLabel("ARCHIVADOR VIRTUAL");
        labelSaludo.setFont(new Font("Arial", Font.BOLD, 100));
        panelFooter = new JPanel();
        labelFooter = new JLabel("Seleccione un boton");
        labelFooter.setFont(new Font("Arial", Font.BOLD, 20));
        panelFooter.add(labelFooter);
        panelArriba.add(labelSaludo);
        panelCentro.add(botonTarea);
        panelCentro.add(botonTrabajo);

        // Interfaz de tareas
        botonTarea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.getContentPane().removeAll();
                frame.setLayout(new BorderLayout());
                panelArriba.removeAll();
                panelArriba.revalidate();
                panelArriba.repaint();
                panelCentro.removeAll();
                panelCentro.revalidate();
                panelCentro.repaint();
                panelFooter.removeAll();
                panelFooter.revalidate();
                panelFooter.repaint();
                JPanel panelIzqArriba = new JPanel();
                panelIzq = new JPanel();
                panelIzq.setLayout(new BorderLayout());
                agregarTarea = new JButton("Agregar tarea");
                modificarTarea = new JButton("Modificar tarea");
                finalizarTarea = new JButton("Finalizar tarea");
                JButton botonFiltrar = new JButton("Filtrar");
                botonTarea.setFont(new Font("Arial", Font.BOLD, 20));
                botonTrabajo.setFont(new Font("Arial", Font.BOLD, 20));
                String[] columnas = { "Tarea", "Estado", "Prioridad" };

                dtm = new DefaultTableModel(columnas, 0);
                tablaTareas = new JTable(dtm);

                ArrayList<String[]> tareas = gd.rellenarTablaTarea();
                for (String[] tarea : tareas) {
                    dtm.addRow(tarea);
                }
                tablaTareas.setModel(dtm);
                JScrollPane scroll = new JScrollPane(tablaTareas);

                // Agregacion de tareas
                agregarTarea.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        panelIzq.removeAll();
                        panelIzq.add(panelIzqArriba, BorderLayout.NORTH);
                        panelAgregar.removeAll();
                        panelAgregar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
                        JPanel panelNombre = new JPanel();
                        panelNombre.setLayout(new FlowLayout());
                        JPanel panelPrioridad = new JPanel();
                        panelPrioridad.setLayout(new FlowLayout());
                        JLabel labelNombre = new JLabel("Tarea:");
                        JTextField nombretf = new JTextField(23);
                        JLabel prioridadLbl = new JLabel("Prioridad:");
                        String[] prioridades = { "Normal", "Importante", "Muy Importante" };
                        JComboBox<String> comboPrioridad = new JComboBox<>(prioridades);
                        comboPrioridad.setPreferredSize(new Dimension(250, 30));
                        JButton botonAgregar = new JButton("Agregar");
                        

                        panelNombre.add(labelNombre);
                        panelNombre.add(nombretf);
                        panelPrioridad.add(prioridadLbl);
                        panelPrioridad.add(comboPrioridad);

                        botonAgregar.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
                                panelIzq.revalidate();
                                panelIzq.repaint();
                                String nombre = nombretf.getText();
                                if (!nombre.isEmpty()) {
                                    String prioridad = (String) comboPrioridad.getSelectedItem();
                                    if (agregarTarea(nombre, prioridad)) {
                                        dtm.setRowCount(0);
                                        ArrayList<String[]> tareas = gd.rellenarTablaTarea();
                                        for (String[] tarea : tareas) {
                                            dtm.addRow(tarea);
                                        }
                                        nombretf.setText("");
                                    } else {
                                        JOptionPane.showMessageDialog(
                                                panelAgregar,
                                                "Error al agregar la tarea (Recuerda que no se pueden repetir tareas).",
                                                "Error",
                                                JOptionPane.ERROR_MESSAGE);

                                        nombretf.requestFocus();
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(
                                            panelAgregar,
                                            "El campo 'tarea' no puede estar vacío.",
                                            "Campo Obligatorio",
                                            JOptionPane.ERROR_MESSAGE);

                                    nombretf.requestFocus();
                                }

                            }
                        });

                        panelAgregar.setPreferredSize(new Dimension(20, 300));
                        panelAgregar.add(panelNombre);
                        panelAgregar.add(panelPrioridad);
                        panelAgregar.add(botonAgregar);
                        panelIzq.add(panelAgregar, BorderLayout.CENTER);

                        panelIzq.revalidate();
                        panelIzq.repaint();
                    }
                });
                // Modificacion de tareas
                modificarTarea.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        panelIzq.removeAll();
                        panelIzq.add(panelIzqArriba, BorderLayout.NORTH);
                        panelModificar.removeAll();
                        panelModificar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
                        JPanel panelCombo = new JPanel();
                        panelCombo.setLayout(new FlowLayout());
                        JLabel tarea = new JLabel("Tarea:");
                        ArrayList<String> nombresList = nombres();
                        JComboBox<String> tareas = new JComboBox<>(nombresList.toArray(new String[0]));
                        tareas.setPreferredSize(new Dimension(250, 30));

                        JLabel newName = new JLabel("Nuevo nombre ");
                        JTextField newNameTF = new JTextField(20);
                        JPanel panelNewName = new JPanel();
                        JButton botonModificar = new JButton("Cambiar");
                        panelNewName.setLayout(new FlowLayout());

                        JLabel newPrioridad = new JLabel("Nueva prioridad ");
                        String[] prioridadesList = { "Normal", "Importante", "Muy Importante" };
                        JComboBox<String> prioridades = new JComboBox<>(prioridadesList);
                        prioridades.setPreferredSize(new Dimension(220, 20));
                        JPanel panelNewPrioridad = new JPanel();
                        panelNewName.setLayout(new FlowLayout());

                        botonModificar.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
                                String nuevoNombre = newNameTF.getText();
                                if (nuevoNombre.isEmpty()) {
                                    nuevoNombre = (String) tareas.getSelectedItem();
                                }
                                String antiguoNombre = (String) tareas.getSelectedItem();
                                String newPrioridad = (String) prioridades.getSelectedItem();
                                if (cambiarTarea(nuevoNombre, antiguoNombre, newPrioridad)) {
                                    dtm.setRowCount(0);
                                    ArrayList<String[]> tareas = gd.rellenarTablaTarea();
                                    for (String[] tarea : tareas) {
                                        dtm.addRow(tarea);
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(
                                            panelAgregar,
                                            "Error al modificar la tarea (Recuerda no repetir nombre).",
                                            "Error modificacion",
                                            JOptionPane.ERROR_MESSAGE);

                                }

                                ArrayList<String> nombresListActualizado = nombres(); // Obtener los nombres
                                                                                      // actualizados

                                tareas.removeAllItems();

                                for (String nombre : nombresListActualizado) {
                                    tareas.addItem(nombre);
                                }
                            }
                        });
                        panelCombo.add(tarea);
                        panelCombo.add(tareas);
                        panelModificar.setPreferredSize(new Dimension(20, 300));
                        panelModificar.add(panelCombo);
                        panelNewPrioridad.add(newPrioridad);
                        panelNewPrioridad.add(prioridades);
                        panelNewName.add(newName);
                        panelNewName.add(newNameTF);
                        panelModificar.add(panelNewName);
                        panelModificar.add(panelNewPrioridad);
                        panelModificar.add(botonModificar);
                        panelModificar.revalidate();
                        panelModificar.repaint();

                        panelIzq.add(panelModificar, BorderLayout.CENTER);

                        panelIzq.revalidate();
                        panelIzq.repaint();
                    }
                });
                // Finalizacion de tareas
                finalizarTarea.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        panelIzq.removeAll();
                        panelIzq.add(panelIzqArriba, BorderLayout.NORTH);
                        panelFinalizar.removeAll();
                        panelFinalizar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
                        JPanel panelCombo = new JPanel();
                        panelCombo.setLayout(new FlowLayout());
                        JLabel tarea = new JLabel("Tarea:");
                        ArrayList<String> nombres = nombres();
                        JComboBox<String> tareas = new JComboBox<>(nombres.toArray(new String[0]));
                        tareas.setPreferredSize(new Dimension(250, 30));
                        JButton botonFinalizar = new JButton("Finalizar");

                        botonFinalizar.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
                                String tareaElegida = (String) tareas.getSelectedItem();
                                if (finTarea(tareaElegida)) {
                                    dtm.setRowCount(0);
                                    ArrayList<String[]> tareas = gd.rellenarTablaTarea();
                                    for (String[] tarea : tareas) {
                                        dtm.addRow(tarea);
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(
                                            panelAgregar,
                                            "Error al finalizar tarea.",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        });
                        panelCombo.add(tarea);
                        panelCombo.add(tareas);
                        panelFinalizar.setPreferredSize(new Dimension(20, 300));
                        panelFinalizar.add(panelCombo);
                        panelFinalizar.add(botonFinalizar);
                        panelIzq.add(panelFinalizar, BorderLayout.CENTER);

                        panelIzq.revalidate();
                        panelIzq.repaint();
                    }
                });

                panelIzqArriba.add(agregarTarea);
                panelIzqArriba.add(modificarTarea);
                panelIzqArriba.add(finalizarTarea);
                panelIzqArriba.add(botonFiltrar);

                panelArriba.add(botonTarea);
                panelArriba.add(botonTrabajo);
                panelIzq.add(panelIzqArriba, BorderLayout.NORTH);

                frame.add(panelArriba, BorderLayout.NORTH);
                frame.add(panelIzq, BorderLayout.WEST);
                frame.add(scroll, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();

            }
        });

        // Interfaz de trabajo
        botonTrabajo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.getContentPane().removeAll();
                frame.setLayout(new BorderLayout());
                panelArriba.removeAll();
                panelArriba.revalidate();
                panelArriba.repaint();
                panelCentro.removeAll();
                panelCentro.revalidate();
                panelCentro.repaint();
                panelFooter.removeAll();
                panelFooter.revalidate();
                panelFooter.repaint();
                JPanel panelIzqArriba = new JPanel();
                JDesktopPane jdp = new JDesktopPane();
                JButton negrita = new JButton("N");
                JButton cursiva = new JButton("I");
                JMenuBar menuBarJif = new JMenuBar();
                JMenu menuJif = new JMenu("Archivo");
                JMenuItem save = new JMenuItem("Guardar");
                JMenuItem insertImagen = new JMenuItem("Insertar Imagen");

                panelIzq = new JPanel();
                panelIzq.setLayout(new FlowLayout());
                nuevoTrabajo = new JButton("Nuevo trabajo");
                cargarTrabajo = new JButton("Cargar trabajo");
                botonTarea.setFont(new Font("Arial", Font.BOLD, 20));
                botonTrabajo.setFont(new Font("Arial", Font.BOLD, 20));

                nuevoTrabajo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        JInternalFrame jif = new JInternalFrame("Libreta " + contadorLibretas, true, true, true, true);
                        contadorLibretas++;
                        jif.setLayout(new BorderLayout());
                        JPanel panelJif = new JPanel();
                        panelJif.setLayout(new BorderLayout());

                        JComboBox<Integer> comboTamaño = new JComboBox<>(new Integer[] { 10, 12, 14, 16, 20, 24, 32 });

                        comboTamaño.addActionListener(e -> {

                            int tam = (int) comboTamaño.getSelectedItem();

                            SimpleAttributeSet atributos = new SimpleAttributeSet();

                            StyleConstants.setFontSize(atributos, tam);

                            // Cambia el estilo actual de escritura
                            textPane.setCharacterAttributes(atributos, false);

                        });

                        negrita.addActionListener(e -> {

                            negritaActiva = !negritaActiva; // cambia entre true/false

                            aplicarEstilo();

                        });

                        cursiva.addActionListener(e -> {
                            cursivaActiva = !cursivaActiva;
                            aplicarEstilo();
                        });

                        insertImagen.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
                                JFileChooser selector = new JFileChooser();

                                if (selector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                                    File archivo = selector.getSelectedFile();

                                    ImageIcon icono = new ImageIcon(
                                            archivo.getAbsolutePath());

                                    Image imagen = icono.getImage();

                                    Image imagenEscalada = imagen.getScaledInstance(
                                            300,
                                            200,
                                            Image.SCALE_SMOOTH);

                                    ImageIcon iconoFinal = new ImageIcon(imagenEscalada);

                                    textPane.insertIcon(iconoFinal);
                                }
                            }

                        });

                        save.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
                                JDialog dialog = new JDialog();
                                dialog.setLayout(new FlowLayout());
                                JLabel nombreArchivoLbl = new JLabel("Nombre del archivo:");
                                JTextField nombreArchivoTf = new JTextField(10);
                                JLabel categoria = new JLabel("Categoria: ");
                                DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
                                for (String nombre : sacarCategorias()) {
                                    modeloCombo.addElement(nombre);
                                }

                                JComboBox<String> categoriasCombo = new JComboBox<>(modeloCombo);
                                categoriasCombo.addItem("Nueva Categoria...");
                                categoriasCombo.setSize(20, 20);
                                JButton botonAceptarSave = new JButton("Aceptar");

                                botonAceptarSave.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent ae) {

                                        if (nombreArchivoTf.getText().equals("")) {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "El archivo debe tener un nombre.",
                                                    "Error",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }

                                        String nameFile = nombreArchivoTf.getText();

                                        if ((String) categoriasCombo.getSelectedItem() == "Nueva Categoria..."
                                                && !nombreArchivoTf.getText().equals("")) {
                                            JDialog dialogoNuevo = new JDialog();
                                            dialogoNuevo.setLayout(new FlowLayout());
                                            JTextField nombreCategoriaTf = new JTextField(10);
                                            JButton saveNuevo = new JButton("Guardar");

                                            saveNuevo.addActionListener(new ActionListener() {
                                                public void actionPerformed(ActionEvent ae) {
                                                    if (nombreCategoriaTf.getText().equals("")) {
                                                        JOptionPane.showMessageDialog(
                                                                null,
                                                                "No se ha nombrado la nueva categoria.",
                                                                "Error",
                                                                JOptionPane.ERROR_MESSAGE);
                                                    } else {

                                                        String url = nombreCategoriaTf.getText();
                                                        if (guardarArchivo(nameFile, url, textPane)) {
                                                            JOptionPane.showMessageDialog(
                                                                    null,
                                                                    "Archivo guardado correctamente");

                                                        } else {
                                                            JOptionPane.showMessageDialog(
                                                                    null,
                                                                    "Error al guardar el archivo",
                                                                    "Error",
                                                                    JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    }

                                                }
                                            });

                                            dialogoNuevo.add(categoria);
                                            dialogoNuevo.add(nombreCategoriaTf);
                                            dialogoNuevo.add(saveNuevo);
                                            dialogoNuevo.setSize(200, 150);
                                            dialogoNuevo.setLocationRelativeTo(null);
                                            dialogoNuevo.setVisible(true);

                                        } else {
                                            String url = (String) categoriasCombo.getSelectedItem();
                                            if (guardarArchivo(nameFile, url, textPane)) {
                                                JOptionPane.showMessageDialog(
                                                        null,
                                                        "Archivo guardado correctamente");
                                            } else {
                                                JOptionPane.showMessageDialog(
                                                        null,
                                                        "Error al guardar el archivo",
                                                        "Error",
                                                        JOptionPane.ERROR_MESSAGE);
                                            }
                                        }

                                    }

                                });

                                dialog.add(nombreArchivoLbl);
                                dialog.add(nombreArchivoTf);
                                dialog.add(categoria);
                                dialog.add(categoriasCombo);
                                dialog.add(botonAceptarSave);
                                dialog.setTitle("Guardar Apunte");
                                dialog.setSize(200, 200);
                                dialog.setLocationRelativeTo(null);
                                dialog.setVisible(true);

                            }

                        });

                        JPanel panelJifBotones = new JPanel();
                        panelJifBotones.add(comboTamaño);
                        panelJifBotones.add(negrita);
                        panelJifBotones.add(cursiva);

                        // Parte central

                        textPane.setMargin(new Insets(5, 5, 5, 5));
                        JScrollPane scroll = new JScrollPane(textPane);

                        scroll.setBorder(BorderFactory.createTitledBorder("Apuntes"));

                        menuJif.add(save);
                        menuJif.add(insertImagen);
                        menuBarJif.add(menuJif);

                        jif.setJMenuBar(menuBarJif);
                        panelJif.add(panelJifBotones, BorderLayout.NORTH);
                        panelJif.add(scroll, BorderLayout.CENTER);
                        jif.add(panelJif, BorderLayout.CENTER);
                        jif.setSize(600, 600);
                        jif.setLocation(50, 50);
                        jdp.add(jif);
                        jif.setVisible(true);
                    }

                });

                cargarTrabajo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        JInternalFrame jif = new JInternalFrame(
                                "",
                                true,
                                true,
                                true,
                                true);

                        JTextPane textPane = new JTextPane();

                        jif.add(new JScrollPane(textPane));

                        jif.setSize(600, 400);
                        jif.setVisible(true);

                        jdp.add(jif);

                        cargarArchivo(textPane);
                    }

                });

                panelCentro.add(jdp);
                panelIzqArriba.add(nuevoTrabajo);
                panelIzqArriba.add(cargarTrabajo);
                panelArriba.add(botonTarea);
                panelArriba.add(botonTrabajo);
                panelIzq.add(panelIzqArriba, BorderLayout.NORTH);
                frame.add(panelCentro, BorderLayout.CENTER);
                frame.add(panelArriba, BorderLayout.NORTH);
                frame.add(panelIzq, BorderLayout.WEST);
                frame.revalidate();
                frame.repaint();

            }
        });


        frame.add(panelFooter, BorderLayout.SOUTH);
        frame.add(panelArriba, BorderLayout.NORTH);
        frame.add(panelCentro, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

    }


    
    //Metodos del Front

    //Agregar una tarea
    private boolean agregarTarea(String nombre, String prioridad) {
        return gd.addTarea(nombre, prioridad);
    }

    //Nombres de las tareas
    private ArrayList<String> nombres() {
        return gd.nombreTareas();
    }

    //Finalizar una tarea
    private boolean finTarea(String tarea) {
        return gd.endTarea(tarea);
    }

    //Modificar las tareas
    private boolean cambiarTarea(String newName, String oldName, String newPrioridad) {
        return gd.changeTarea(newName, oldName, newPrioridad);
    }

   // Para cambiar los estilos de los caracteres
    private void aplicarEstilo() {

        SimpleAttributeSet atributos = new SimpleAttributeSet();

        StyleConstants.setFontFamily(atributos, "Arial");
        StyleConstants.setFontSize(atributos, 16);

        StyleConstants.setBold(atributos, negritaActiva);

        StyleConstants.setItalic(atributos, cursivaActiva);

        textPane.setCharacterAttributes(atributos, false);
    }


    // Obtener las categorias (directorios)
    private ArrayList<String> sacarCategorias() {
        return gd.getCategoria();
    }


    //Guardado de archivo
    private boolean guardarArchivo(String nombre, String categoria, JTextPane textPane) {
        return gd.saveFile(nombre, categoria, textPane);
    }

    //Carga de archivo
    public void cargarArchivo(JTextPane textPane) {

        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new File("C:\\Archivador"));

        int resultado = chooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            File archivo = chooser.getSelectedFile();

            if (gd.cargarArchivo(archivo, textPane)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Archivo cargado correctamente");

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Error al cargar",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
