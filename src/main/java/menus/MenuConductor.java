package menus;

import modelo.Conductor;
import java.util.Scanner;

public class MenuConductor {

    private Conductor conductor;
    private Scanner scanner;

    public MenuConductor(Conductor conductor, Scanner scanner) {
        this.conductor = conductor;
        this.scanner = scanner;
    }

    public void mostrarMenu() {

        int opcion = 0;

        while (opcion != 4) {

            System.out.println("\n===== MENU CONDUCTOR =====");
            System.out.println("1. Cambiar disponibilidad");
            System.out.println("2. Consultar mis datos");
            System.out.println("3. Consultar mi vehiculo");
            System.out.println("4. Cerrar sesion");
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
                    System.out.println("Cerrando sesion...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    private void cambiarDisponibilidad() {

        conductor.cambiarDisponibilidad();

        if (conductor.isDisponible()) {
            System.out.println("Ahora estas disponible para recibir viajes.");
        } else {
            System.out.println("Ahora estas fuera de servicio.");
        }
    }

    private void consultarVehiculo() {

        if (conductor.getIdVehiculo() > 0) {
            System.out.println(
                    "ID de tu vehiculo: " + conductor.getIdVehiculo()
            );
        } else {
            System.out.println("Todavia no tenes un vehiculo asignado.");
        }
    }
}