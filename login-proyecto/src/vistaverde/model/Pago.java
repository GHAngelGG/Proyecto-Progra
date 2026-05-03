package vistaverde.model;

public class Pago {

    private int mes;
    private int anio;
    private double monto;

    // Arreglo con los nombres de los meses, índice 0 vacío para que coincida con 1=Enero
    private static final String[] NOMBRES_MESES = {
        "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public Pago(int mes, int anio, double monto) {
        this.mes = mes;
        this.anio = anio;
        this.monto = monto;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    public double getMonto() {
        return monto;
    }

    public String getNombreMes() {
        return NOMBRES_MESES[mes];
    }

    // Versión estática para obtener el nombre desde cualquier parte sin crear un objeto
    public static String getNombreMes(int mes) {
        return NOMBRES_MESES[mes];
    }

    @Override
    public String toString() {
        return NOMBRES_MESES[mes] + " " + anio + " — Q" + String.format("%.2f", monto);
    }
}
