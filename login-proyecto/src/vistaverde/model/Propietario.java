package vistaverde.model;

public class Propietario {

    private String nombre;
    private int numeroCasa;
    private String telefono;
    private String correo;

    public Propietario(String nombre, int numeroCasa, String telefono, String correo) {
        this.nombre = nombre;
        this.numeroCasa = numeroCasa;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre + " — Casa " + numeroCasa;
    }
}
