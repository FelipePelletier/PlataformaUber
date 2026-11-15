package repositorio;

import enums.EstadoViaje;
import excepciones.AccesoDatosException;
import excepciones.ElementoNoEncontradoException;
import modelo.Cliente;
import modelo.Conductor;
import modelo.Usuario;
import modelo.Viaje;
import persistencia.ConexionDB;

import java.sql.*;
import java.text.CompactNumberFormat;
import java.util.ArrayList;
import java.util.List;

public class VIajeDao implements DAO<Viaje>{
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public VIajeDao() {
        super();
    }

    @Override
    public void insertar(Viaje v) throws SQLException {

        String sqlInsertar="INSERT INTO viajes"+
                "(origen, destino, fecha_hora, precio, estado, id_cliente, id_conductor)"+
                "VALUES(?,?,?,?,?,?,?)";
        try(Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement ps = conexion.prepareStatement(
                    sqlInsertar,PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, v.getOrigen());
            ps.setString(2,v.getDestino());
            ps.setTimestamp(3, Timestamp.valueOf(v.getFechaHora()));
            ps.setDouble(4,v.getPrecio());
            ps.setString(5,v.getEstado().name());
            ps.setInt(6,v.getCliente().getIdUsuario());
            ps.setInt(7,v.getConductor().getIdUsuario);

            int filas= ps.executeUpdate();
            if(filas>0)
            {
                try(ResultSet rs=ps.getGeneratedKeys())
                {
                    if (rs.next()) {
                        v.setIdViaje(rs.getInt(1));
                    }
                }catch (SQLException e ){
                throw new AccesoDatosException(
                    "ERROR al insertar el viaje en la base de datos", e);
                }
            }

        }


    }

    @Override
    public List<Viaje> listarTodos() throws SQLException {

        List<Viaje> viajes = new ArrayList<>();
        String sqlListar = "SELECT * FROM viajes";
        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sqlListar);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                viajes.add(mapeoViaje(rs));
            }

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "ERROR listar los viajes de la base de datos", e);
        }
        return viajes;
    }





        return List.of();
    }

    @Override
    public Viaje buscarPorId(int id) throws SQLException, ElementoNoEncontradoException {
        return null;
    }

    @Override
    public boolean activar(int id) throws SQLException {
        return false;
    }

    @Override
    public boolean desactivar(int id) throws SQLException {
        return false;
    }

    @Override
    public boolean actualizar(Viaje objeto) throws SQLException {
        return false;
    }

    private Viaje mapeoViaje(ResultSet rs) throws SQLException {

        int idCliente = rs.getInt("id_cliente");
        Usuario usuario = usuarioDAO.buscarPorId(idCliente);

        if (!(usuario instanceof Cliente)) {
            throw new AccesoDatosException(
                    "El viaje " + rs.getInt("id_viaje") + " no tiene un cliente válido");
        }

        return new Viaje(
                rs.getInt("id_viaje"),
                rs.getString("origen"),
                rs.getString("destino"),
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                rs.getDouble("precio"),
                EstadoViaje.valueOf(rs.getString("estado")),
                (Cliente) usuario,
                null //falta clase conductor
        );
    }
}
