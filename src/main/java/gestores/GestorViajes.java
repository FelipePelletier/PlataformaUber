package gestores;

import enums.EstadoViaje;
import enums.MetodoPago;
import enums.EstadoPago;
import excepciones.ElementoNoEncontradoException;
import modelo.Cliente;
import modelo.Conductor;
import modelo.Pagos;
import modelo.Viaje;
import repositorio.Repositorio;
import repositorio.ViajeDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorViajes {

    private Repositorio<Viaje> viajes;
    private ViajeDao viajeDAO;
    private GestorPagos gestorPagos;

    public GestorViajes() {
        this.viajes=new Repositorio<>();
        this.viajeDAO= new ViajeDao();
        this.gestorPagos = new GestorPagos();
    }

    //carga viajes en bdd
    public void cargarViajes() throws SQLException {

        List<Viaje> viajesbd = viajeDAO.listarTodos();

        for (Viaje v : viajesbd) {
            viajes.agregar(v);
        }

        gestorPagos.cargarPagos();
    }
    //solicitar viaje
    public Viaje solicitarViaje(
            Cliente c,
            String origen,
            String destino,
            MetodoPago metodoPago
    ) throws SQLException {

        Viaje v = new Viaje(origen, destino, c);

        // Asociamos el método de pago al viaje.
        v.setPago(metodoPago);

        // Guardamos primero el viaje en MySQL.
        viajeDAO.insertar(v);

        // Registramos su pago en MySQL.
        gestorPagos.registrarPago(v);

        // Agregamos el viaje al repositorio en memoria.
        viajes.agregar(v);

        return v;
    }
    //buscar por id

    public Viaje buscarViajePorID(int id) {
Viaje v= viajes.buscarPorId(String.valueOf(id));

if (v== null)
{
    throw new ElementoNoEncontradoException("No se encontro un viaje con este id: "+id);

}
return v;
    }

 //estados
 public void asignarConductor(int idViaje, Conductor c) throws SQLException{

        Viaje v= buscarViajePorID(idViaje);
        v.asignarConductor(c);
        viajeDAO.actualizar(v);
 }
    public  void iniciarViaje (int idViaje) throws SQLException{

        Viaje v =buscarViajePorID(idViaje);
        v.iniciar();
        viajeDAO.actualizar(v);
    }

    public void finalizarViaje(int idViaje) throws SQLException {

        Viaje v = buscarViajePorID(idViaje);

        // Finalizamos el viaje.
        v.finalizar();

        // Guardamos el nuevo estado del viaje en MySQL.
        viajeDAO.actualizar(v);

        // Buscamos el pago asociado al viaje.
        Pagos pago = gestorPagos.buscarPagoPorViajeId(idViaje);

        // Si es efectivo y está pendiente, confirmamos el pago.
        if (pago.getMetodoPago() == MetodoPago.EFECTIVO &&
                pago.getEstado() == EstadoPago.PENDIENTE) {

            gestorPagos.procesarPago(pago.getIdPago());
        }
    }
    public  void cancelarViaje (int idViaje) throws SQLException{

        Viaje v =buscarViajePorID(idViaje);
        v.cancelar();
        viajeDAO.actualizar(v);
    }
    //listado
    public List<Viaje> listarViajes(){
        return viajes.obtenerTodos();
    }
    public List<Viaje> listarPorClientes(Cliente c){

        List<Viaje> resultado = new ArrayList<>();
 for (Viaje v: viajes.obtenerTodos())
 {
   if(v.getCliente().getIdUsuario()== c.getIdUsuario())
   {
       resultado.add(v);
   }
 }
 return resultado;
    }

    public List<Viaje> listarPorEstado(EstadoViaje e){

        List<Viaje> resultado = new ArrayList<>();
        for (Viaje v: viajes.obtenerTodos())
        {
            if(v.getEstado()==e)
            {
                resultado.add(v);
            }
        }
        return resultado;
    }
    public List<Viaje> listarPorConductores(Conductor conductor) {
        List<Viaje> resultado = new ArrayList<>();
        if (conductor == null) {
            return resultado;
        }
        for (Viaje viaje : viajes.obtenerTodos()) {
            if (viaje.getConductor() != null &&
                    viaje.getConductor().getIdUsuario() == conductor.getIdUsuario()) {
                resultado.add(viaje);
            }
        }
        return resultado;
    }

}
