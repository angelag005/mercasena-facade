package co.sena.mercasena.facade;

import java.util.HashMap;
import java.util.Map;

/** Subsistema de inventario (simulado en memoria). */
public class Inventario {

    private final Map<String, Integer> stock = new HashMap<>();

    public void agregarStock(String sku, int cantidad) {
        stock.merge(sku, cantidad, Integer::sum);
    }

    public int disponible(String sku) {
        return stock.getOrDefault(sku, 0);
    }

    public void reservar(String sku, int cantidad) {
        int actual = disponible(sku);
        if (actual < cantidad) {
            throw new IllegalStateException("Stock insuficiente para " + sku);
        }
        stock.put(sku, actual - cantidad);
    }

    public void liberar(String sku, int cantidad) {
        stock.merge(sku, cantidad, Integer::sum);
    }
}
