package menus;

import modelo.Cliente;
import java.util.Scanner;

public class MenuCliente {

    private Scanner scanner;
    private Cliente cliente;

    public MenuCliente(Cliente cliente,Scanner scanner) {
        this.cliente = cliente;
        this.scanner = scanner;
    }

    public void mostrarMenu() {

        int opcion = 0;

        while (opcion != 4) {

            System.out.println("\n===== MENU CLIENTE =====");
            System.out.println("Bienvenido/a " + cliente.getNombre());
            System.out.println("1. Solicitar viaje");
            System.out.println("2. Consultar mis viajes");
            System.out.println("3. Mi perfil");
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
                    System.out.println("Solicitud de viajes pendiente.");
                    break;

                case 2:
                    System.out.println("Historial de viajes pendiente.");
                    break;

                case 3:
                    System.out.println("\n===== MI PERFIL =====");
                    cliente.mostrarDatos();
                    break;

                case 4:
                    System.out.println("Cerrando sesion...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }
}
