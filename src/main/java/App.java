import gestores.GestorUsuario;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        GestorUsuario gestorUsuario = new GestorUsuario();

        try {

            gestorUsuario.cargarUsuarios();

            System.out.println("Plataforma Uber iniciada correctamente.");

        } catch (SQLException e) {

            System.out.println("Error al iniciar la aplicación: "
                    + e.getMessage());

        }
    }
}