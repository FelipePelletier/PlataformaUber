package modelo;

import enums.TipoUsuario;

public class Administrador extends Usuario{

    public Administrador(String nombre, String apellido, String dni,
                         String email, String telefono, String password) {

        super(nombre, apellido, dni, email, telefono, password);

        setTipoUsuario(TipoUsuario.ADMINISTRADOR);
    }
}
