package repositorio;

import excepciones.ElementoNoEncontradoException;
import modelo.Usuario;

import java.util.List;
import java.sql.SQLException;

public interface DAO <T>{

    void insertar(T objeto)throws SQLException;

    List<T> listarTodos() throws SQLException;

    T buscarPorId(int id) throws SQLException, ElementoNoEncontradoException;

    boolean desactivar(int id) throws SQLException;

    boolean actualizar (T objeto) throws SQLException;
}
