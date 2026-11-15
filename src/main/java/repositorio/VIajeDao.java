package repositorio;

import excepciones.AccesoDatosException;
import excepciones.ElementoNoEncontradoException;
import modelo.Viaje;
import persistencia.ConexionDB;

import java.sql.*;
import java.util.List;

public class VIajeDao implements DAO<Viaje>{

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
}
