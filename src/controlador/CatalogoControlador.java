package tiendaLibros.controlador;

import tiendaLibros.model.Libro;
import tiendaLibros.model.estructuras.ListaDobleCircular;
import tiendaLibros.util.ArchivoUtil;


public class CatalogoControlador {

    private ListaDobleCircular<Libro> catalogo;

    public CatalogoControlador() {
        catalogo = ArchivoUtil.cargarLibros();
    }


    public boolean agregarLibro(Libro libro) {
     
        Libro existe = catalogo.buscar(l -> l.getIsbn().equals(libro.getIsbn()));
        if (existe != null) return false;
        catalogo.agregar(libro);
        ArchivoUtil.guardarLibros(catalogo);
        return true;
    }

   
    public boolean eliminarLibro(String isbn) {
        Libro libro = catalogo.buscar(l -> l.getIsbn().equals(isbn));
        if (libro == null) return false;
        catalogo.eliminar(libro);
        ArchivoUtil.guardarLibros(catalogo);
        return true;
    }

 
    public Libro buscarLibro(String texto) {
        return catalogo.buscar(l ->
            l.getIsbn().equalsIgnoreCase(texto) ||
            l.getTitulo().toLowerCase().contains(texto.toLowerCase())
        );
    }

   
    public boolean actualizarLibro(String isbn, String titulo, String autor,
                                    String genero, double precio, int stock) {
        Libro libro = catalogo.buscar(l -> l.getIsbn().equals(isbn));
        if (libro == null) return false;
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setGenero(genero);
        libro.setPrecio(precio);
        libro.setStock(stock);
        ArchivoUtil.guardarLibros(catalogo);
        return true;
    }

    public Libro[] getLibrosArray() {
        Object[] arr = catalogo.toArray();
        Libro[] libros = new Libro[arr.length];
        for (int i = 0; i < arr.length; i++) libros[i] = (Libro) arr[i];
        return libros;
    }

    public ListaDobleCircular<Libro> getCatalogo() { return catalogo; }
}
