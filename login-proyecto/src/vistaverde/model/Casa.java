package vistaverde.model;

import java.util.ArrayList;

public class Casa {

    private int numero;
    private Propietario propietario;
    private ArrayList<Pago> pagos;

    public Casa(int numero) {
        this.numero = numero;
        this.propietario = null;
        this.pagos = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public ArrayList<Pago> getPagos() {
        return pagos;
    }

    // Verifica si la casa ya tiene registrado un pago en ese mes y año
    public boolean tienePago(int mes, int anio) {
        for (Pago p : pagos) {
            if (p.getMes() == mes && p.getAnio() == anio) {
                return true;
            }
        }
        return false;
    }

    public boolean tienePropietario() {
        return propietario != null;
    }

    public void agregarPago(Pago pago) {
        pagos.add(pago);
    }

    // Suma todos los pagos registrados de la casa
    public double getTotalPagado() {
        double total = 0;
        for (Pago p : pagos) {
            total += p.getMonto();
        }
        return total;
    }

    // Retorna los números de mes que ya fueron pagados en un año dado
    public ArrayList<Integer> getMesesPagados(int anio) {
        ArrayList<Integer> meses = new ArrayList<>();
        for (Pago p : pagos) {
            if (p.getAnio() == anio) {
                meses.add(p.getMes());
            }
        }
        return meses;
    }

    // Retorna los meses que faltan pagar desde enero hasta mesActual
    public ArrayList<Integer> getMesesPendientes(int mesActual, int anio) {
        ArrayList<Integer> pendientes = new ArrayList<>();
        for (int m = 1; m <= mesActual; m++) {
            if (!tienePago(m, anio)) {
                pendientes.add(m);
            }
        }
        return pendientes;
    }

    // Total pagado solo en un año específico
    public double getTotalPagadoAnio(int anio) {
        double total = 0;
        for (Pago p : pagos) {
            if (p.getAnio() == anio) {
                total += p.getMonto();
            }
        }
        return total;
    }
}
