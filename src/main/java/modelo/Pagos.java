
package modelo;

import enums.EstadoPago;
import enums.MetodoPago;
import excepciones.DatosInvalidosException;
import excepciones.EstadoPagoException;

import java.time.LocalDateTime;

public class Pagos {

    private final int idPago;
    private double monto;
    private Viaje viaje;
    private LocalDateTime fechaPago;
    private MetodoPago metodoPago;
    private EstadoPago estado;

    // Constructores

    // Constructor para crear un pago antes de persistirlo en MySQL.
    public Pagos() {
        this.idPago = 0;
        this.fechaPago = LocalDateTime.now();
        this.estado = EstadoPago.PENDIENTE;
    }

    // Constructor para generar un pago a partir de un viaje.
    public Pagos(Viaje viaje) {

        if (viaje == null) {
            throw new DatosInvalidosException(
                    "El pago debe estar asociado a un viaje válido."
            );
        }

        this.monto = viaje.calcularPrecio();

        if (this.monto <= 0) {
            throw new DatosInvalidosException(
                    "El monto del viaje debe ser mayor a cero."
            );
        }

        this.idPago = 0;
        this.viaje = viaje;
        this.fechaPago = viaje.getFechaHora();
        this.metodoPago = viaje.getPago();

        if (this.metodoPago == null) {
            throw new DatosInvalidosException(
                    "El método de pago no puede ser nulo."
            );
        }

        if (metodoPago == MetodoPago.EFECTIVO) {
            this.estado = EstadoPago.PENDIENTE;
        } else {
            this.estado = EstadoPago.CONFIRMADO;
        }
    }

    // Constructor para recuperar un pago desde MySQL.
    public Pagos(int idPago, double monto, Viaje viaje,
                 LocalDateTime fechaPago, MetodoPago metodoPago,
                 EstadoPago estado) {

        if (idPago <= 0) {
            throw new DatosInvalidosException(
                    "El ID del pago debe ser mayor a cero."
            );
        }

        if (monto <= 0) {
            throw new DatosInvalidosException(
                    "El monto debe ser mayor a cero."
            );
        }

        if (viaje == null || fechaPago == null ||
                metodoPago == null || estado == null) {
            throw new DatosInvalidosException(
                    "Los datos del pago no pueden ser nulos."
            );
        }

        this.idPago = idPago;
        this.monto = monto;
        this.viaje = viaje;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
        this.estado = estado;
    }

    // Getters y setters

    public int getIdPago() {
        return idPago;
    }

    public double getMonto() {
        return monto;
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

    // Métodos

    public boolean procesarPago() {

        if (this.estado != EstadoPago.PENDIENTE) {
            throw new EstadoPagoException(
                    "No se puede procesar el pago #" + this.idPago +
                            " porque su estado actual es " + this.estado + "."
            );
        }

        this.estado = EstadoPago.CONFIRMADO;

        System.out.println("Su pago ha sido confirmado");

        return true;
    }

    public boolean reembolsarPago() {

        if (this.estado != EstadoPago.CONFIRMADO) {
            throw new EstadoPagoException(
                    "Acción denegada: No se puede reembolsar el pago #" +
                            this.idPago + " porque no ha sido confirmado."
            );
        }

        this.estado = EstadoPago.REEMBOLSADO;

        System.out.println("Su pago ha sido reembolsado");

        return true;
    }

    public double calcularComisionPlataforma() {
        return this.monto * 0.25;
    }

    public double calcularGananciaConductor() {
        return this.monto - calcularComisionPlataforma();
    }

    @Override
    public String toString() {
        return "Pagos{" +
                "idPago=" + idPago +
                ", monto=$" + monto +
                ", viaje=" + viaje +
                ", fechaPago=" + fechaPago +
                ", metodoPago=" + metodoPago +
                ", estado=" + estado +
                '}';
    }
}
