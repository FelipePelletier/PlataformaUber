
package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            System.getenv("DB_URL");

    private static final String USUARIO =
            System.getenv("DB_USER");

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection obtenerConexion() throws SQLException {

        if (URL == null || USUARIO == null || PASSWORD == null) {
            throw new SQLException(
                    "Faltan configurar las variables de entorno de MySQL."
            );
        }

        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
