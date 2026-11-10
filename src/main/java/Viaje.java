import java.time.LocalDateTime;

public class Viaje {
    private int idViaje;
    private String origen;
    private String destino;
    private LocalDateTime fechaHora;
    private double precio;
    private EstadoViaje estado;
    private Cliente cliente;
    private Conductor conductor;

    public Viaje() {
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoViaje.SOLICITADO;
    }

    public Viaje(String origen, String destino, Cliente cliente) {
        if( origen== null || origen.isBlank()||destino==null||destino.isBlank())
        {
            throw new ViajeInvalidoException("origen y destino son obligatorios");
        }
        if(cliente==null)
        {
            throw  new ViajeInvalidoException("El viaje necesita un cliente");
        }
        this.origen = origen;
        this.destino = destino;
        this.cliente = cliente;
        this.estado = EstadoViaje.SOLICITADO;
        this.fechaHora = LocalDateTime.now();
        this.precio = calcularPrecio();
    }



}
