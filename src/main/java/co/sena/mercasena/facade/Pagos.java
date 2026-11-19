package co.sena.mercasena.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Subsistema de pagos (simulado). */
public class Pagos {

    private final List<BigDecimal> cobrosExitosos = new ArrayList<>();

    public boolean cobrar(String medioPago, BigDecimal monto) {
        if ("TARJETA_RECHAZADA".equals(medioPago)) {
            return false;
        }
        cobrosExitosos.add(monto);
        return true;
    }

    public List<BigDecimal> getCobrosExitosos() {
        return List.copyOf(cobrosExitosos);
    }
}
