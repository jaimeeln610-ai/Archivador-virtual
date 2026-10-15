package com.example;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;

public class GestorDatos {

    // Metodo para agregar una tarea
    public boolean addTarea(String nombre, String prioridad) {
        String sqlStr = "INSERT INTO agenda (tarea, prioridad) VALUES (?, ?)";
        try (Connection con = openConnection();
                PreparedStatement ps = con.prepareStatement(sqlStr);) {
            ps.setString(1, nombre);
            ps.setString(2, prioridad);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // Metodo para finalizar una tarea
    public boolean endTarea(String tarea) {
        String sqlStr = "UPDATE agenda SET estado = 1 WHERE tarea = ? AND estado = 0";
        try (Connection con = openConnection();
                PreparedStatement ps = con.prepareStatement(sqlStr);) {
            ps.setString(1, tarea);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // Metodo para modificar una tarea
    public boolean changeTarea(String nombreNuevo, String nombreAntiguo, String newPrioridad) {
        String sqlStr = "UPDATE agenda SET tarea = ? , prioridad = ? WHERE tarea = ?";

        try (Connection con = openConnection();
                PreparedStatement ps = con.prepareStatement(sqlStr);) {
            ps.setString(1, nombreNuevo);
            ps.setString(2, newPrioridad);
            ps.setString(3, nombreAntiguo);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // Metodo para obtener el nombre de las tareas
    public ArrayList<String> nombreTareas() {
        String sqlStr = "SELECT tarea FROM agenda";
        ArrayList<String> datos = new ArrayList<>();

        try (Connection con = openConnection();
                PreparedStatement ps = con.prepareStatement(sqlStr);) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    datos.add(rs.getString("tarea"));
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return datos;
    }

    // Metodo para rellenar la tabla
    public ArrayList<String[]> rellenarTablaTarea() {
        String sqlStr = "SELECT tarea, estado, prioridad FROM agenda ORDER BY id DESC";
        ArrayList<String[]> datos = new ArrayList<>();

        try (Connection con = openConnection();
                PreparedStatement ps = con.prepareStatement(sqlStr);) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String[] fila = new String[3];
                    fila[0] = rs.getString("tarea");

                    if (rs.getString("estado").equals("0")) {
                        fila[1] = "Sin hacer";
                    } else {
                        fila[1] = "Hecha";
                    }

                    fila[2] = rs.getString("prioridad");

                    datos.add(fila);
                }

            }

            return datos;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error");
            return null;
        }
    }

    // Metodo de conexion a la BBDD
    public static Connection openConnection() {
        Connection con = null;
        try {
            // jdbc:mariadb://servidor:puerto/nombreBaseDatos?usuario=xxx&contraseña=xxx
            String connectionUrl = "jdbc:mariadb://100.90.186.64:3306/archivador?user=archivador&password=1234";

            // Obtenemos el objeto Connection que representa la conexión
            con = DriverManager.getConnection(connectionUrl);

        } catch (SQLException e) {
            // Capturamos errores relacionados con SQL y la base de datos
            System.out.println("No es posible conectar a la bd");
            e.printStackTrace();
        }
        return con;
    }

    // Metodo para obtener las categorias(directorios) que existen
    public ArrayList<String> getCategoria() {
        ArrayList<String> nombres = new ArrayList<>();
        try {
            File carpeta = new File("C:\\Archivador");
            File[] contenido = carpeta.listFiles();

            for (File file : contenido) {
                if (file.isDirectory()) {
                    nombres.add(file.getName());
                }
            }
            for (String nombre : nombres) {
                System.out.println(nombre);
            }
            return nombres;
        } catch (Exception e) {
            // el uso del try catch es para que no de fallo si el contenido del array es
            // nulo
            return nombres;
        }

    }

    // Metodo para guardar el archivo en html
    public boolean saveFile(String nombreArchivo, String Ubicacion, JTextPane textPane) {
        String ruta = "C:\\Archivador\\" + Ubicacion;
        String name = nombreArchivo + ".html";

        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdir();
        }
        File archivo = new File(directorio, name);

        try {

            HTMLEditorKit kit = new HTMLEditorKit();

            FileOutputStream salida = new FileOutputStream(archivo);

            kit.write(salida, textPane.getDocument(), 0, textPane.getDocument().getLength());

            salida.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metodo para cargar el archivo html
    public boolean cargarArchivo(File archivo, JTextPane textPane) {

        try {

            HTMLEditorKit kit = new HTMLEditorKit();
            textPane.setEditorKit(kit);

            FileInputStream entrada = new FileInputStream(archivo);

            kit.read(
                    entrada,
                    textPane.getDocument(),
                    0);

            entrada.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

}
