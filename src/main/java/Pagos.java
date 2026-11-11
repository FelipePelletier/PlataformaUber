import java.time.LocalDateTime;

public class Pagos {
    private int identificador = 0;
    private final int idPago;
    private double monto;
    private Viaje viaje;
    private LocalDateTime fechaPago;
    private MetodoPago metodoPago;
    private EstadoPago estado;


    public Pagos() {
    }

    public Pagos(double monto, Viaje viaje, LocalDateTime fechaPago, EstadoPago estado, MetodoPago metodoPago) {
        this.idPago = identificador++;
        this.monto = monto;
        this.viaje = viaje;
        this.fechaPago = fechaPago;
        this.estado = estado;
        this.metodoPago = metodoPago;
    }


}
