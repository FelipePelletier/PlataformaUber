
package repositorio;

import modelo.Vehiculo;
import modelo.Auto;
import modelo.Moto;
import persistencia.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public Vehiculo buscarPorConductorId(int idConductor) throws SQLException {

        String sql = "SELECT * FROM vehiculos WHERE id_conductor = ? LIMIT 1";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idConductor);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String tipo = rs.getString("tipo_vehiculo");

                    Vehiculo vehiculo;

                    if ("AUTO".equals(tipo)) {
                        vehiculo = new Auto(
                                rs.getString("patente"),
                                rs.getString("marca"),
                                rs.getString("modelo"),
                                rs.getInt("anio"),
                                rs.getString("color"),
                                rs.getInt("cant_puertas")
                        );

                    } else if ("MOTO".equals(tipo)) {
                        vehiculo = new Moto(
                                rs.getString("patente"),
                                rs.getString("marca"),
                                rs.getString("modelo"),
                                rs.getInt("anio"),
                                rs.getString("color"),
                                rs.getInt("cilindrada")
                        );

                    } else {
                        throw new SQLException("Tipo de vehículo desconocido.");
                    }

                    vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                    vehiculo.setActivo(rs.getBoolean("disponible"));

                    return vehiculo;
                }

                return null;
            }

        } catch (SQLException e) {
            throw new SQLException("Error al buscar el vehículo del conductor.", e);
        }
    }

    public List<Vehiculo> listarPorConductorId(int idConductor)
            throws SQLException {

        List<Vehiculo> vehiculos = new ArrayList<>();

        String sql = "SELECT * FROM vehiculos WHERE id_conductor = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idConductor);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String tipo = rs.getString("tipo_vehiculo");

                    Vehiculo vehiculo;

                    if ("AUTO".equals(tipo)) {

                        vehiculo = new Auto(
                                rs.getString("patente"),
                                rs.getString("marca"),
                                rs.getString("modelo"),
                                rs.getInt("anio"),
                                rs.getString("color"),
                                rs.getInt("cant_puertas")
                        );

                    } else if ("MOTO".equals(tipo)) {

                        vehiculo = new Moto(
                                rs.getString("patente"),
                                rs.getString("marca"),
                                rs.getString("modelo"),
                                rs.getInt("anio"),
                                rs.getString("color"),
                                rs.getInt("cilindrada")
                        );

                    } else {
                        throw new SQLException("Tipo de vehículo desconocido.");
                    }

                    vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                    vehiculo.setActivo(rs.getBoolean("disponible"));

                    vehiculos.add(vehiculo);
                }
            }

        } catch (SQLException e) {
            throw new SQLException(
                    "Error al listar los vehículos del conductor.", e
            );
        }

        return vehiculos;
    }

    public void insertar(Vehiculo vehiculo, int idConductor)
            throws SQLException {

        String sql = "INSERT INTO vehiculos " +
                "(patente, marca, modelo, anio, color, tipo_vehiculo, " +
                "disponible, id_conductor, cant_puertas, cilindrada) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, vehiculo.getPatente());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            ps.setString(5, vehiculo.getColor());

            if (vehiculo instanceof Auto) {
                Auto auto = (Auto) vehiculo;
                ps.setString(6, "AUTO");
                ps.setInt(9, auto.getCantPuertas());
                ps.setNull(10, java.sql.Types.INTEGER);

            } else if (vehiculo instanceof Moto) {
                Moto moto = (Moto) vehiculo;
                ps.setString(6, "MOTO");
                ps.setNull(9, java.sql.Types.INTEGER);
                ps.setInt(10, moto.getCilindrada());

            } else {
                throw new SQLException("Tipo de vehículo desconocido.");
            }

            ps.setBoolean(7, true);
            ps.setInt(8, idConductor);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    vehiculo.setIdVehiculo(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("La patente ya se encuentra registrada.", e);
            }

            throw new SQLException("Error al registrar el vehículo.", e);
        }
    }

    public boolean asignarAConductor(int idVehiculo, int idConductor)
            throws SQLException {

        String sqlBuscarVehiculo =
                "SELECT id_conductor FROM vehiculos " +
                        "WHERE id_vehiculo = ? FOR UPDATE";

        String sqlBuscarAsignacion =
                "SELECT id_vehiculo FROM vehiculos " +
                        "WHERE id_conductor = ? AND id_vehiculo <> ?";

        String sqlActualizar =
                "UPDATE vehiculos SET id_conductor = ? " +
                        "WHERE id_vehiculo = ?";

        try (Connection conexion = ConexionDB.obtenerConexion()) {

            conexion.setAutoCommit(false);

            try {
                // Verificamos que exista el vehículo
                int conductorActual;

                try (PreparedStatement ps = conexion.prepareStatement(sqlBuscarVehiculo)) {
                    ps.setInt(1, idVehiculo);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conexion.rollback();
                            return false;
                        }

                        conductorActual = rs.getInt("id_conductor");
                    }
                }

                // Si ya pertenece a otro conductor, no lo reasignamos
                if (conductorActual != idConductor) {
                    conexion.rollback();
                    return false;
                }

                // Verificamos que el conductor no tenga otro vehículo
                try (PreparedStatement ps = conexion.prepareStatement(sqlBuscarAsignacion)) {
                    ps.setInt(1, idConductor);
                    ps.setInt(2, idVehiculo);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement ps = conexion.prepareStatement(sqlActualizar)) {
                    ps.setInt(1, idConductor);
                    ps.setInt(2, idVehiculo);

                    ps.executeUpdate();
                }

                conexion.commit();
                return true;

            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new SQLException("Error al asignar el vehículo.", e);
        }
    }
}
