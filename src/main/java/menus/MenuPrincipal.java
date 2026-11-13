package menus;

import autenticacion.Autenticacion;
import modelo.Usuario;
import java.util.Scanner;
import gestores.GestorUsuario;
import java.sql.SQLException;

public class MenuPrincipal {

    private Scanner scanner;
    private Autenticacion autenticacion;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.autenticacion = new Autenticacion();
    }


    public void mostrarMenu() {

        int opcion = 0;

        while (opcion != 3) {

            System.out.println("\n===== PLATAFORMA UBER =====");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir");
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
                    iniciarSesion();
                    break;

                case 2:
                    System.out.println("Registro de usuarios pendiente.");
                    break;

                case 3:
                    System.out.println("Hasta pronto!");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }


    private void iniciarSesion() {

        System.out.println("\n===== INICIAR SESION =====");

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {

            Usuario usuario = autenticacion.iniciarSesion(email, password);

            if (usuario == null) {
                System.out.println("Email o contraseña incorrectos.");
                return;
            }

            System.out.println("\nBienvenido/a " + usuario.getNombre());

            switch (usuario.getTipoUsuario()) {

                case ADMINISTRADOR:

                    GestorUsuario gestorUsuario = new GestorUsuario();

                    try {
                        gestorUsuario.cargarUsuarios();

                        MenuAdministrador menuAdministrador =
                                new MenuAdministrador(gestorUsuario);

                        menuAdministrador.mostrarMenu();

                        autenticacion.cerrarSesion(usuario);

                        System.out.println("\nSesion cerrada correctamente.");

                    } catch (SQLException e) {
                        System.out.println(
                                "Error al cargar los usuarios: " + e.getMessage()
                        );
                    }

                    break;

                case CLIENTE:
                    System.out.println("Menu de cliente pendiente de implementar.");
                    break;

                case CONDUCTOR:
                    System.out.println("Menu de conductor pendiente de implementar.");
                    break;
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("No se pudo iniciar sesion: " + e.getMessage());
        }
    }


}
