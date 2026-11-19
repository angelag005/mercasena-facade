package co.sena.mercasena.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Fachada: una sola operación para todo el proceso de compra. */
public class FachadaCompra {

    private final Inventario inventario;
    private final Pagos pagos;
    private final Facturacion facturacion;
    private final Notificador notificador;

    public FachadaCompra(Inventario inventario, Pagos pagos, Facturacion facturacion, Notificador notificador) {
        this.inventario = inventario;
        this.pagos = pagos;
        this.facturacion = facturacion;
        this.notificador = notificador;
    }

    public ResultadoCompra comprar(String cliente, List<LineaCompra> lineas, String medioPago) {
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("La compra debe tener al menos una línea");
        }
        BigDecimal total = lineas.stream()
                .map(l -> l.precioUnitario().multiply(BigDecimal.valueOf(l.cantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<LineaCompra> reservadas = new ArrayList<>();
        try {
            for (LineaCompra linea : lineas) {
                inventario.reservar(linea.sku(), linea.cantidad());
                reservadas.add(linea);
            }
        } catch (IllegalStateException sinStock) {
            liberar(reservadas);
            return new ResultadoCompra(false, null, total, "SIN_STOCK");
        }

        if (!pagos.cobrar(medioPago, total)) {
            liberar(reservadas);
            return new ResultadoCompra(false, null, total, "PAGO_RECHAZADO");
        }

        String factura = facturacion.emitir(cliente, total);

        notificador.enviar(cliente, "Compra confirmada");

        return new ResultadoCompra(true, factura, total, null);
    }

    private void liberar(List<LineaCompra> reservadas) {
        for (LineaCompra linea : reservadas) {
            inventario.liberar(linea.sku(), linea.cantidad());
        }
    }
}
