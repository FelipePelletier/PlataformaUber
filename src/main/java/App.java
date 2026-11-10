
import gestores.GestorUsuario;
import modelo.Usuario;
import excepciones.ElementoNoEncontradoException;
import excepciones.AccesoDatosException;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        GestorUsuario gestorUsuario = new GestorUsuario();

        try {

            // 1. Cargamos los usuarios desde MySQL
            gestorUsuario.cargarUsuarios();

            System.out.println("Usuarios cargados: "
                    + gestorUsuario.listarUsuarios().size());

            // 2. Buscamos a Carlos
            Usuario usuario = gestorUsuario.buscarUsuarioPorId(5);

            System.out.println("\nUsuario antes de desactivar:");
            usuario.mostrarDatos();

            // 3. Intentamos desactivar a Carlos
            boolean desactivado = gestorUsuario.desactivarUsuario(5);

            if (desactivado) {

                System.out.println("\nUsuario desactivado correctamente.");

            } else {

                System.out.println("\nEl usuario ya estaba desactivado.");

            }

            // 4. Consultamos el estado en memoria
            System.out.println("\nUsuario después de la operación:");
            gestorUsuario.buscarUsuarioPorId(5).mostrarDatos();

        } catch (ElementoNoEncontradoException e) {

            System.out.println("Usuario no encontrado: " + e.getMessage());

        } catch (AccesoDatosException e) {

            System.out.println("Error de acceso a datos: " + e.getMessage());

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        }
    }
}
