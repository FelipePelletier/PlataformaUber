package modelo;

import modelo.Vehiculo;

public class Moto extends Vehiculo {

    private int cilindrada;

    // Constructor vacío
    public Moto() {
        super();
    }

    // Constructor con parámetros
    public Moto(String patente, String marca,
                String modelo, int anio,
                String color, int cilindrada) {

        super(patente, marca, modelo, anio, color);

        if (cilindrada <= 0) {
            throw new IllegalArgumentException(
                    "La cilindrada debe ser mayor a cero"
            );
        }

        this.cilindrada = cilindrada;
    }

    @Override
    public void mostrarDatos() {
        super.mostrarDatos();

        System.out.println(
                "Cilindrada: " + cilindrada + " cc"
        );
    }

    public int getCilindrada() {
        return cilindrada;
    }
}
