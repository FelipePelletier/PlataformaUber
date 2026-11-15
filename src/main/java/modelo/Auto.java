package modelo;

public class Auto extends Vehiculo {

    private int cantPuertas;

    // Constructor vacío
    public Auto() {
        super();
    }

    // Constructor con parámetros
    public Auto(String patente, String marca, String modelo, int anio, String color, int cantPuertas) {

        super(patente, marca, modelo, anio, color);

        if (cantPuertas < 2 || cantPuertas > 5) {
            throw new IllegalArgumentException(
                    "Cantidad de puertas inválida"
            );
        }

        this.cantPuertas = cantPuertas;
    }

    @Override
    public void mostrarDatos() {
        super.mostrarDatos();

        System.out.println(
                "Cantidad de puertas: " + cantPuertas
        );
    }

    public int getCantPuertas() {
        return cantPuertas;
    }
}
