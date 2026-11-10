public class ExcepcionBase extends RuntimeException {

    private String mensaje;

    public ExcepcionBase(String message)
    {
        super(message);
        this.mensaje=message;
    }

    public String getMensaje() {
        return mensaje;
    }
}
