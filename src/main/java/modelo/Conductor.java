
package modelo;

import enums.TipoUsuario;
import excepciones.ConductorInvalidoException;

public class Conductor extends Usuario {

    private String numeroLicencia;
    private boolean disponible;
    private int idVehiculo;

    // Constructor
    public Conductor(String nombre,
                     String apellido,
                     String dni,
                     String email,
                     String telefono,
                     String password,
                     String numeroLicencia) {

        super(nombre, apellido, dni, email, telefono, password);

        this.numeroLicencia = validarLicencia(numeroLicencia);
        this.disponible = false;
        this.idVehiculo = 0;

        setTipoUsuario(TipoUsuario.CONDUCTOR);
    }

    // Validar licencia
    private String validarLicencia(String numeroLicencia) {

        if (numeroLicencia == null || numeroLicencia.isBlank()) {
            throw new ConductorInvalidoException(
                    "El número de licencia es obligatorio"
            );
        }

        return numeroLicencia.trim();
    }

    // Asignar vehículo mediante su ID
    public void asignarVehiculo(int idVehiculo) {

        if (idVehiculo <= 0) {
            throw new ConductorInvalidoException(
                    "El ID del vehículo debe ser mayor a cero"
            );
        }

        this.idVehiculo = idVehiculo;
    }

    // Obtener ID del vehículo
    public int getIdVehiculo() {
        return idVehiculo;
    }

    // Obtener licencia
    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    // Obtener disponibilidad
    public boolean isDisponible() {
        return disponible;
    }

    // Cambiar disponibilidad
    public void cambiarDisponibilidad() {
        this.disponible = !this.disponible;
    }

    // Establecer disponibilidad
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // Mostrar datos
    @Override
    public void mostrarDatos() {

        super.mostrarDatos();

        System.out.println("Número de licencia: " + numeroLicencia);
        System.out.println("Disponible: " + disponible);
        System.out.println("ID del vehículo: " + idVehiculo);
    }
}
