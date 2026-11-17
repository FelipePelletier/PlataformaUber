package gestores;

import enums.MetodoPago;
import excepciones.EstadoPagoException;
import modelo.Pagos;
import modelo.Viaje;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorPagos {
   Scanner entrada = new Scanner(System.in);
    private final List<Pagos> pagos;

    public GestorPagos() {
        this.pagos = new ArrayList<>();
    }


    public int registrarPago(Viaje viaje) {
        Pagos nuevoPago = new Pagos(viaje);
        pagos.add(nuevoPago);
        return nuevoPago.getIdPago();
    }


    public Pagos buscarPagoPorId(int idPago) {
        for (Pagos p : pagos) {
            if (p.getIdPago() == idPago) {
                return p;
            }
        }
        throw new EstadoPagoException("No se encontró ningún pago con el ID: " + idPago);
    }

    public List<Pagos> getPagosRealizados() {
        return pagos;
    }

    public void mostrarTodosLosPagos() {
        for (Pagos p : pagos) {
            System.out.println(p.toString());
        }
    }
}