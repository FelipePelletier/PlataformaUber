package excepciones;

public class AccesoDatosException extends ExcepcionBase {

    public AccesoDatosException(String message) {
        super(message);
    }

    public AccesoDatosException(String mensaje, Throwable causa){
        super(mensaje,causa);
    }
}
