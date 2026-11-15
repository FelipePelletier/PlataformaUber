package excepciones;

public class EstadoPagoException extends RuntimeException {
    public EstadoPagoException(String message) {
        super(message);
    }
}
