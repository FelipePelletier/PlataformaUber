package excepciones;

public class PagoNoConfirmadoException extends RuntimeException {
    public PagoNoConfirmadoException(String message) {
        super(message);
    }
}
