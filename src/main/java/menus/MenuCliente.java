
package menus;

import enums.MetodoPago;
import enums.EstadoViaje;

import excepciones.ElementoNoEncontradoException;
import excepciones.ViajeInvalidoException;
import excepciones.CalificacionInvalidaException;

import gestores.GestorViajes;
import gestores.GestorCalificaciones;

import modelo.Cliente;
import modelo.Viaje;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuCliente {

    private final Scanner scanner;
    private final Cliente cliente;
    private final GestorViajes gestorViajes;
    private final GestorCalificaciones gestorCalificaciones;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public MenuCliente(Cliente cliente, Scanner scanner,
                       GestorViajes gestorViajes) {

        this.cliente = cliente;
        this.scanner = scanner;
        this.gestorViajes = gestorViajes;
        this.gestorCalificaciones = new GestorCalificaciones();
    }

    public void mostrarMenu() {

        int opcion = 0;

        while (opcion != 6) {

            System.out.println("\n========== MENU CLIENTE ==========");
            System.out.println("Bienvenido/a " + cliente.getNombre());
            System.out.println("----------------------------------");
            System.out.println("1. Solicitar viaje");
            System.out.println("2. Consultar mis viajes");
            System.out.println("3. Mi perfil");
            System.out.println("4. Cancelar un viaje");
            System.out.println("5. Calificar un viaje finalizado");
            System.out.println("6. Cerrar sesion");
            System.out.println("----------------------------------");

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
                    solicitarViaje();
                    break;

                case 2:
                    consultarMisViajes();
                    break;

                case 3:
                    System.out.println("\n========== MI PERFIL ==========");
                    cliente.mostrarDatos();
                    break;

                case 4:
                    cancelarViaje();
                    break;

                case 5:
                    calificarViaje();
                    break;

                case 6:
                    System.out.println("Cerrando sesion...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    private void mostrarViaje(Viaje viaje) {

        System.out.println("\n----------------------------------");
        System.out.println("ID DEL VIAJE: " + viaje.getIdViaje());
        System.out.println("ORIGEN: " + viaje.getOrigen());
        System.out.println("DESTINO: " + viaje.getDestino());

        if (viaje.getFechaHora() != null) {
            System.out.println(
                    "FECHA Y HORA: "
                            + viaje.getFechaHora().format(FORMATO_FECHA)
            );
        } else {
            System.out.println("FECHA Y HORA: No disponible");
        }

        System.out.printf("PRECIO: $%.2f%n", viaje.getPrecio());
        System.out.println("ESTADO: " + viaje.getEstado());
        System.out.println(
                "METODO DE PAGO: "
                        + (viaje.getPago() != null
                        ? viaje.getPago()
                        : "No especificado")
        );

        System.out.println(
                "CONDUCTOR: "
                        + (viaje.getConductor() != null
                        ? viaje.getConductor().getNombre()
                        : "Todavia no asignado")
        );

        System.out.println("----------------------------------");
    }

    private void solicitarViaje() {

        System.out.println("\n========== SOLICITAR VIAJE ==========");

        System.out.print("Ingrese el origen: ");
        String origen = scanner.nextLine();

        System.out.print("Ingrese el destino: ");
        String destino = scanner.nextLine();

        System.out.println("\nELIJA EL METODO DE PAGO:");
        System.out.println("1 - EFECTIVO");
        System.out.println("2 - TARJETA");
        System.out.println("3 - MERCADO PAGO");

        if (!scanner.hasNextInt()) {
            System.out.println("Debe ingresar un numero.");
            scanner.nextLine();
            return;
        }

        int opcion = scanner.nextInt();
        scanner.nextLine();

        MetodoPago metodoPago;

        switch (opcion) {
            case 1:
                metodoPago = MetodoPago.EFECTIVO;
                break;

            case 2:
                metodoPago = MetodoPago.TARJETA;
                break;

            case 3:
                metodoPago = MetodoPago.MERCADO_PAGO;
                break;

            default:
                System.out.println("Ingrese un metodo de pago valido.");
                return;
        }

        try {

            Viaje v = gestorViajes.solicitarViaje(
                    cliente, origen, destino, metodoPago
            );

            System.out.println("\n¡VIAJE SOLICITADO EXITOSAMENTE!");
            mostrarViaje(v);

        } catch (ViajeInvalidoException e) {

            System.out.println("ERROR de validacion: " + e.getMessage());

        } catch (SQLException e) {

            System.out.println("ERROR al guardar el viaje: " + e.getMessage());
        }
    }

    private void consultarMisViajes() {

        System.out.println("\n========== MIS VIAJES ==========");

        List<Viaje> misViajes =
                gestorViajes.listarPorClientes(cliente);

        if (misViajes.isEmpty()) {

            System.out.println("No tenes viajes registrados.");

        } else {

            for (Viaje v : misViajes) {
                mostrarViaje(v);
            }
        }
    }

    private void calificarViaje() {

        System.out.println("\n========== CALIFICAR CONDUCTOR ==========");

        List<Viaje> misViajes =
                gestorViajes.listarPorClientes(cliente);

        boolean hayFinalizados = false;

        for (Viaje v : misViajes) {

            if (v.getEstado() == EstadoViaje.FINALIZADO) {

                mostrarViaje(v);
                hayFinalizados = true;
            }
        }

        if (!hayFinalizados) {

            System.out.println(
                    "No tenes viajes finalizados para calificar."
            );

            return;
        }

        System.out.print("\nIngrese el ID del viaje: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Debe ingresar un ID numerico.");
            scanner.nextLine();
            return;
        }

        int idViaje = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Ingrese una puntuacion (1 a 5): ");

        if (!scanner.hasNextInt()) {
            System.out.println("La puntuacion debe ser numerica.");
            scanner.nextLine();
            return;
        }

        int puntuacion = scanner.nextInt();
        scanner.nextLine();

        if (puntuacion < 1 || puntuacion > 5) {

            System.out.println(
                    "La puntuacion debe estar entre 1 y 5."
            );

            return;
        }

        System.out.print("Ingrese un comentario (opcional): ");
        String comentario = scanner.nextLine();

        try {

            Viaje viaje =
                    gestorViajes.buscarViajePorID(idViaje);

            if (viaje.getCliente().getIdUsuario()
                    != cliente.getIdUsuario()) {

                System.out.println(
                        "Ese viaje no pertenece a tu cuenta."
                );

                return;
            }

            if (viaje.getEstado() != EstadoViaje.FINALIZADO) {

                System.out.println(
                        "Solo podes calificar viajes finalizados."
                );

                return;
            }

            if (viaje.getConductor() == null) {

                System.out.println(
                        "Este viaje no tiene un conductor asignado."
                );

                return;
            }

            gestorCalificaciones.calificarViaje(
                    viaje,
                    cliente,
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

            System.out.println(e.getMessage());

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar la calificacion: "
                            + e.getMessage()
            );
        }
    }

    private void cancelarViaje() {

        System.out.println("\n========== CANCELAR VIAJE ==========");

        consultarMisViajes();

        System.out.print("\nIngrese el ID del viaje a cancelar: ");

        if (!scanner.hasNextInt()) {

            System.out.println("Debe ingresar un ID numerico.");
            scanner.nextLine();
            return;
        }

        int idViaje = scanner.nextInt();
        scanner.nextLine();

        try {

            Viaje v = gestorViajes.buscarViajePorID(idViaje);

            if (v.getCliente().getIdUsuario()
                    != cliente.getIdUsuario()) {

                System.out.println(
                        "El ID debe pertenecer a uno de tus viajes."
                );

                return;
            }

            gestorViajes.cancelarViaje(idViaje);

            System.out.println(
                    "Viaje cancelado exitosamente."
            );

        } catch (ElementoNoEncontradoException |
                 ViajeInvalidoException e) {

            System.out.println(
                    "ERROR al cancelar: " + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "ERROR en la base de datos: " + e.getMessage()
            );
        }
    }
}
