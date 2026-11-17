
package repositorio;

import enums.EstadoPago;
import enums.MetodoPago;
import excepciones.AccesoDatosException;
import excepciones.ElementoNoEncontradoException;
import modelo.Pagos;
import modelo.Viaje;
import persistencia.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    private final ViajeDao viajeDAO = new ViajeDao();

    // INSERTAR PAGO
    public int insertar(Pagos pago) {

        if (pago == null) {
            throw new IllegalArgumentException(
                    "El pago no puede ser nulo."
            );
        }

        if (pago.getViaje() == null ||
                pago.getViaje().getIdViaje() <= 0) {
            throw new IllegalArgumentException(
                    "El pago debe estar asociado a un viaje válido."
            );
        }

        if (pago.getMetodoPago() == null ||
                pago.getEstado() == null ||
                pago.getFechaPago() == null) {
            throw new IllegalArgumentException(
                    "Los datos del pago están incompletos."
            );
        }

        String sql = "INSERT INTO pagos " +
                "(importe, fecha_pago, metodo_pago, estado, id_viaje) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, pago.getMonto());
            ps.setTimestamp(2, Timestamp.valueOf(pago.getFechaPago()));
            ps.setString(3, pago.getMetodoPago().name());
            ps.setString(4, pago.getEstado().name());
            ps.setInt(5, pago.getViaje().getIdViaje());

            int filasInsertadas = ps.executeUpdate();

            if (filasInsertadas == 0) {
                throw new SQLException(
                        "No se pudo insertar el pago."
                );
            }

            try (ResultSet resultado = ps.getGeneratedKeys()) {

                if (resultado.next()) {
                    return resultado.getInt(1);
                }
            }

            throw new SQLException(
                    "No se pudo recuperar el ID generado para el pago."
            );

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                throw new AccesoDatosException(
                        "El viaje ya tiene un pago registrado.", e
                );
            }

            throw new AccesoDatosException(
                    "Error al insertar el pago en la base de datos.", e
            );
        }
    }

    // LISTAR TODOS LOS PAGOS
    public List<Pagos> listarTodos() {

        List<Pagos> listaPagos = new ArrayList<>();

        String sql = "SELECT * FROM pagos";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listaPagos.add(mapearPago(rs));
            }

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al listar los pagos.", e
            );
        }

        return listaPagos;
    }

    // BUSCAR PAGO POR ID
    public Pagos buscarPorId(int id) {

        String sql = "SELECT * FROM pagos WHERE id_pago = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearPago(rs);
                }
            }

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al buscar el pago por ID.", e
            );
        }

        throw new ElementoNoEncontradoException(
                "No se encontró un pago con ID: " + id
        );
    }

    // BUSCAR PAGO POR ID DE VIAJE
    public Pagos buscarPorViajeId(int idViaje) {

        String sql = "SELECT * FROM pagos WHERE id_viaje = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idViaje);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearPago(rs);
                }
            }

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al buscar el pago del viaje.", e
            );
        }

        throw new ElementoNoEncontradoException(
                "No se encontró un pago para el viaje: " + idViaje
        );
    }

    // ACTUALIZAR ESTADO DEL PAGO
    public boolean actualizarEstado(int idPago, EstadoPago nuevoEstado) {

        if (nuevoEstado == null) {
            throw new IllegalArgumentException(
                    "El estado del pago no puede ser nulo."
            );
        }

        String sql = "UPDATE pagos SET estado = ? WHERE id_pago = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idPago);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al actualizar el estado del pago.", e
            );
        }
    }

    // MAPEAR RESULTADO DE MYSQL A OBJETO PAGOS
    private Pagos mapearPago(ResultSet rs) throws SQLException {

        int idViaje = rs.getInt("id_viaje");

        Viaje viaje = viajeDAO.buscarPorId(idViaje);

        return new Pagos(
                rs.getInt("id_pago"),
                rs.getDouble("importe"),
                viaje,
                rs.getTimestamp("fecha_pago").toLocalDateTime(),
                MetodoPago.valueOf(rs.getString("metodo_pago")),
                EstadoPago.valueOf(rs.getString("estado"))
        );
    }
}
