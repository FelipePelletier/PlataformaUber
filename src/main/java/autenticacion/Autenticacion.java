
package autenticacion;

import modelo.Usuario;
import repositorio.UsuarioDAO;

public class Autenticacion {

    private UsuarioDAO usuarioDAO;

    public Autenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String email, String password) {

        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "El email y la contraseña son obligatorios."
            );
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            return null;
        }

        if (!usuario.iniciarSesion(email, password)) {
            return null;
        }

        return usuario;
    }

    public void cerrarSesion(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "No hay un usuario para cerrar sesión."
            );
        }

        usuario.cerrarSesion();
    }
}
