package modelo;

import enums.EstadoPago;
import enums.MetodoPago;
import excepciones.DatosInvalidosException;
import excepciones.EstadoPagoException;

import java.time.LocalDateTime;

public class Pagos {
    private static int identificador = 0;
    private final int idPago;
    private double monto;
    private Viaje viaje;
    private LocalDateTime fechaPago;
    private MetodoPago metodoPago;
    private EstadoPago estado;


    //constructores

    public Pagos() {
        this.idPago = identificador++;
        this.fechaPago = LocalDateTime.now();
        this.estado = EstadoPago.PENDIENTE;
    }

    public Pagos(double monto, Viaje viaje, LocalDateTime fechaPago, EstadoPago estado, MetodoPago metodoPago) {
        if (monto <= 0) {
            throw new DatosInvalidosException("El monto del viaje debe ser mayor a cero.");
        }
        if (viaje == null) {
            throw new DatosInvalidosException("El pago debe estar asociado a un viaje válido.");
        }

        this.idPago = identificador++;
        this.monto = monto;
        this.viaje = viaje;
        this.fechaPago = fechaPago;
        this.estado = EstadoPago.PENDIENTE;
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

    //metodos
    public boolean procesarPago() {
        boolean exito = true;

        if (this.estado == EstadoPago.CONFIRMADO) {
            throw new EstadoPagoException("El pago con ID " + this.idPago + " ya fue confirmado anteriormente.");
        }
        if (this.estado == EstadoPago.REEMBOLSADO) {
            throw new EstadoPagoException("No se puede procesar un pago que ya fue reembolsado.");
        }
        this.estado = EstadoPago.CONFIRMADO;
        System.out.println("Su pago ha sido confirmado");
        return true;
    }

    public boolean reembolsarPago() {
        boolean exito = true;

        if (this.estado != EstadoPago.CONFIRMADO) {
            throw new EstadoPagoException("Acción denegada: No se puede reembolsar el pago #" + this.idPago +
                    " porque no ha sido confirmado");
        }
        this.estado = EstadoPago.REEMBOLSADO;
        System.out.println("Su pago ha sido reembolsado");
        return true;
    }



}
