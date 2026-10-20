package modelo;

import interfaces.Calificable;
import excepciones.CalificacionInvalidaException;

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

        this.nombre = validarCampo(nombre,"nombre");
        this.apellido = validarCampo(apellido,"apellido");
        this.dni = validarCampo(dni,"DNI");
        this.email = validarEmail(email);
        this.telefono = validarCampo(telefono,"telefono");
        this.password = validarCampo(password,"password");

        this.activo = true;
        this.calificacionPromedio = 0.0;
        this.cantidadCalificaciones = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarCampo(nombre,"nombre");
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = validarCampo(apellido,"apellido");
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
        this.email = validarEmail(email);
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

    private static String validarCampo(String valor, String nombreCampo){
        if (valor == null || valor.isBlank()){
            throw new IllegalArgumentException(
                    "El campo " + nombreCampo + " es obligatorio"
            );
        }
        return valor;
    }

    private static String validarEmail(String email){
        validarCampo(email,"email");

        String formato = "^[a-zA-Z0-9_][a-zA-Z0-9._%+-]*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$";

        if (!email.matches(formato)){
            throw new IllegalArgumentException(
                    "El formato del email no es valido"
            );
        }
        return email;
    }

    @Override
    public void recibirCalificacion(int puntuacion) {
        if (puntuacion < 1 || puntuacion > 5){
            throw new CalificacionInvalidaException(
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

        this.nombre = validarCampo(nombre, "nombre");
        this.apellido = validarCampo(apellido, "apellido");
        this.dni = validarCampo(dni, "DNI");
        this.email = validarEmail(email);
        this.telefono = validarCampo(telefono, "telefono");
        this.password = validarCampo(password, "password");

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
