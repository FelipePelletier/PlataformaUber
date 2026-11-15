package modelo;

public abstract class Vehiculo {

    private int idVehiculo;
    private String patente;
    private String marca;
    private String modelo;
    private int anio;
    private String color;
    private boolean activo;

    // Constructor vacío
    public Vehiculo() {
    }

    // Constructor con parámetros
    public Vehiculo(String patente, String marca, String modelo, int anio, String color) {

        if (patente == null || patente.isBlank()) {
            throw new IllegalArgumentException(
                    "La patente no puede estar vacía"
            );
        }

        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException(
                    "La marca no puede estar vacía"
            );
        }

        if (anio <= 2000) {
            throw new IllegalArgumentException(
                    "El año del vehículo es inválido"
            );
        }

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

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void mostrarDatos() {
        System.out.println("Patente: " + patente);
        System.out.println("Marca: " + marca);
        System.out.println("Modelo: " + modelo);
        System.out.println("Año: " + anio);
        System.out.println("Color: " + color);
        System.out.println("Activo: " + activo);
    }

    // Getter
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

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }
}
