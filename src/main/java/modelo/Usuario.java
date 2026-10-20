package modelo;

import interfaces.Calificable;

public abstract class Usuario implements Calificable {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private String password;
    private boolean activo;
    private double calificacionPromedio;
    private int cantidadCalificaciones;

    public Usuario(String nombre, String apellido, String dni, String email, String telefono, String password) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.activo = true;
        this.calificacionPromedio = 0.0;
        this.cantidadCalificaciones = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public int getCantidadCalificaciones() {
        return cantidadCalificaciones;
    }

    @Override
    public void recibirCalificacion(int puntuacion) {
        if (puntuacion < 1 || puntuacion > 5){
            throw new IllegalArgumentException(
                    "La puntuacion debe estar entre 1 y 5");
        }

        calificacionPromedio = (calificacionPromedio * cantidadCalificaciones + puntuacion) / (cantidadCalificaciones + 1);

        cantidadCalificaciones++;
    }

    @Override
    public double obtenerCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void activar(){
        this.activo = true;
    }

    public void desactivar(){
        this.activo = false;
    }

    public void cerrarSesion(){

    }

    public boolean iniciarSesion(String email,String password){
        return activo && this.email != null && this.password != null && this.email.equals(email) && this.password.equals(password);
    }

    public void modificarDatos(String nombre, String apellido,String dni, String email, String telefono, String password){

       if (nombre == null || nombre.isBlank()
               || apellido == null || apellido.isBlank()
               || dni == null || dni.isBlank()
               || email == null || email.isBlank()
               || telefono == null || telefono.isBlank()
               || password == null || password.isBlank()){

           throw new IllegalArgumentException(
                   "Todos los campos son obligatorios"
           );
       }

       this.nombre = nombre;
       this.apellido = apellido;
       this.dni = dni;
       this.email = email;
       this.telefono = telefono;
       this.password = password;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", activo=" + activo +
                ", calificacionPromedio=" + calificacionPromedio +
                '}';
    }

    public void mostrarDatos(){
        System.out.println(toString());
    }
}
