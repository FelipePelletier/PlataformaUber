package repositorio;

import java.util.List;
import java.sql.SQLException;

public interface DAO <T>{

    void insertar(T objeto) throws SQLException;

    List<T> listarTodos() throws SQLException;

    T buscarPorId(int id) throws SQLException;

    void desactivar(int id) throws SQLException;

    void actualizar (T objeto) throws SQLException;
}
