package repositorio;

import interfaces.Identificable;

import java.util.ArrayList;
import java.util.List;

public class Repositorio<T extends Identificable> {

    private List<T> elementos;

    public Repositorio() {
        this.elementos = new ArrayList<>();
    }

    public void agregar(T elemento){

        if (buscarPorId(elemento.getId()) != null){
            throw new IllegalArgumentException(
                    "Ya existe un elemento con ese ID"
            );
        }

        elementos.add(elemento);
    }

    public boolean eliminar(T elemento){
        return elementos.remove(elemento);
    }

    public T buscarPorId(String id) {

        for (T elemento : elementos) {

            if (elemento.getId().equals(id)) {
                return elemento;
            }
        }

        return null;
    }


    public List<T> obtenerTodos() {
        return new ArrayList<>(elementos);
    }

    public boolean actualizar(T elemento) {

        T existente = buscarPorId(elemento.getId());

        if (existente != null){

            int indice = elementos.indexOf(existente);

            elementos.set(indice,elemento);

            return true;
        }

        return false;
    }

}
