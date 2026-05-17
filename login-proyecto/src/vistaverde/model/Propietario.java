package vistaverde.model;

public class Propietario {

    private String nombres;
    private String apellidos;
    private int numeroCasa;
    private String telefono;
    private String correo;

    public Propietario(String nombres, String apellidos, int numeroCasa, String telefono, String correo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numeroCasa = numeroCasa;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    // Devuelve nombre completo (nombres + apellidos)
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    // Mantengo getNombre() por compatibilidad con codigo antiguo
    public String getNombre() {
        return getNombreCompleto();
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " — Casa " + numeroCasa;
    }
}
