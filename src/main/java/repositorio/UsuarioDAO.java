package repositorio;

import excepciones.ElementoNoEncontradoException;
import modelo.Cliente;
import modelo.Usuario;
import persistencia.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import excepciones.AccesoDatosException;

public class UsuarioDAO implements DAO<Usuario>{

    @Override
    public void insertar(Usuario usuario) throws SQLException {

        String sqlInsertarUsuario = "INSERT INTO usuarios " + "(nombre,apellido,dni,email,telefono,password,activo,calificacion_promedio,cantidad_calificaciones,tipo_usuario) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(
                    sqlInsertarUsuario,PreparedStatement.RETURN_GENERATED_KEYS)){

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getDni());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getPassword());
            ps.setBoolean(7, usuario.isActivo());
            ps.setDouble(8, usuario.getCalificacionPromedio());
            ps.setInt(9, usuario.getCantidadCalificaciones());
            ps.setString(10, usuario instanceof Cliente ? "CLIENTE" : "CONDUCTOR");

            int filasInsertadas = ps.executeUpdate();

            if (filasInsertadas > 0) {
                try (ResultSet resultado = ps.getGeneratedKeys()) {
                    if (resultado.next()) {
                        usuario.asignarIdUsuario(resultado.getInt(1));
                    }
                }
            }
        } catch (SQLException e){
            if (e.getErrorCode() == 1062) {
                throw new AccesoDatosException(
                        "El DNI o el email ya se encuentran registrados", e);
            }

            throw e;

        }
    }


    @Override
    public List<Usuario> listarTodos() throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();

        String sqlListarUsuarios =
                "SELECT * FROM usuarios WHERE tipo_usuario = 'CLIENTE'";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sqlListarUsuarios);
             ResultSet resultado = ps.executeQuery()) {

            while (resultado.next()) {

                Cliente cliente = new Cliente(
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getString("dni"),
                        resultado.getString("email"),
                        resultado.getString("telefono"),
                        resultado.getString("password")
                );

                cliente.asignarIdUsuario(resultado.getInt("id_usuario"));

                cliente.cargarDatosPersistidos(
                        resultado.getBoolean("activo"),
                        resultado.getDouble("calificacion_promedio"),
                        resultado.getInt("cantidad_calificaciones")
                );

                usuarios.add(cliente);
            }

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al listar los usuarios de la base de datos", e
            );
        }

        return usuarios;
    }



    @Override
    public Usuario buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM usuarios " +
                "WHERE id_usuario = ? AND tipo_usuario = 'CLIENTE'";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet resultado = ps.executeQuery()) {

                if (resultado.next()) {

                    Cliente cliente = new Cliente(
                            resultado.getString("nombre"),
                            resultado.getString("apellido"),
                            resultado.getString("dni"),
                            resultado.getString("email"),
                            resultado.getString("telefono"),
                            resultado.getString("password")
                    );

                    cliente.asignarIdUsuario(
                            resultado.getInt("id_usuario")
                    );

                    cliente.cargarDatosPersistidos(
                            resultado.getBoolean("activo"),
                            resultado.getDouble("calificacion_promedio"),
                            resultado.getInt("cantidad_calificaciones")
                    );

                    return cliente;
                }

                throw new ElementoNoEncontradoException(
                        "No se encontró un cliente con el ID: " + id
                );

            }

        } catch (SQLException e) {

            throw new AccesoDatosException(
                    "Error al buscar el usuario en la base de datos", e
            );
        }
    }


    @Override
    public boolean desactivar(int id) throws SQLException {

        String sql = "UPDATE usuarios SET activo = false " +
                "WHERE id_usuario = ? AND activo = true";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filasActualizadas = ps.executeUpdate();

            return filasActualizadas > 0;

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al desactivar el usuario en la base de datos", e
            );
        }
    }



    @Override
    public boolean actualizar(Usuario usuario) throws SQLException {

        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, " +
                "dni = ?, email = ?, telefono = ?, password = ? " +
                "WHERE id_usuario = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getDni());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getPassword());
            ps.setInt(7, usuario.getIdUsuario());

            int filasActualizadas = ps.executeUpdate();

            return filasActualizadas > 0;

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                throw new AccesoDatosException(
                        "El DNI o el email ya pertenecen a otro usuario", e
                );
            }

            throw new AccesoDatosException(
                    "Error al actualizar el usuario en la base de datos", e
            );
        }
    }


}
