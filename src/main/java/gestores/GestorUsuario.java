package gestores;

import excepciones.ElementoNoEncontradoException;
import modelo.Usuario;
import repositorio.Repositorio;
import repositorio.UsuarioDAO;

import java.sql.SQLException;
import java.util.List;

public class GestorUsuario {

    private Repositorio<Usuario> repositorioUsuarios;
    private UsuarioDAO usuarioDAO;

    public GestorUsuario() {
        this.repositorioUsuarios = new Repositorio<>();
        this.usuarioDAO = new UsuarioDAO();
    }

    // Cargar usuarios desde la base de datos
    public void cargarUsuarios() throws SQLException {

        List<Usuario> usuariosBD = usuarioDAO.listarTodos();

        for (Usuario usuario : usuariosBD) {
            repositorioUsuarios.agregar(usuario);
        }
    }

    // Buscar usuario por ID
    public Usuario buscarUsuarioPorId(int id) {

        Usuario usuario = repositorioUsuarios.buscarPorId(
                String.valueOf(id)
        );

        if (usuario == null) {
            throw new ElementoNoEncontradoException(
                    "No se encontró un usuario con el ID: " + id
            );
        }

        return usuario;
    }

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {

        return repositorioUsuarios.obtenerTodos();
    }

    // Modificar los datos de un usuario
    public boolean modificarUsuario(
            int id,
            String nombre,
            String apellido,
            String dni,
            String email,
            String telefono,
            String password
    ) throws SQLException {

        Usuario usuario = buscarUsuarioPorId(id);

        usuario.modificarDatos(
                nombre,
                apellido,
                dni,
                email,
                telefono,
                password
        );

        return usuarioDAO.actualizar(usuario);
    }

    // Desactivar un usuario
    public boolean desactivarUsuario(int id) throws SQLException {

        Usuario usuario = buscarUsuarioPorId(id);

        boolean desactivado = usuarioDAO.desactivar(id);

        if (desactivado) {
            usuario.desactivar();
            return true;
        }

        return false;
    }

    // Registrar un usuario
    public void registrarUsuario(Usuario usuario) throws SQLException {

        usuarioDAO.insertar(usuario);

        repositorioUsuarios.agregar(usuario);
    }
}