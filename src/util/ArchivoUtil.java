package tiendaLibros.util;

import tiendaLibros.model.Libro;
import tiendaLibros.model.Usuario;
import tiendaLibros.model.estructuras.ListaDobleCircular;
import tiendaLibros.model.estructuras.ListaSencilla;

import java.io.*;
import java.nio.file.*;


public class ArchivoUtil {

    private static final String CARPETA = "datos/";
    private static final String ARCHIVO_USUARIOS = CARPETA + "usuarios.txt";
    private static final String ARCHIVO_LIBROS   = CARPETA + "libros.txt";
    private static final String ARCHIVO_HISTORIAL = CARPETA + "historial.txt";

 
    public static void inicializarArchivos() {
        try {
            Files.createDirectories(Paths.get(CARPETA));

         
            File fUsuarios = new File(ARCHIVO_USUARIOS);
            if (!fUsuarios.exists()) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(fUsuarios))) {
                    pw.println("admin,admin123,admin");
                    pw.println("juan,juan123,usuario");
                    pw.println("maria,maria456,usuario");
                }
            }

          
            File fLibros = new File(ARCHIVO_LIBROS);
            if (!fLibros.exists()) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(fLibros))) {
                    pw.println("ISBN001,Cien años de soledad,Gabriel García Márquez,Novela,35000,10");
                    pw.println("ISBN002,El principito,Antoine de Saint-Exupéry,Infantil,22000,15");
                    pw.println("ISBN003,1984,George Orwell,Ciencia Ficción,28000,8");
                    pw.println("ISBN004,Don Quijote de la Mancha,Miguel de Cervantes,Clásico,45000,5");
                    pw.println("ISBN005,Harry Potter y la piedra filosofal,J.K. Rowling,Fantasía,38000,12");
                    pw.println("ISBN006,El alquimista,Paulo Coelho,Autoayuda,25000,20");
                }
            }

         
            File fHistorial = new File(ARCHIVO_HISTORIAL);
            if (!fHistorial.exists()) fHistorial.createNewFile();

        } catch (IOException e) {
            System.err.println("Error inicializando archivos: " + e.getMessage());
        }
    }

   
    public static ListaSencilla<Usuario> cargarUsuarios() {
        ListaSencilla<Usuario> lista = new ListaSencilla<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] p = linea.split(",");
                    if (p.length == 3) {
                        lista.agregar(new Usuario(p[0].trim(), p[1].trim(), p[2].trim()));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
        }
        return lista;
    }

   
    public static void guardarUsuarios(ListaSencilla<Usuario> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_USUARIOS))) {
            tiendaLibros.model.nodos.Nodo<Usuario> actual = lista.getCabeza();
            while (actual != null) {
                pw.println(actual.dato.toString());
                actual = actual.siguiente;
            }
        } catch (IOException e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
        }
    }

    
    public static ListaDobleCircular<Libro> cargarLibros() {
        ListaDobleCircular<Libro> lista = new ListaDobleCircular<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_LIBROS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] p = linea.split(",");
                    if (p.length == 6) {
                        lista.agregar(new Libro(
                            p[0].trim(), p[1].trim(), p[2].trim(),
                            p[3].trim(), Double.parseDouble(p[4].trim()),
                            Integer.parseInt(p[5].trim())
                        ));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando libros: " + e.getMessage());
        }
        return lista;
    }

   
    public static void guardarLibros(ListaDobleCircular<Libro> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_LIBROS))) {
            if (!lista.estaVacia()) {
                tiendaLibros.model.nodos.Nodo<Libro> actual = lista.getCabeza();
                for (int i = 0; i < lista.getTamanio(); i++) {
                    pw.println(actual.dato.toString());
                    actual = actual.siguiente;
                }
            }
        } catch (IOException e) {
            System.err.println("Error guardando libros: " + e.getMessage());
        }
    }

 
    public static void agregarHistorial(String linea) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_HISTORIAL, true))) {
            pw.println(linea);
        } catch (IOException e) {
            System.err.println("Error guardando historial: " + e.getMessage());
        }
    }

   
    public static String[] leerHistorial() {
        java.util.List<String> lineas = new java.util.ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_HISTORIAL))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) lineas.add(linea.trim());
            }
        } catch (IOException e) {
            System.err.println("Error leyendo historial: " + e.getMessage());
        }
        return lineas.toArray(new String[0]);
    }
}
