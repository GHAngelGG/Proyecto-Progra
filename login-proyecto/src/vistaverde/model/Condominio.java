package vistaverde.model;

import java.util.ArrayList;
import java.time.LocalDate;

public class Condominio {

    public static final int TOTAL_CASAS = 30;
    public static final double CUOTA_INICIAL = 1500.00;

    private ArrayList<Casa> casas;
    private double cuotaMensual;

    public Condominio() {
        this.cuotaMensual = CUOTA_INICIAL;
        this.casas = new ArrayList<>();
        // Crea las 30 casas del condominio numeradas del 1 al 30
        for (int i = 1; i <= TOTAL_CASAS; i++) {
            casas.add(new Casa(i));
        }
    }

    public ArrayList<Casa> getCasas() {
        return casas;
    }

    // Obtiene una casa por su número (1 a 30)
    public Casa getCasa(int numero) {
        return casas.get(numero - 1);
    }

    public double getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(double cuota) {
        this.cuotaMensual = cuota;
    }

    // Registra un propietario. Retorna false si la casa ya tiene dueño
    public boolean registrarPropietario(Propietario p) {
        Casa c = getCasa(p.getNumeroCasa());
        if (c.tienePropietario()) {
            return false;
        }
        c.setPropietario(p);
        return true;
    }

    // Registra un pago. Retorna "OK" si fue exitoso o un mensaje de error
    public String registrarPago(int numeroCasa, int mes, int anio) {
        Casa c = getCasa(numeroCasa);

        if (!c.tienePropietario()) {
            return "La casa " + numeroCasa + " no tiene propietario registrado.";
        }

        if (c.tienePago(mes, anio)) {
            return "La casa " + numeroCasa + " ya tiene registrado el pago de "
                    + Pago.getNombreMes(mes) + " " + anio + ".";
        }

        // No se permiten pagos de meses futuros
        int mesActual = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();

        if (anio > anioActual || (anio == anioActual && mes > mesActual)) {
            return "No se puede registrar el pago de un mes futuro.";
        }

        // Verifica que los meses anteriores del mismo año estén pagados
        for (int m = 1; m < mes; m++) {
            if (!c.tienePago(m, anio)) {
                return "Debe registrar primero el pago de "
                        + Pago.getNombreMes(m) + " " + anio + " antes de continuar.";
            }
        }

        c.agregarPago(new Pago(mes, anio, cuotaMensual));
        return "OK";
    }

    // Devuelve las casas que no pagaron en el mes y año indicados
    public ArrayList<Casa> getCasasMorosas(int mes, int anio) {
        ArrayList<Casa> morosas = new ArrayList<>();
        for (Casa c : casas) {
            if (c.tienePropietario() && !c.tienePago(mes, anio)) {
                morosas.add(c);
            }
        }
        return morosas;
    }

    // Suma total recaudado en un mes y año específicos
    public double getTotalRecaudado(int mes, int anio) {
        double total = 0;
        for (Casa c : casas) {
            if (c.tienePago(mes, anio)) {
                total += cuotaMensual;
            }
        }
        return total;
    }

    // Lo que se espera recaudar si todas las casas pagaran
    public double getTotalEsperado() {
        return TOTAL_CASAS * cuotaMensual;
    }
}
