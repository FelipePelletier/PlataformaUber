
package gestores;

import enums.EstadoViaje;
import excepciones.CalificacionInvalidaException;
import modelo.Calificacion;
import modelo.Usuario;
import modelo.Viaje;
import repositorio.CalificacionDAO;

import java.sql.SQLException;

public class GestorCalificaciones {

    private final CalificacionDAO calificacionDAO;

    public GestorCalificaciones() {
        this.calificacionDAO = new CalificacionDAO();
    }

    public void calificarViaje(
            Viaje viaje,
            Usuario autor,
            int puntuacion,
            String comentario
    ) throws SQLException {

        if (viaje == null || autor == null) {
            throw new CalificacionInvalidaException(
                    "El viaje y el usuario son obligatorios."
            );
        }

        if (viaje.getEstado() != EstadoViaje.FINALIZADO) {
            throw new CalificacionInvalidaException(
                    "Solo se pueden calificar viajes finalizados."
            );
        }

        if (puntuacion < 1 || puntuacion > 5) {
            throw new CalificacionInvalidaException(
                    "La puntuacion debe estar entre 1 y 5."
            );
        }

        if (comentario != null && comentario.length() > 255) {
            throw new CalificacionInvalidaException(
                    "El comentario no puede superar los 255 caracteres."
            );
        }

        Usuario calificado;

        if (viaje.getCliente().getIdUsuario()
                == autor.getIdUsuario()) {

            calificado = viaje.getConductor();

        } else if (viaje.getConductor() != null &&
                viaje.getConductor().getIdUsuario()
                        == autor.getIdUsuario()) {

            calificado = viaje.getCliente();

        } else {
            throw new CalificacionInvalidaException(
                    "No participaste de este viaje."
            );
        }

        if (calificado == null) {
            throw new CalificacionInvalidaException(
                    "No hay un usuario para calificar."
            );
        }

        Calificacion calificacion = new Calificacion(
                puntuacion,
                comentario,
                autor.getIdUsuario(),
                calificado.getIdUsuario(),
                viaje.getIdViaje()
        );

        calificacionDAO.insertar(calificacion);

        calificado.recibirCalificacion(puntuacion);
    }
}
