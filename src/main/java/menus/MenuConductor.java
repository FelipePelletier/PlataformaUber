package menus;

import enums.EstadoViaje;
import excepciones.ConductorInvalidoException;
import excepciones.ElementoNoEncontradoException;
import excepciones.ViajeInvalidoException;
import gestores.GestorViajes;
import modelo.*;

import java.util.List;
import java.util.Scanner;

import repositorio.UsuarioDAO;
import repositorio.VehiculoDAO;
import java.sql.SQLException;

public class MenuConductor {

    private Conductor conductor;
    private Scanner scanner;
    private GestorViajes gestorViajes;

    public MenuConductor(Conductor conductor, Scanner scanner,GestorViajes gestorViajes) {
        this.conductor = conductor;
        this.scanner = scanner;
this.gestorViajes=gestorViajes;
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

        while (opcion != 11) {

            System.out.println("\n===== MENU CONDUCTOR =====");
            System.out.println("1. Cambiar disponibilidad");
            System.out.println("2. Consultar mis datos");
            System.out.println("3. Consultar mi vehiculo");
            System.out.println("4. Registrar vehiculo");
            System.out.println("5. Seleccionar vehiculo para trabajar");
            System.out.println("6. Ver viajes disponibles (Pendientes)");
            System.out.println("7. Aceptar un viaje");
            System.out.println("8. Iniciar viaje");
            System.out.println("9. Finalizar viaje");
            System.out.println("10. Consultar mis viajes");
            System.out.println("11. Cerrar sesion");
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
                    verViajesDisponibles();
                    break;
                case 7:
                    aceptarViaje();
                    break;
                case 8:
                    iniciarViaje();
                    break;
                case 9:
                    finalizarViaje();
                    break;
                case 10:
                    consultarMisViajes();
                    break;
                case 11:
                    System.out.println("cerrando sesion...");
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

    private void verViajesDisponibles() {
        System.out.println("\n===== VIAJES DISPONIBLES =====");
        List<Viaje> pendientes = gestorViajes.listarPorEstado(EstadoViaje.PENDIENTE);
        if (pendientes.isEmpty()) {
            System.out.println("No hay viajes pendientes en este momento.");
        } else {
            for (Viaje v : pendientes) {
                v.mostrarDatos();
            }
        }
    }

    private void aceptarViaje() {
        if (!conductor.isDisponible()) {
            System.out.println("Debes estar disponible para aceptar un viaje.");
            return;
        }
        verViajesDisponibles();
        int idViaje = leerEntero("Ingrese el ID del viaje: ");

        try {
            gestorViajes.asignarConductor(idViaje, conductor);
            System.out.println("Viaje #" + idViaje + " aceptado exitosamente");
        } catch (ElementoNoEncontradoException | ViajeInvalidoException | ConductorInvalidoException e) {
            System.out.println(" ERROR al aceptar el viaje: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR en la base de datos: " + e.getMessage());
        }
    }

    private void iniciarViaje() {
        int idViaje = leerEntero("Ingrese el ID del viaje:");

        try {
            Viaje v = gestorViajes.buscarViajePorID(idViaje);
            if (v.getConductor() != conductor.getIdUsuario()) {
                System.out.println("Este viaje no esta asignado a vos");
                return;
            }
            gestorViajes.iniciarViaje(idViaje);
            System.out.println("Viaje #" + idViaje + " iniciado");
        } catch (ElementoNoEncontradoException | ViajeInvalidoException e) {
            System.out.println("ERROR al iniciar el viaje: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR en la base de datos: " + e.getMessage());
        }
    }

    private void finalizarViaje() {
        int idViaje = leerEntero("Ingrese el ID del viaje a finalizar: ");

        try {
            Viaje v = gestorViajes.buscarViajePorID(idViaje);
            if (v.getConductor() != conductor.getIdUsuario()) {
                System.out.println("Este viaje no esta asignado a vos.");
                return;
            }
            gestorViajes.finalizarViaje(idViaje);
            System.out.println("Viaje #" + idViaje + " finalizado exitosamente.");
        } catch (ElementoNoEncontradoException | ViajeInvalidoException e) {
            System.out.println("ERROR al finalizar el viaje: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR en la base de datos: " + e.getMessage());
        }
    }

    private void consultarMisViajes() {
        System.out.println("\n===== MIS VIAJES =====");
        List<Viaje> misViajes = gestorViajes.listarPorConductores(conductor);
        if (misViajes.isEmpty()) {
            System.out.println("No has realizado viajes aun");
        } else {
            for (Viaje v : misViajes) {
                v.mostrarDatos();
            }
        }
    }


}