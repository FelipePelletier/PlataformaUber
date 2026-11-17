package gestores;

import enums.EstadoViaje;
import enums.MetodoPago;
import excepciones.ElementoNoEncontradoException;
import modelo.Cliente;
import modelo.Conductor;
import modelo.Viaje;
import repositorio.Repositorio;
import repositorio.ViajeDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorViajes {

    private Repositorio<Viaje> viajes;
    private ViajeDao viajeDAO;

    public GestorViajes() {
        this.viajes=new Repositorio<>();
        this.viajeDAO= new ViajeDao();
    }

    //carga los viajes de la base de datos
    public void cargarViajes()throws SQLException{
        List<Viaje> viajesbd=viajeDAO.listarTodos();
         for(Viaje v : viajesbd)
         {
             viajes.agregar(v);

         }

    }
    //solicitar viaje
    public Viaje solicitarViaje(int idCliente, String origen, String destino, MetodoPago metodoPago) throws SQLException{
        GestorPagos gestorPagos= new GestorPagos();
        Viaje v= new Viaje(origen,destino,idCliente);
        viajeDAO.insertar(v);
        viajes.agregar(v);
        gestorPagos.registrarPago(v);
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
 public void asignarConductor(int idViaje, Conductor conductor) throws SQLException{

        Viaje v= buscarViajePorID(idViaje);
        v.asignarConductor(conductor);
        viajeDAO.actualizar(v);
 }
    public  void iniciarViaje (int idViaje) throws SQLException{

        Viaje v =buscarViajePorID(idViaje);
        v.iniciar();
        viajeDAO.actualizar(v);
    }

    public  void finalizarViaje (int idViaje) throws SQLException{

        Viaje v =buscarViajePorID(idViaje);
        v.finalizar();
        viajeDAO.actualizar(v);
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
    public List<Viaje> listarPorClientes(int id){

        List<Viaje> resultado = new ArrayList<>();
 for (Viaje v: viajes.obtenerTodos())
 {
   if(v.getCliente()== id)
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
            if (viaje.getConductor() == conductor.getIdUsuario()) {
                resultado.add(viaje);
            }
        }
        return resultado;
    }

}
