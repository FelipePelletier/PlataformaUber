import java.time.LocalDateTime;

public class Pagos {
    private int identificador = 0;
    private final int idPago;
    private double monto;
    private Viaje viaje;
    private LocalDateTime fechaPago;
    private MetodoPago metodoPago;
    private EstadoPago estado;


    //constructores

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

    //getters y setters

    public int getIdPago() {
        return idPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }
}
