
package repositorio;

import excepciones.AccesoDatosException;
import modelo.Calificacion;
import persistencia.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CalificacionDAO {

    public void insertar(Calificacion calificacion) throws SQLException {

        String sqlInsertar =
                "INSERT INTO calificaciones " +
                        "(puntuacion, comentario, fecha_calificacion, id_viaje, " +
                        "id_usuario_calificador, id_usuario_calificado) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlPromedio =
                "UPDATE usuarios SET " +
                        "calificacion_promedio = " +
                        "((calificacion_promedio * cantidad_calificaciones) + ?) " +
                        "/ (cantidad_calificaciones + 1), " +
                        "cantidad_calificaciones = cantidad_calificaciones + 1 " +
                        "WHERE id_usuario = ?";

        try (Connection conexion = ConexionDB.obtenerConexion()) {

            conexion.setAutoCommit(false);

            try {

                // Guardar la calificación
                try (PreparedStatement ps =
                             conexion.prepareStatement(sqlInsertar)) {

                    ps.setInt(1, calificacion.getPuntuacion());
                    ps.setString(2, calificacion.getComentario());
                    ps.setTimestamp(3,
                            Timestamp.valueOf(calificacion.getFecha()));
                    ps.setInt(4, calificacion.getIdViaje());
                    ps.setInt(5, calificacion.getIdAutor());
                    ps.setInt(6, calificacion.getIdCalificado());

                    ps.executeUpdate();
                }

                // Actualizar el promedio del usuario calificado
                try (PreparedStatement psPromedio =
                             conexion.prepareStatement(sqlPromedio)) {

                    psPromedio.setInt(1, calificacion.getPuntuacion());
                    psPromedio.setInt(2, calificacion.getIdCalificado());

                    if (psPromedio.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se encontró el usuario calificado."
                        );
                    }
                }

                // Confirmar ambas operaciones
                conexion.commit();

            } catch (SQLException e) {

                conexion.rollback();

                if (e.getErrorCode() == 1062) {
                    throw new SQLException(
                            "Ya calificaste este viaje.",
                            e
                    );
                }

                throw e;

            } finally {

                conexion.setAutoCommit(true);
            }

        } catch (SQLException e) {

            throw new AccesoDatosException(
                    "No se pudo guardar la calificación: "
                            + e.getMessage(),
                    e
            );
        }
    }
}
