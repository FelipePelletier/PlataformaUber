package modelo;

import interfaces.Calificable;
import excepciones.CalificacionInvalidaException;
import interfaces.Identificable;

public abstract class Usuario implements Calificable, Identificable {

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
    private boolean sesionIniciada;

    public Usuario(String nombre, String apellido, String dni, String email, String telefono, String password) {

        this.nombre = validarNombre(nombre,"nombre");
        this.apellido = validarNombre(apellido,"apellido");
        this.dni = validarDni(dni);
        this.email = validarEmail(email);
        this.telefono = validarTelefono(telefono);
        this.password = validarPassword(password);

        this.activo = true;
        this.calificacionPromedio = 0.0;
        this.cantidadCalificaciones = 0;
        this.sesionIniciada = false;
    }

    public void asignarIdUsuario(int idUsuario){
        if (idUsuario <= 0){
            throw new IllegalArgumentException("El ID debe ser mayor a cero");
        }

        this.idUsuario = idUsuario;
    }

    public void cargarDatosPersistidos(boolean activo, double calificacionPromedio, int cantidadCalificaciones) {
        this.activo = activo;
        this.calificacionPromedio = calificacionPromedio;
        this.cantidadCalificaciones = cantidadCalificaciones;
    }

    @Override
    public String getId() {
        return String.valueOf(idUsuario);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarNombre(nombre,"nombre");
    }

    public String getPassword() {
        return password;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = validarNombre(apellido,"apellido");
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = validarDni(dni);
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
        this.telefono = validarTelefono(telefono);
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

    public boolean isSesionIniciada() {
        return sesionIniciada;
    }

    private static String validarCampo(String valor, String nombreCampo){
        if (valor == null || valor.isBlank()){
            throw new IllegalArgumentException(
                    "El campo " + nombreCampo + " es obligatorio"
            );
        }
        return valor;
    }

    private static String validarDni(String dni){

        validarCampo(dni, "DNI");

        if (!dni.matches("\\d{7,8}")){
            throw new IllegalArgumentException(
                    "El DNI debe contener entre 7 y 8 números"
            );
        }

        return dni;
    }

    private static String validarTelefono(String telefono) {

        validarCampo(telefono, "telefono");

        if (!telefono.matches("\\d{10,15}")) {
            throw new IllegalArgumentException(
                    "El telefono debe contener entre 10 y 15 numeros"
            );
        }

        return telefono;
    }

    private static String validarNombre(String nombre, String nombreCampo) {

        validarCampo(nombre, nombreCampo);

        if (!nombre.matches("[\\p{L} ]+")) {
            throw new IllegalArgumentException(
                    "El campo " + nombreCampo + " solo puede contener letras"
            );
        }

        return nombre.trim();
    }

    private static String validarPassword(String password) {

        validarCampo(password, "password");

        if (password.length() < 8) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres"
            );
        }

        return password;
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
        sesionIniciada = false;
    }

    public boolean iniciarSesion(String email,String password){

        boolean credencialesValidas = activo
                && this.email != null
                && this.password != null
                && this.email.equals(email)
                && this.password.equals(password);

        if (credencialesValidas){
            sesionIniciada = true;
        }

        return credencialesValidas;
    }

    public void modificarDatos(String nombre, String apellido, String dni,
                               String email, String telefono, String password) {

        String nuevoNombre = validarNombre(nombre, "nombre");
        String nuevoApellido = validarNombre(apellido, "apellido");
        String nuevoDni = validarDni(dni);
        String nuevoEmail = validarEmail(email);
        String nuevoTelefono = validarTelefono(telefono);
        String nuevoPassword = validarPassword(password);

        this.nombre = nuevoNombre;
        this.apellido = nuevoApellido;
        this.dni = nuevoDni;
        this.email = nuevoEmail;
        this.telefono = nuevoTelefono;
        this.password = nuevoPassword;
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
