package menus;

import excepciones.AccesoDatosException;
import excepciones.ElementoNoEncontradoException;
import excepciones.PasajeroInvalidoException;
import gestores.GestorUsuario;
import modelo.Cliente;
import modelo.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuAdministrador {

    private Scanner scanner = new Scanner(System.in);
    private GestorUsuario gestorUsuario;

    public MenuAdministrador(GestorUsuario gestorUsuario) {
        this.gestorUsuario = gestorUsuario;
    }

    public void mostrarMenu() {

        int opcion = 0;

        while (opcion != 6) {

            System.out.println("\n===== MENU ADMINISTRADOR =====");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Buscar usuario por ID");
            System.out.println("3. Registrar cliente");
            System.out.println("4. Modificar usuario");
            System.out.println("5. Desactivar usuario");
            System.out.println("6. Volver al menu principal");
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
                    listarUsuarios();
                    break;
                case 2:
                    buscarUsuario();
                    break;
                case 3:
                    registrarCliente();
                    break;
                case 4:
                    modificarUsuario();
                    break;
                case 5:
                    desactivarUsuario();
                    break;
                case 6:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    private void listarUsuarios() {

        List<Usuario> usuarios = gestorUsuario.listarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {

            System.out.println("\n===== LISTADO DE USUARIOS =====");

            for (Usuario usuario : usuarios) {
                System.out.println(usuario);
            }
        }
    }

    private void buscarUsuario() {

        System.out.print("Ingrese el ID del usuario: ");
        int id = leerId();

        try {
            Usuario usuario = gestorUsuario.buscarUsuarioPorId(id);

            System.out.println("\n===== USUARIO ENCONTRADO =====");
            System.out.println(usuario);

        } catch (ElementoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void registrarCliente() {

        System.out.println("\n===== REGISTRAR CLIENTE =====");

        String nombre = leerCampo("Nombre: ");
        String apellido = leerCampo("Apellido: ");
        String dni = leerCampo("DNI: ");
        String email = leerCampo("Email: ");
        String telefono = leerCampo("Telefono: ");
        String password = leerCampo("Password: ");

        try {

            Cliente cliente = new Cliente(
                    nombre,
                    apellido,
                    dni,
                    email,
                    telefono,
                    password
            );

            gestorUsuario.registrarUsuario(cliente);

            System.out.println("Cliente registrado correctamente.");

        } catch (PasajeroInvalidoException e) {
            System.out.println("Datos invalidos: " + e.getMessage());

        } catch (AccesoDatosException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }

    private void modificarUsuario() {

        System.out.println("\n===== MODIFICAR USUARIO =====");

        int id = leerId();

        try {

            Usuario usuario = gestorUsuario.buscarUsuarioPorId(id);

            System.out.println("\nUsuario encontrado:");
            System.out.println(usuario);

            System.out.println("\nIngrese los nuevos datos:");

            String nombre = leerCampo("Nombre: ");
            String apellido = leerCampo("Apellido: ");
            String dni = leerCampo("DNI: ");
            String email = leerCampo("Email: ");
            String telefono = leerCampo("Telefono: ");
            String password = leerCampo("Password: ");

            boolean actualizado = gestorUsuario.modificarUsuario(
                    id,
                    nombre,
                    apellido,
                    dni,
                    email,
                    telefono,
                    password
            );

            if (actualizado) {
                System.out.println("Usuario modificado correctamente.");
            } else {
                System.out.println("No se pudo modificar el usuario.");
            }

        } catch (PasajeroInvalidoException e) {
            System.out.println("Datos invalidos: " + e.getMessage());

        } catch (ElementoNoEncontradoException e) {
            System.out.println("Usuario no encontrado: " + e.getMessage());

        } catch (AccesoDatosException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }

    private void desactivarUsuario() {

        System.out.println("\n===== DESACTIVAR USUARIO =====");

        int id = leerId();

        try {

            // Buscamos al usuario
            Usuario usuario = gestorUsuario.buscarUsuarioPorId(id);

            System.out.println("\nUsuario seleccionado:");
            System.out.println(usuario);

            // Verificamos si ya está desactivado
            if (!usuario.isActivo()) {
                System.out.println("El usuario ya se encuentra desactivado.");
                return;
            }

            // Solicitamos confirmación
            String confirmacion;

            do {
                System.out.print("\n¿Confirma la desactivacion? (S/N): ");
                confirmacion = scanner.nextLine().trim();

                if (!confirmacion.equalsIgnoreCase("S")
                        && !confirmacion.equalsIgnoreCase("N")) {

                    System.out.println("Respuesta invalida. Ingrese S o N.");
                }

            } while (!confirmacion.equalsIgnoreCase("S")
                    && !confirmacion.equalsIgnoreCase("N"));

            // Desactivamos si confirmó
            if (confirmacion.equalsIgnoreCase("S")) {

                boolean desactivado = gestorUsuario.desactivarUsuario(id);

                if (desactivado) {
                    System.out.println("Usuario desactivado correctamente.");
                } else {
                    System.out.println("No se pudo desactivar el usuario.");
                }

            } else {
                System.out.println("Operacion cancelada.");
            }

        } catch (ElementoNoEncontradoException e) {
            System.out.println("Usuario no encontrado: " + e.getMessage());

        } catch (AccesoDatosException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }

    private int leerId() {

        int id;

        while (true) {

            System.out.print("Ingrese el ID del usuario: ");

            if (scanner.hasNextInt()) {

                id = scanner.nextInt();
                scanner.nextLine();

                if (id > 0) {
                    return id;
                }

                System.out.println("El ID debe ser un numero positivo.");

            } else {

                System.out.println("Debe ingresar un ID numerico.");
                scanner.nextLine();
            }
        }
    }

    private String leerCampo(String mensaje) {

        String valor;

        do {
            System.out.print(mensaje);
            valor = scanner.nextLine().trim();

            if (valor.isEmpty()) {
                System.out.println("Este campo no puede estar vacio.");
            }

        } while (valor.isEmpty());

        return valor;
    }
}