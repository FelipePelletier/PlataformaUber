package modelo;

public abstract class Vehiculo {


    private int idVehiculo;
    private String patente;
    private String marca;
    private String modelo;
    private int anio;
    private String color;
    private boolean activo;

    public Vehiculo() {
    }

    public Vehiculo(String patente, String marca, String modelo, int anio, String color) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.activo = true;
    }

    public boolean estaDisponible() {
        return activo;
    }

    public void mostrarDatos() {
        System.out.println("ID Vehículo: " + idVehiculo);
        System.out.println("Patente: " + patente);
        System.out.println("Marca: " + marca);
        System.out.println("Modelo: " + modelo);
        System.out.println("Año: " + anio);
        System.out.println("Color: " + color);
        System.out.println("Activo: " + activo);
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public String getColor() {
        return color;
    }

    public boolean isActivo() {
        return activo;
    }


}

