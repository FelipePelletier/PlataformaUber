package repositorio;

import enums.EstadoViaje;
import enums.MetodoPago;
import excepciones.AccesoDatosException;
import excepciones.ElementoNoEncontradoException;
import modelo.Cliente;
import modelo.Conductor;
import modelo.Usuario;
import modelo.Viaje;
import persistencia.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViajeDao implements DAO<Viaje>{
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public ViajeDao() {
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
            if (v.getConductor() != null) {
                ps.setInt(7, v.getConductor().getIdUsuario());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
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
        String sqlListar =
                "SELECT v.*, p.metodo_pago " +
                        "FROM viajes v " +
                        "LEFT JOIN pagos p ON v.id_viaje = p.id_viaje";
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


    @Override
    public Viaje buscarPorId(int id) throws SQLException, ElementoNoEncontradoException {

        String sqlBuscar =
                "SELECT v.*, p.metodo_pago " +
                        "FROM viajes v " +
                        "LEFT JOIN pagos p ON v.id_viaje = p.id_viaje " +
                        "WHERE v.id_viaje = ?";
 try(Connection conexion=ConexionDB.obtenerConexion();
     PreparedStatement ps= conexion.prepareStatement(sqlBuscar)){
     ps.setInt(1, id);


     try(ResultSet rs= ps.executeQuery()){

          if(rs.next())
         {
         return mapeoViaje(rs);
         }
     }throw new ElementoNoEncontradoException(
             "No se encuentra el ID: " + id);
     }catch (SQLException e)
 {
     throw new AccesoDatosException("ERROR al buscar el viaje en la base de datos"+ e);
 }

 }
public boolean cancelar(int id)
{
    String sqlCancelar="UPDATE viajes SET estado = 'CANCELADO'"+
            "WHERE id_viaje = ? AND estado IN ('PENDIENTE','ACEPTADO')";

    try(Connection conexion=ConexionDB.obtenerConexion();

         PreparedStatement ps= conexion.prepareStatement(sqlCancelar)){

        ps.setInt(1,id);
        return ps.executeUpdate()>0;



    }catch (SQLException e){
        throw new AccesoDatosException("ERROR cancelando el viaje en la base de datos"+e);
    }

}

    @Override
    public boolean actualizar(Viaje v) throws SQLException {

        String sqlActualizar = "UPDATE viajes SET origen = ?, destino = ?, " +
                "precio = ?, estado = ?,id_conductor = ? WHERE id_viaje = ?";

        try(Connection conexion=ConexionDB.obtenerConexion();
            PreparedStatement ps= conexion.prepareStatement(sqlActualizar))
        {

            ps.setString(1,v.getOrigen());
            ps.setString(2,v.getDestino());
            ps.setDouble(3,v.getPrecio());
            ps.setString(4,v.getEstado().name());
            if (v.getConductor() != null) {
                ps.setInt(5, v.getConductor().getIdUsuario());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, v.getIdViaje());
            return ps.executeUpdate() > 0;

        }catch (SQLException e) {
            throw new AccesoDatosException(
                    "ERROR actualizando el viaje en la base de datos", e);
        }

    }


    @Override
    public boolean desactivar(int id) throws SQLException {
        throw new UnsupportedOperationException("Un viaje no se desactiva");
    }

    @Override
    public boolean activar(int id) throws SQLException {
        throw new UnsupportedOperationException("Un viaje no se activa");
    }

    private Viaje mapeoViaje(ResultSet rs) throws SQLException {

        int idCliente = rs.getInt("id_cliente");
        Usuario usuario = usuarioDAO.buscarPorId(idCliente);

        if (!(usuario instanceof Cliente)) {
            throw new AccesoDatosException(
                    "El viaje " + rs.getInt("id_viaje")
                            + " no tiene un cliente válido"
            );
        }

        Conductor conductor = null;
        int idConductor = rs.getInt("id_conductor");

        if (idConductor > 0) {
            Usuario usuarioConductor = usuarioDAO.buscarPorId(idConductor);

            if (usuarioConductor instanceof Conductor) {
                conductor = (Conductor) usuarioConductor;
            }
        }

        // Recuperar el método de pago desde la tabla pagos
        String metodoPagoBD = rs.getString("metodo_pago");

        MetodoPago metodoPago = null;

        if (metodoPagoBD != null) {
            metodoPago = MetodoPago.valueOf(metodoPagoBD);
        }

        return new Viaje(
                rs.getInt("id_viaje"),
                rs.getString("origen"),
                rs.getString("destino"),
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                rs.getDouble("precio"),
                EstadoViaje.valueOf(rs.getString("estado")),
                (Cliente) usuario,
                conductor,
                metodoPago
        );
    }
}
