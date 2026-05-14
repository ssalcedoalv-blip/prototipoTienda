package tiendaLibros.model;

/**
 * MODELO - Libro
 * Representa un libro del catálogo.
 */
public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private String genero;
    private double precio;
    private int stock;

    public Libro(String isbn, String titulo, String autor, String genero, double precio, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getGenero() { return genero; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setGenero(String genero) { this.genero = genero; }

    @Override
    public String toString() {
        return isbn + "," + titulo + "," + autor + "," + genero + "," + precio + "," + stock;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Libro)) return false;
        Libro otro = (Libro) obj;
        return this.isbn.equals(otro.isbn);
    }
}
