package menus;

import excepciones.ConductorInvalidoException;
import modelo.Auto;
import modelo.Conductor;
import java.util.Scanner;

import modelo.Moto;
import modelo.Vehiculo;
import repositorio.UsuarioDAO;
import repositorio.VehiculoDAO;
import java.sql.SQLException;

public class MenuConductor {

    private Conductor conductor;
    private Scanner scanner;

    public MenuConductor(Conductor conductor, Scanner scanner) {
        this.conductor = conductor;
        this.scanner = scanner;

        cargarVehiculos();
    }

    private void cargarVehiculos() {

        VehiculoDAO vehiculoDAO = new VehiculoDAO();

        try {
            for (Vehiculo vehiculo : vehiculoDAO.listarPorConductorId(
                    conductor.getIdUsuario())) {

                conductor.agregarVehiculo(vehiculo);
            }

            // Restaurar el vehículo seleccionado anteriormente
            int idVehiculoSeleccionado =
                    conductor.getIdVehiculoSeleccionadoPersistido();

            if (idVehiculoSeleccionado > 0) {
                try {
                    conductor.seleccionarVehiculo(idVehiculoSeleccionado);
                    System.out.println("Vehículo seleccionado restaurado correctamente.");
                } catch (ConductorInvalidoException e) {
                    System.out.println("No se pudo restaurar el vehículo seleccionado: "
                            + e.getMessage());
                }
            }

            System.out.println("Vehiculos cargados: "
                    + conductor.getVehiculos().size());

        } catch (SQLException e) {
            System.out.println("Error al cargar los vehiculos: "
                    + e.getMessage());
        }
    }

    public void mostrarMenu() {

        int opcion = 0;

        while (opcion != 6) {

            System.out.println("\n===== MENU CONDUCTOR =====");
            System.out.println("1. Cambiar disponibilidad");
            System.out.println("2. Consultar mis datos");
            System.out.println("3. Consultar mi vehiculo");
            System.out.println("4. Registrar vehiculo");
            System.out.println("5. Seleccionar vehiculo para trabajar");
            System.out.println("6. Cerrar sesion");
            System.out.print("Seleccione una opcion: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Debe ingresar un numero.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {

                case 1:
                    cambiarDisponibilidad();
                    break;

                case 2:
                    conductor.mostrarDatos();
                    break;

                case 3:
                    consultarVehiculo();
                    break;

                case 4:
                    registrarVehiculo();
                    break;

                case 5:
                    seleccionarVehiculo();
                    break;

                case 6:
                    System.out.println("Cerrando sesion...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    private void cambiarDisponibilidad() {

        boolean nuevaDisponibilidad = !conductor.isDisponible();

        // Para ponerse disponible debe seleccionar un vehículo
        if (nuevaDisponibilidad &&
                conductor.getVehiculoSeleccionado() == null) {

            System.out.println(
                    "No podes ponerte disponible sin seleccionar un vehiculo."
            );
            System.out.println(
                    "Primero selecciona un vehiculo en la opcion 5."
            );
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            boolean actualizado = usuarioDAO.actualizarDisponibilidad(
                    conductor.getIdUsuario(),
                    nuevaDisponibilidad
            );

            if (actualizado) {
                conductor.setDisponible(nuevaDisponibilidad);

                if (nuevaDisponibilidad) {
                    System.out.println(
                            "Ahora estas disponible para recibir viajes."
                    );
                } else {
                    System.out.println("Ahora estas fuera de servicio.");
                }

            } else {
                System.out.println(
                        "No se pudo actualizar la disponibilidad."
                );
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar la disponibilidad: " + e.getMessage()
            );
        }
    }

    private void consultarVehiculo() {

        if (conductor.getVehiculos().isEmpty()) {
            System.out.println("Todavia no tenes vehiculos registrados.");
            return;
        }

        System.out.println("\n===== MIS VEHICULOS =====");

        for (Vehiculo vehiculo : conductor.getVehiculos()) {

            System.out.println("\n-------------------------");
            System.out.println("ID del vehiculo: " + vehiculo.getIdVehiculo());

            vehiculo.mostrarDatos();
        }
    }

    private void registrarVehiculo() {

        System.out.println("\n===== REGISTRAR VEHICULO =====");

        System.out.println("1. Auto");
        System.out.println("2. Moto");

        int tipo = leerEntero("Seleccione el tipo de vehiculo: ");

        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo de vehiculo invalido.");
            return;
        }

        System.out.print("Patente: ");
        String patente = scanner.nextLine().trim();

        System.out.print("Marca: ");
        String marca = scanner.nextLine().trim();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine().trim();

        int anio = leerEntero("Año: ");

        System.out.print("Color: ");
        String color = scanner.nextLine().trim();

        try {

            Vehiculo vehiculo;

            if (tipo == 1) {

                int cantPuertas = leerEntero("Cantidad de puertas: ");

                vehiculo = new Auto(
                        patente, marca, modelo, anio, color, cantPuertas
                );

            } else {

                int cilindrada = leerEntero("Cilindrada (cc): ");

                vehiculo = new Moto(
                        patente, marca, modelo, anio, color, cilindrada
                );
            }

            VehiculoDAO vehiculoDAO = new VehiculoDAO();

            vehiculoDAO.insertar(
                    vehiculo,
                    conductor.getIdUsuario()
            );

            conductor.agregarVehiculo(vehiculo);

            System.out.println("\nVehiculo registrado correctamente.");
            System.out.println("ID asignado: " + vehiculo.getIdVehiculo());

        } catch (IllegalArgumentException e) {

            System.out.println("Datos invalidos: " + e.getMessage());

        } catch (SQLException e) {

            System.out.println("Error al registrar el vehiculo: " + e.getMessage());
        }
    }

    private int leerEntero(String mensaje) {

        while (true) {

            System.out.print(mensaje);

            if (scanner.hasNextInt()) {
                int numero = scanner.nextInt();
                scanner.nextLine();
                return numero;
            }

            System.out.println("Debe ingresar un numero.");
            scanner.nextLine();
        }
    }

    private void seleccionarVehiculo() {

        if (conductor.getVehiculos().isEmpty()) {
            System.out.println("No tenes vehiculos registrados.");
            return;
        }

        System.out.println("\n===== SELECCIONAR VEHICULO =====");

        for (Vehiculo vehiculo : conductor.getVehiculos()) {

            System.out.println(
                    "ID: " + vehiculo.getIdVehiculo()
                            + " | Patente: " + vehiculo.getPatente()
                            + " | Marca: " + vehiculo.getMarca()
                            + " | Modelo: " + vehiculo.getModelo()
                            + " | Activo: " + vehiculo.isActivo()
            );
        }

        int idVehiculo = leerEntero(
                "\nIngrese el ID del vehiculo que desea utilizar: "
        );

        try {
            conductor.seleccionarVehiculo(idVehiculo);

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            boolean actualizado = usuarioDAO.actualizarVehiculoSeleccionado(
                    conductor.getIdUsuario(),
                    conductor.getVehiculoSeleccionado().getIdVehiculo()
            );

            if (actualizado) {
                System.out.println("\nVehiculo seleccionado correctamente.");
                System.out.println(
                        "Vehiculo elegido: "
                                + conductor.getVehiculoSeleccionado().getPatente()
                );
            } else {
                System.out.println("No se pudo guardar el vehiculo seleccionado.");
            }

        } catch (ConductorInvalidoException e) {
            System.out.println("No se pudo seleccionar el vehiculo: "
                    + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error al guardar la seleccion: " + e.getMessage());
        }
    }
}