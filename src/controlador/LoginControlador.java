package tiendaLibros.controlador;

import tiendaLibros.model.Usuario;
import tiendaLibros.model.estructuras.ListaSencilla;
import tiendaLibros.util.ArchivoUtil;


public class LoginControlador {


    private ListaSencilla<Usuario> listaUsuarios;

    public LoginControlador() {
     
        listaUsuarios = ArchivoUtil.cargarUsuarios();
    }

  
    public Usuario validarLogin(String nombre, String contrasena) {
        return listaUsuarios.buscar(u ->
            u.getNombre().equals(nombre) && u.getContrasena().equals(contrasena)
        );
    }

  
    public boolean registrarUsuario(String nombre, String contrasena, String rol) {
    
        Usuario existente = listaUsuarios.buscar(u -> u.getNombre().equals(nombre));
        if (existente != null) return false;

        Usuario nuevo = new Usuario(nombre, contrasena, rol);
        listaUsuarios.agregar(nuevo);
        ArchivoUtil.guardarUsuarios(listaUsuarios);
        return true;
    }

    public ListaSencilla<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
}
