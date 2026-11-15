package gestores;

import enums.MetodoPago;
import excepciones.EstadoPagoException;
import modelo.Pagos;
import modelo.Viaje;

import java.util.ArrayList;
import java.util.List;

public class GestorPagos {
    private final List<Pagos> pagosRealizados;

    public GestorPagos() {
        this.pagosRealizados = new ArrayList<>();
    }

    public void registrarYProcesarPago(double monto, Viaje viaje, MetodoPago metodoPago) {
        Pagos nuevoPago = new Pagos(monto, viaje, null, null, metodoPago);
        nuevoPago.procesarPago();
        pagosRealizados.add(nuevoPago);
    }

    public Pagos buscarPagoPorId(int idPago) {
        for (Pagos p : pagosRealizados) {
            if (p.getIdPago() == idPago) {
                return p;
            }
        }
        throw new EstadoPagoException("No se encontró ningún pago con el ID: " + idPago);
    }

    public List<Pagos> getPagosRealizados() {
        return pagosRealizados;
    }

    public void mostrarTodosLosPagos() {
        for (Pagos p : pagosRealizados) {
            System.out.println(p.toString());
        }
    }
}