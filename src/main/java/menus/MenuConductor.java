
package menus;

import enums.EstadoViaje;
import excepciones.*;

import gestores.GestorViajes;
import gestores.GestorCalificaciones;

import modelo.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.SQLException;

import repositorio.UsuarioDAO;
import repositorio.VehiculoDAO;

public class MenuConductor {

    private Conductor conductor;
    private Scanner scanner;
    private GestorViajes gestorViajes;
    private GestorCalificaciones gestorCalificaciones;

    public MenuConductor(Conductor conductor, Scanner scanner,
                         GestorViajes gestorViajes) {

        this.conductor = conductor;
        this.scanner = scanner;
        this.gestorViajes = gestorViajes;
        this.gestorCalificaciones = new GestorCalificaciones();

        cargarVehiculos();
    }

    // CARGAR VEHICULOS
    private void cargarVehiculos() {

        VehiculoDAO vehiculoDAO = new VehiculoDAO();

        try {

            for (Vehiculo vehiculo :
                    vehiculoDAO.listarPorConductorId(
                            conductor.getIdUsuario())) {

                conductor.agregarVehiculo(vehiculo);
            }

            int idVehiculoSeleccionado =
                    conductor.getIdVehiculoSeleccionadoPersistido();

            if (idVehiculoSeleccionado > 0) {

                try {

                    conductor.seleccionarVehiculo(
                            idVehiculoSeleccionado
                    );

                    System.out.println(
                            "Vehiculo seleccionado restaurado correctamente."
                    );

                } catch (ConductorInvalidoException e) {

                    System.out.println(
                            "No se pudo restaurar el vehiculo seleccionado: "
                                    + e.getMessage()
                    );
                }
            }

            System.out.println(
                    "Vehiculos cargados: "
                            + conductor.getVehiculos().size()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al cargar los vehiculos: " + e.getMessage()
            );
        }
    }

    // MENU PRINCIPAL DEL CONDUCTOR
    public void mostrarMenu() {

        int opcion = 0;

        while (opcion != 12) {

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
            System.out.println("11. Calificar pasajero");
            System.out.println("12. Cerrar sesion");

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
                    calificarViaje();
                    break;

                case 12:
                    System.out.println("Cerrando sesion...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    // CAMBIAR DISPONIBILIDAD
    private void cambiarDisponibilidad() {

        boolean nuevaDisponibilidad = !conductor.isDisponible();

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

                    System.out.println(
                            "Ahora estas fuera de servicio."
                    );
                }

            } else {

                System.out.println(
                        "No se pudo actualizar la disponibilidad."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar la disponibilidad: "
                            + e.getMessage()
            );
        }
    }

    // CONSULTAR VEHICULOS
    private void consultarVehiculo() {

        if (conductor.getVehiculos().isEmpty()) {

            System.out.println(
                    "Todavia no tenes vehiculos registrados."
            );

            return;
        }

        System.out.println("\n===== MIS VEHICULOS =====");

        for (Vehiculo vehiculo : conductor.getVehiculos()) {

            System.out.println("\n-------------------------");

            System.out.println(
                    "ID del vehiculo: " + vehiculo.getIdVehiculo()
            );

            vehiculo.mostrarDatos();
        }
    }

    // REGISTRAR VEHICULO
    private void registrarVehiculo() {

        System.out.println("\n===== REGISTRAR VEHICULO =====");

        System.out.println("1. Auto");
        System.out.println("2. Moto");

        int tipo = leerEntero(
                "Seleccione el tipo de vehiculo: "
        );

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

                int cantPuertas = leerEntero(
                        "Cantidad de puertas: "
                );

                vehiculo = new Auto(
                        patente, marca, modelo, anio,
                        color, cantPuertas
                );

            } else {

                int cilindrada = leerEntero(
                        "Cilindrada (cc): "
                );

                vehiculo = new Moto(
                        patente, marca, modelo, anio,
                        color, cilindrada
                );
            }

            VehiculoDAO vehiculoDAO = new VehiculoDAO();

            vehiculoDAO.insertar(
                    vehiculo,
                    conductor.getIdUsuario()
            );

            conductor.agregarVehiculo(vehiculo);

            System.out.println(
                    "\nVehiculo registrado correctamente."
            );

            System.out.println(
                    "ID asignado: " + vehiculo.getIdVehiculo()
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Datos invalidos: " + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar el vehiculo: " + e.getMessage()
            );
        }
    }

    // LEER NUMEROS ENTEROS
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

    // SELECCIONAR VEHICULO
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

            boolean actualizado =
                    usuarioDAO.actualizarVehiculoSeleccionado(
                            conductor.getIdUsuario(),
                            conductor.getVehiculoSeleccionado()
                                    .getIdVehiculo()
                    );

            if (actualizado) {

                System.out.println(
                        "\nVehiculo seleccionado correctamente."
                );

                System.out.println(
                        "Vehiculo elegido: "
                                + conductor.getVehiculoSeleccionado()
                                .getPatente()
                );

            } else {

                System.out.println(
                        "No se pudo guardar el vehiculo seleccionado."
                );
            }

        } catch (ConductorInvalidoException e) {

            System.out.println(
                    "No se pudo seleccionar el vehiculo: "
                            + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar la seleccion: " + e.getMessage()
            );
        }
    }

    // MOSTRAR LISTA DE VIAJES CON ID PRIMERO
    private void mostrarListaViajes(List<Viaje> lista, String titulo) {

        System.out.println("\n===== " + titulo + " =====");

        if (lista.isEmpty()) {

            System.out.println("No hay viajes para mostrar.");
            return;
        }

        for (Viaje v : lista) {

            System.out.println("\n-------------------------");
            System.out.println("ID del viaje: " + v.getIdViaje());
            System.out.println("Origen: " + v.getOrigen());
            System.out.println("Destino: " + v.getDestino());
            System.out.println("Precio: $" + v.getPrecio());
            System.out.println("Estado: " + v.getEstado());
            System.out.println("Metodo de pago: " + v.getPago());
        }

        System.out.println("\n-------------------------");
    }

    // VER VIAJES DISPONIBLES
    private void verViajesDisponibles() {

        List<Viaje> pendientes =
                gestorViajes.listarPorEstado(EstadoViaje.PENDIENTE);

        mostrarListaViajes(
                pendientes,
                "VIAJES DISPONIBLES"
        );
    }

    // ACEPTAR VIAJE
    private void aceptarViaje() {

        if (!conductor.isDisponible()) {

            System.out.println(
                    "Debes estar disponible para aceptar un viaje."
            );

            return;
        }

        List<Viaje> pendientes =
                gestorViajes.listarPorEstado(EstadoViaje.PENDIENTE);

        mostrarListaViajes(
                pendientes,
                "VIAJES PARA ACEPTAR"
        );

        if (pendientes.isEmpty()) {
            return;
        }

        int idViaje = leerEntero(
                "\nIngrese el ID del viaje que desea aceptar: "
        );

        try {

            gestorViajes.asignarConductor(idViaje, conductor);

            System.out.println(
                    "\nViaje #" + idViaje + " aceptado exitosamente."
            );

            consultarMisViajes();

        } catch (ElementoNoEncontradoException |
                 ViajeInvalidoException |
                 ConductorInvalidoException e) {

            System.out.println(
                    "Error al aceptar el viaje: " + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error en la base de datos: " + e.getMessage()
            );
        }
    }

    // INICIAR VIAJE
    private void iniciarViaje() {

        List<Viaje> misViajes =
                gestorViajes.listarPorConductores(conductor);

        List<Viaje> aceptados = new ArrayList<>();

        for (Viaje v : misViajes) {

            if (v.getEstado() == EstadoViaje.ACEPTADO) {
                aceptados.add(v);
            }
        }

        mostrarListaViajes(
                aceptados,
                "VIAJES ACEPTADOS"
        );

        if (aceptados.isEmpty()) {
            return;
        }

        int idViaje = leerEntero(
                "\nIngrese el ID del viaje que desea iniciar: "
        );

        try {

            Viaje v = gestorViajes.buscarViajePorID(idViaje);

            if (v.getConductor() == null ||
                    v.getConductor().getIdUsuario()
                            != conductor.getIdUsuario()) {

                System.out.println(
                        "Este viaje no esta asignado a vos."
                );

                return;
            }

            gestorViajes.iniciarViaje(idViaje);

            System.out.println(
                    "\nViaje #" + idViaje + " iniciado correctamente."
            );

            consultarMisViajes();

        } catch (ElementoNoEncontradoException |
                 ViajeInvalidoException e) {

            System.out.println(
                    "Error al iniciar el viaje: " + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error en la base de datos: " + e.getMessage()
            );
        }
    }

    // FINALIZAR VIAJE
    private void finalizarViaje() {

        List<Viaje> misViajes =
                gestorViajes.listarPorConductores(conductor);

        List<Viaje> enCurso = new ArrayList<>();

        for (Viaje v : misViajes) {

            if (v.getEstado() == EstadoViaje.EN_CURSO) {
                enCurso.add(v);
            }
        }

        mostrarListaViajes(
                enCurso,
                "VIAJES EN CURSO"
        );

        if (enCurso.isEmpty()) {
            return;
        }

        int idViaje = leerEntero(
                "\nIngrese el ID del viaje que desea finalizar: "
        );

        try {

            Viaje v = gestorViajes.buscarViajePorID(idViaje);

            if (v.getConductor() == null ||
                    v.getConductor().getIdUsuario()
                            != conductor.getIdUsuario()) {

                System.out.println(
                        "Este viaje no esta asignado a vos."
                );

                return;
            }

            gestorViajes.finalizarViaje(idViaje);

            System.out.println(
                    "\nViaje #" + idViaje + " finalizado correctamente."
            );

            consultarMisViajes();

        } catch (ElementoNoEncontradoException |
                 ViajeInvalidoException e) {

            System.out.println(
                    "Error al finalizar el viaje: " + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error en la base de datos: " + e.getMessage()
            );
        }
    }

    // CONSULTAR MIS VIAJES
    private void consultarMisViajes() {

        List<Viaje> misViajes =
                gestorViajes.listarPorConductores(conductor);

        mostrarListaViajes(
                misViajes,
                "MIS VIAJES"
        );
    }

    // CALIFICAR PASAJERO
    private void calificarViaje() {

        System.out.println("\n===== CALIFICAR PASAJERO =====");

        List<Viaje> misViajes =
                gestorViajes.listarPorConductores(conductor);

        List<Viaje> finalizados = new ArrayList<>();

        for (Viaje v : misViajes) {

            if (v.getEstado() == EstadoViaje.FINALIZADO) {
                finalizados.add(v);
            }
        }

        mostrarListaViajes(
                finalizados,
                "VIAJES FINALIZADOS"
        );

        if (finalizados.isEmpty()) {

            System.out.println(
                    "No tenes viajes finalizados para calificar."
            );

            return;
        }

        int idViaje = leerEntero(
                "\nIngrese el ID del viaje: "
        );

        int puntuacion = leerEntero(
                "Ingrese la puntuacion (1 a 5): "
        );

        if (puntuacion < 1 || puntuacion > 5) {

            System.out.println(
                    "La puntuacion debe estar entre 1 y 5."
            );

            return;
        }

        System.out.print(
                "Ingrese un comentario (opcional): "
        );

        String comentario = scanner.nextLine();

        try {

            Viaje viaje =
                    gestorViajes.buscarViajePorID(idViaje);

            if (viaje.getConductor() == null ||
                    viaje.getConductor().getIdUsuario()
                            != conductor.getIdUsuario()) {

                System.out.println(
                        "Este viaje no pertenece a tu cuenta."
                );

                return;
            }

            if (viaje.getEstado() != EstadoViaje.FINALIZADO) {

                System.out.println(
                        "Solo podes calificar viajes finalizados."
                );

                return;
            }

            gestorCalificaciones.calificarViaje(
                    viaje,
                    conductor,
                    puntuacion,
                    comentario
            );

            System.out.println(
                    "\n¡Calificacion guardada correctamente!"
            );

        } catch (CalificacionInvalidaException e) {

            System.out.println(
                    "No se pudo calificar: " + e.getMessage()
            );

        } catch (ElementoNoEncontradoException e) {

            System.out.println(
                    "ERROR: " + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "ERROR al guardar la calificacion: "
                            + e.getMessage()
            );
        } catch (AccesoDatosException e) {

        System.out.println(
                "\nNo se pudo calificar: " + e.getMessage()
        );
    }
    }
}
