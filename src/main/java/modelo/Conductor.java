
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

    private String validarLicencia(String numeroLicencia) {

        if (numeroLicencia == null || numeroLicencia.isBlank()) {
            throw new ConductorInvalidoException(
                    "El número de licencia es obligatorio"
            );
        }

        return numeroLicencia.trim();
    }

    public void agregarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new ConductorInvalidoException(
                    "El vehículo no puede ser nulo"
            );
        }

        vehiculos.add(vehiculo);
    }

    public List<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public Vehiculo buscarVehiculoPorId(int idVehiculo) {

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getIdVehiculo() == idVehiculo) {
                return vehiculo;
            }
        }

        return null;
    }

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

    public Vehiculo getVehiculoSeleccionado() {
        return vehiculoSeleccionado;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void cambiarDisponibilidad() {
        this.disponible = !this.disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public void mostrarDatos() {

        System.out.println("\n==================================");
        System.out.println("         DATOS DEL CONDUCTOR");
        System.out.println("==================================");

        super.mostrarDatos();

        System.out.println("Número de licencia: " + numeroLicencia);
        System.out.println("Disponibilidad: " +
                (disponible ? "Disponible" : "No disponible"));
        System.out.println("Cantidad de vehículos: " + vehiculos.size());

        if (vehiculoSeleccionado != null) {
            System.out.println("Vehículo seleccionado: " +
                    vehiculoSeleccionado.getPatente());
        } else {
            System.out.println("Vehículo seleccionado: Ninguno");
        }

        System.out.println("==================================");
    }
}
