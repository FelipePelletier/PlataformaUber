import excepciones.ViajeInvalidoException;

import java.time.LocalDateTime;

public class Viaje {
    private static final double TARIFA_BASE = 2000;
    private static final double RECARGO_NOCTURNO = 0.25;
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

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Conductor getConductor() {
        return conductor;
    }


    public EstadoViaje getEstado() {
        return estado;
    }


    public void asignarConductor(Conductor conductor)
    {
        if (estado!= EstadoViaje.SOLICITADO)
        {
            throw new ViajeInvalidoException("solo se puede asignar un conductor a un viaje solicitado");
        }
        if(conductor==null)
        {
            throw new ViajeInvalidoException("el conductor es obligatorio no puede estar vacio");
        }
        this.conductor=conductor;
        this.estado=EstadoViaje.ACEPTADO;
    }

    public void iniciar(){

        if(estado!= EstadoViaje.ACEPTADO)
        {
            throw new ViajeInvalidoException("Solo se permite iniciar viajes ACEPTADOS");
        }
        this.estado = EstadoViaje.EN_CURSO;
    }


    public void finalizar(){

        if(estado!= EstadoViaje.EN_CURSO)
        {
            throw new ViajeInvalidoException("Solo se permite finalizar viajes EN CURSO");
        }
        this.estado = EstadoViaje.FINALIZADO;

    }
    public void cancelar(){

        if(estado!=EstadoViaje.SOLICITADO && estado!=EstadoViaje.ACEPTADO)
        {
            throw new ViajeInvalidoException("No se permite cancelar un viaje en estado:" + estado);

        }
        this.estado= EstadoViaje.CANCELADO;
    }

    public double calcularPrecio()
    {
double total=TARIFA_BASE;
int hora=fechaHora.getHour();
if(hora>=22|| hora <6)
{
    total+=TARIFA_BASE*RECARGO_NOCTURNO;
}
return total;
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", precio=" + precio +
                ", estado=" + estado +
                ", idViaje=" + idViaje +
                '}';
    }

    public void mostrarDatos()
    {
        System.out.println(this);
    }




}
