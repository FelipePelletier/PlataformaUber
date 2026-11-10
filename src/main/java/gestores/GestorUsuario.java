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

    public void cargarUsuarios() throws SQLException{

        // Recuperamos los usuarios desde MySQL
        List<Usuario> usuariosBD = usuarioDAO.listarTodos();

        //Los incorporamos al repositorio en memoria
        for (Usuario usuario : usuariosBD){
            repositorioUsuarios.agregar(usuario);
        }
    }


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


    public List<Usuario> listarUsuarios() {

        return repositorioUsuarios.obtenerTodos();

    }


    public boolean modificarUsuario(
            int id,
            String nombre,
            String apellido,
            String dni,
            String email,
            String telefono,
            String password
        ) throws SQLException {

        // Buscamos al usuario en memoria
        Usuario usuario = buscarUsuarioPorId(id);

        // Modificamos sus datos
        usuario.modificarDatos(
                nombre,
                apellido,
                dni,
                email,
                telefono,
                password
        );

        // Guardamos los cambios en MySQL
        return usuarioDAO.actualizar(usuario);
    }



    public boolean desactivarUsuario(int id) throws SQLException {

        // Buscamos al usuario en memoria
        Usuario usuario = buscarUsuarioPorId(id);

        // Intentamos desactivarlo en MySQL
        boolean desactivado = usuarioDAO.desactivar(id);

        if (desactivado) {

            // Actualizamos el objeto en memoria
            usuario.desactivar();

            return true;
        }

        return false;
    }


    public void registrarUsuario(Usuario usuario) throws SQLException {

        // Registramos el usuario en MySQL
        usuarioDAO.insertar(usuario);

        // Lo incorporamos al repositorio en memoria
        repositorioUsuarios.agregar(usuario);
    }



}
