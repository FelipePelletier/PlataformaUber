
package modelo;

import java.time.LocalDateTime;

public class Calificacion {

    private int idCalificacion;
    private int puntuacion;
    private String comentario;
    private LocalDateTime fecha;
    private int idAutor;
    private int idViaje;
    private int idCalificado;

    public Calificacion() {
    }

    public Calificacion(int puntuacion, String comentario,
                        int idAutor, int idCalificado, int idViaje) {

        setPuntuacion(puntuacion);

        this.comentario = comentario;
        this.fecha = LocalDateTime.now();
        this.idAutor = idAutor;
        this.idCalificado = idCalificado;
        this.idViaje = idViaje;
    }

    public int getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(int idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {

        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException(
                    "La puntuación debe estar entre 1 y 5."
            );
        }

        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdCalificado() {
        return idCalificado;
    }

    public void setIdCalificado(int idCalificado) {
        this.idCalificado = idCalificado;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public void mostrarDatos() {

        System.out.println("===== CALIFICACIÓN =====");
        System.out.println("ID Calificación: " + idCalificacion);
        System.out.println("Puntuación: " + puntuacion + " / 5");
        System.out.println("Comentario: " + comentario);
        System.out.println("Fecha: " + fecha);
        System.out.println("ID Calificador: " + idAutor);
        System.out.println("ID Calificado: " + idCalificado);
        System.out.println("ID Viaje: " + idViaje);
    }
}
