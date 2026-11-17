package menus;

import enums.MetodoPago;
import excepciones.ElementoNoEncontradoException;
import excepciones.ViajeInvalidoException;
import gestores.GestorViajes;
import modelo.Cliente;
import modelo.Viaje;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuCliente {

    private final Scanner scanner;
    private final Cliente cliente;
    private final GestorViajes gestorViajes;


    public MenuCliente(Cliente cliente,Scanner scanner,GestorViajes gestorViajes) {
        this.cliente = cliente;
        this.scanner = scanner;
        this.gestorViajes=gestorViajes;
    }

    public void mostrarMenu() {

        int opcion = 0;

        while (opcion != 5) {

            System.out.println("\n===== MENU CLIENTE =====");
            System.out.println("Bienvenido/a " + cliente.getNombre());
            System.out.println("1. Solicitar viaje");
            System.out.println("2. Consultar mis viajes");
            System.out.println("3. Mi perfil");
            System.out.println("4. Cancelar un viaje");
            System.out.println("5. Cerrar sesion");
            System.out.print("Seleccione una opcion: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Debe ingresar un numero.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    solicitarViaje();
                    break;

                case 2:
                    consultarMisViajes();
                    break;

                case 3:
                    System.out.println("\n===== MI PERFIL =====");
                    cliente.mostrarDatos();
                    break;
                case 4:
                    cancelarViaje();
                    break;

                case 5:
                    System.out.println("Cerrando sesion...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }
    private void solicitarViaje() {
        System.out.println("\n===== SOLICITAR VIAJE =====");
        System.out.println("INGRESE EL ORIGEN:");
        String origen = scanner.nextLine();
        System.out.println("INGRESE EL DESTINO:");
        String destino = scanner.nextLine();
        System.out.println("ELIJA EL METODO DE PAGO:");
        System.out.println("1-EFECTIVO");
        System.out.println("2-TARJETA");
        System.out.println("3-MERCADO PAGO");
        MetodoPago metodoPago;
        try {
            Viaje v = gestorViajes.solicitarViaje(cliente, origen, destino, metodoPago);
            System.out.println("Viaje solicitado exitosamente");
            v.mostrarDatos();
        } catch (ViajeInvalidoException e) {
            System.out.println("ERROR de validacion" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR al guardar el viaje" + e.getMessage());
        }
    }
        private void consultarMisViajes() {
            System.out.println("\n===== MIS VIAJES =====");
            List<Viaje> misViajes= gestorViajes.listarPorClientes(cliente);

            if(misViajes.isEmpty())
            {
                System.out.println("SIN VIAJES REGISTRADOS");
            }else {
                for (Viaje v: misViajes)
                {
                    v.mostrarDatos();
                }
            }
        }

    private void cancelarViaje() {
        System.out.println("\n===== CANCELAR VIAJE =====");
        consultarMisViajes();
        System.out.println("ingrese el id del viaje a cancelar:");

        if (!scanner.hasNextInt()) {
        System.out.println("Debe ingresar un ID numerico.");
          scanner.nextLine();
          return;}
        int idViaje = scanner.nextInt();
        scanner.nextLine();

        try{Viaje v = gestorViajes.buscarViajePorID(idViaje);
            if(v.getCliente().getIdUsuario()!=cliente.getIdUsuario())
            {
                System.out.println("El id debe pertenerce a uno de tus viajes");
                return;
            }
         gestorViajes.cancelarViaje(idViaje);
        System.out.println("Viaje cancelado exitosamente.");
    } catch(ElementoNoEncontradoException | ViajeInvalidoException e) {
                    System.out.println("ERROR al cancelar: " + e.getMessage());
             } catch (SQLException e) {
                   System.out.println("ERROR en la base de datos: " + e.getMessage());
              }
    }
   }





