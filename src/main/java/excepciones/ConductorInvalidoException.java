
package excepciones;

public class ConductorInvalidoException extends RuntimeException {

    public ConductorInvalidoException(String mensaje) {
        super(mensaje);
    }
}
