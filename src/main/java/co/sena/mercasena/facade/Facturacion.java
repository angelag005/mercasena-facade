package co.sena.mercasena.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Subsistema de facturación (simulado). */
public class Facturacion {

    private final List<String> facturas = new ArrayList<>();

    public String emitir(String cliente, BigDecimal total) {
        String numero = "FAC-" + (facturas.size() + 1);
        facturas.add(numero + " " + cliente + " " + total.toPlainString());
        return numero;
    }

    public List<String> getFacturas() {
        return List.copyOf(facturas);
    }
}
