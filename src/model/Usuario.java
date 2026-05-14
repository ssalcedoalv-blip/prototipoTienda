package tiendaLibros.model;

/**
 * MODELO - Usuario
 * Representa un usuario del sistema (admin o cliente).
 */
public class Usuario {
    private String nombre;
    private String contrasena;
    private String rol; // "admin" o "usuario"

    public Usuario(String nombre, String contrasena, String rol) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getNombre() { return nombre; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }

    @Override
    public String toString() {
        return nombre + "," + contrasena + "," + rol;
    }
}
