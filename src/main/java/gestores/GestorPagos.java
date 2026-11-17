
package gestores;

import modelo.Pagos;
import modelo.Viaje;
import repositorio.PagoDAO;

import java.util.ArrayList;
import java.util.List;

public class GestorPagos {

    private final List<Pagos> pagos;
    private final PagoDAO pagoDAO;

    public GestorPagos() {
        this.pagos = new ArrayList<>();
        this.pagoDAO = new PagoDAO();
    }

    public void cargarPagos() {
        pagos.clear();
        pagos.addAll(pagoDAO.listarTodos());
    }

    public int registrarPago(Viaje viaje) {

        // Creamos el pago a partir del viaje.
        Pagos nuevoPago = new Pagos(viaje);

        // Lo insertamos en MySQL y recuperamos su ID.
        int idGenerado = pagoDAO.insertar(nuevoPago);

        // Construimos el objeto definitivo con el ID de MySQL.
        Pagos pagoRegistrado = new Pagos(
                idGenerado,
                nuevoPago.getMonto(),
                nuevoPago.getViaje(),
                nuevoPago.getFechaPago(),
                nuevoPago.getMetodoPago(),
                nuevoPago.getEstado()
        );

        // Lo agregamos a la colección en memoria.
        pagos.add(pagoRegistrado);

        return idGenerado;
    }

    public Pagos buscarPagoPorId(int idPago) {

        for (Pagos p : pagos) {
            if (p.getIdPago() == idPago) {
                return p;
            }
        }

        // Si no está en memoria, lo buscamos en MySQL.
        Pagos pago = pagoDAO.buscarPorId(idPago);

        pagos.add(pago);

        return pago;
    }

    public Pagos buscarPagoPorViajeId(int idViaje) {

        for (Pagos p : pagos) {
            if (p.getViaje().getIdViaje() == idViaje) {
                return p;
            }
        }

        Pagos pago = pagoDAO.buscarPorViajeId(idViaje);

        pagos.add(pago);

        return pago;
    }

    public List<Pagos> getPagosRealizados() {
        return pagos;
    }

    public void mostrarTodosLosPagos() {

        for (Pagos p : pagos) {
            System.out.println(p);
        }
    }

    public void procesarPago(int idPago) {

        Pagos pago = buscarPagoPorId(idPago);

        // Actualizamos el estado del pago.
        pago.procesarPago();

        // Persistimos el cambio en MySQL.
        if (!pagoDAO.actualizarEstado(idPago, pago.getEstado())) {
            throw new IllegalStateException(
                    "No se pudo actualizar el estado del pago en la base de datos."
            );
        }
    }

    public void reembolsarPago(int idPago) {

        Pagos pago = buscarPagoPorId(idPago);

        // Actualizamos el estado del pago.
        pago.reembolsarPago();

        // Persistimos el cambio en MySQL.
        if (!pagoDAO.actualizarEstado(idPago, pago.getEstado())) {
            throw new IllegalStateException(
                    "No se pudo actualizar el estado del pago en la base de datos."
            );
        }
    }
}
