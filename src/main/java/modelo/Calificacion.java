package modelo;

import java.time.LocalDateTime;

public class Calificacion {
private int idCalificacion;
private int puntuacion;
private  String comentario;
private LocalDateTime fecha;
private int idAutor;
private int idViaje;
private int idCalificado;


    public Calificacion() {
    }

    public Calificacion(int puntuacion, String comentario, int idAutor, int idCalificado, int idViaje) {
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = LocalDateTime.now();
        this.idAutor=idAutor;
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

    public int getIdUsuarioCalificador() {
        return idCalificado;
    }

    public void setIdUsuarioCalificador(int idUsuarioCalificador) {
        this.idCalificado = idUsuarioCalificador;
    }

    public int getIdUsuarioCalificado() {
        return idCalificado;
    }

    public void setIdUsuarioCalificado(int idUsuarioCalificado) {
        this.idCalificado = idUsuarioCalificado;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }
    public void mostrarDatos() {
        System.out.println("===== CALIFICACIÓN =====");
        System.out.println("ID Calificación: " + this.idCalificacion);
        System.out.println("Puntuación: " + this.puntuacion + " / 5");
        System.out.println("Comentario: " + this.comentario);
        System.out.println("Fecha: " + this.fecha);
        System.out.println("ID Calificador: " + this.idAutor);
        System.out.println("ID Calificado: " + this.idCalificado);
        System.out.println("ID Viaje: " + this.idViaje);
    }
}
