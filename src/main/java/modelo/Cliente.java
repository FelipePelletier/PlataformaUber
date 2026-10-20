package modelo;
import java.util.ArrayList;
import java.util.List;
import excepciones.PasajeroInvalidoException;

public class Cliente extends Usuario{

    /*private List<Viaje> historialViajes;*/

    public Cliente(String nombre, String apellido, String dni, String email, String telefono, String password) {
        super(nombre, apellido, dni, email, telefono, password);

        /*this.historialViajes = new ArrayList<>();*/
    }

   /* public List<Viaje> verHistorialViajes(){
        return List.copyOf(historialViajes); //para que otras clases puedan consultar los viajes sin modificar la lista interna del cliente
    }*/

    /*public void agregarViaje(Viaje viaje){

        if (viaje == null){
            throw new IllegalArgumentException(
                    "El viaje no puede ser null"
            );
        }

        historialViajes.add(viaje);
    }*/

    private void validarUbicacion(String ubicacion,String nombreCampo){
        if (ubicacion == null || ubicacion.isBlank()){
            throw new PasajeroInvalidoException(
                    "El campo " + nombreCampo + " es obligatorio"
            );
        }
    }

   /* public Viaje solicitarViaje(String origen,String destino){
        validarUbicacion(origen,"origen");
        validarUbicacion(destino,"destino");

        return null;
    }*/

    /*public void cancelarViaje(Viaje viaje){
        if (viaje == null){
            throw new IllegalArgumentException(
                    "El viaje no puede ser null"
            );
        }

        viaje.cancelar();
    }*/

}
