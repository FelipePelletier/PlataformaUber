
package modelo;

import excepciones.PasajeroInvalidoException;
import enums.TipoUsuario;

public class Cliente extends Usuario {

    public Cliente(String nombre, String apellido, String dni,
                   String email, String telefono, String password) {

        super(nombre, apellido, dni, email, telefono, password);

        setTipoUsuario(TipoUsuario.CLIENTE);
    }

    public void mostrarDatos() {

        System.out.println("\n==================================");
        System.out.println("          DATOS DEL CLIENTE");
        System.out.println("==================================");
        System.out.println("Nombre: " + getNombre());
        System.out.println("Apellido: " + getApellido());
        System.out.println("DNI: " + getDni());
        System.out.println("Email: " + getEmail());
        System.out.println("Telefono: " + getTelefono());
        System.out.println("==================================");
    }

    private void validarUbicacion(String ubicacion, String nombreCampo) {

        if (ubicacion == null || ubicacion.isBlank()) {

            throw new PasajeroInvalidoException(
                    "El campo " + nombreCampo + " es obligatorio"
            );
        }
    }
}
