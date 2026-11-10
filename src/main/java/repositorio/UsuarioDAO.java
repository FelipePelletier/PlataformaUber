package repositorio;

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
        return new ArrayList<>();
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {

        String sqlBuscarUsuario = "SELECT * FROM usuarios WHERE id_usuario = ? AND tipo_usuario = 'CLIENTE'";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sqlBuscarUsuario)) {

            ps.setInt(1, id);

            try(ResultSet resultado = ps.executeQuery()){
                if (resultado.next()){

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

                    return cliente;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public void desactivar(int id) throws SQLException {

        String sqlDesactivarUsuario =
                "UPDATE usuarios SET activo = false WHERE id_usuario = ? and activo = true";


        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sqlDesactivarUsuario)) {

            ps.setInt(1, id);

            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("Usuario desactivado correctamente");
            } else {
                System.out.println("El usuario no existe o ya estaba desactivado");
            }
        }
    }

    @Override
    public void actualizar(Usuario usuario) throws SQLException {

        String sqlActualizarUsuario =
                "UPDATE usuarios SET nombre = ?, apellido = ?, dni = ?, email = ?, telefono = ?, password = ? " +
                        "WHERE id_usuario = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sqlActualizarUsuario)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getDni());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getPassword());
            ps.setInt(7, usuario.getIdUsuario());

            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("Usuario actualizado correctamente");
            } else {
                System.out.println("No se encontro un usuario con ese ID");
            }
        } catch (SQLException e){

            if (e.getErrorCode() == 1062) {
                throw new AccesoDatosException(
                        "El DNI o el email ya pertenecen a otro usuario",
                        e
                );
            }

            throw e;
        }
    }
}
