
package modelo;

import enums.TipoUsuario;
import excepciones.ConductorInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class Conductor extends Usuario {

    private String numeroLicencia;
    private boolean disponible;
    private List<Vehiculo> vehiculos;
    private Vehiculo vehiculoSeleccionado;
    private int idVehiculoSeleccionadoPersistido;

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
        this.vehiculos = new ArrayList<>();
        this.vehiculoSeleccionado = null;

        setTipoUsuario(TipoUsuario.CONDUCTOR);
    }

    public void setIdVehiculoSeleccionadoPersistido(int idVehiculo) {
        this.idVehiculoSeleccionadoPersistido = idVehiculo;
    }

    public int getIdVehiculoSeleccionadoPersistido() {
        return idVehiculoSeleccionadoPersistido;
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

    // Agregar un vehículo al conductor
    public void agregarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new ConductorInvalidoException(
                    "El vehículo no puede ser nulo"
            );
        }

        vehiculos.add(vehiculo);
    }

    // Obtener todos los vehículos
    public List<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    // Buscar un vehículo por ID
    public Vehiculo buscarVehiculoPorId(int idVehiculo) {

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getIdVehiculo() == idVehiculo) {
                return vehiculo;
            }
        }

        return null;
    }

    // Seleccionar vehículo para utilizar
    public void seleccionarVehiculo(int idVehiculo) {

        Vehiculo vehiculo = buscarVehiculoPorId(idVehiculo);

        if (vehiculo == null) {
            throw new ConductorInvalidoException(
                    "El vehículo seleccionado no pertenece al conductor"
            );
        }

        if (!vehiculo.isActivo()) {
            throw new ConductorInvalidoException(
                    "El vehículo seleccionado no está disponible"
            );
        }

        this.vehiculoSeleccionado = vehiculo;
    }

    // Obtener vehículo seleccionado
    public Vehiculo getVehiculoSeleccionado() {
        return vehiculoSeleccionado;
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
        System.out.println("Cantidad de vehículos: " + vehiculos.size());

        if (vehiculoSeleccionado != null) {
            System.out.println(
                    "Vehículo seleccionado: " +
                            vehiculoSeleccionado.getPatente()
            );
        }
    }
}
