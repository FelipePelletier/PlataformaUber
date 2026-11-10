package excepciones;

public class ExcepcionBase extends RuntimeException {

    public ExcepcionBase(String message) {
        super(message);
    }

    public ExcepcionBase(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
