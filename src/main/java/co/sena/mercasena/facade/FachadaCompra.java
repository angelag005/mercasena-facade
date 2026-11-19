package co.sena.mercasena.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Fachada: una sola operación para todo el proceso de compra. */
public class FachadaCompra {

    private static final String ERROR_LINEAS_VACIAS = "La compra debe tener al menos una línea";
    private static final String MOTIVO_SIN_STOCK = "SIN_STOCK";
    private static final String MOTIVO_PAGO_RECHAZADO = "PAGO_RECHAZADO";
    private static final String MENSAJE_CONFIRMACION = "Compra confirmada";

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
        validarLineasNoVacias(lineas);
        BigDecimal total = calcularTotal(lineas);

        List<LineaCompra> reservadas = new ArrayList<>();
        if (!intentarReservarStock(lineas, reservadas)) {
            return ResultadoCompraFallida(total, MOTIVO_SIN_STOCK);
        }

        if (!procesarPago(medioPago, total, reservadas)) {
            return ResultadoCompraFallida(total, MOTIVO_PAGO_RECHAZADO);
        }

        return finalizarCompraExitosa(cliente, total);
    }

    private void validarLineasNoVacias(List<LineaCompra> lineas) {
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException(ERROR_LINEAS_VACIAS);
        }
    }

    private BigDecimal calcularTotal(List<LineaCompra> lineas) {
        return lineas.stream()
                .map(linea -> linea.precioUnitario().multiply(BigDecimal.valueOf(linea.cantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean intentarReservarStock(List<LineaCompra> lineas, List<LineaCompra> reservadas) {
        try {
            for (LineaCompra linea : lineas) {
                inventario.reservar(linea.sku(), linea.cantidad());
                reservadas.add(linea);
            }
            return true;
        } catch (IllegalStateException sinStock) {
            liberar(reservadas);
            return false;
        }
    }

    private boolean procesarPago(String medioPago, BigDecimal total, List<LineaCompra> reservadas) {
        if (!pagos.cobrar(medioPago, total)) {
            liberar(reservadas);
            return false;
        }
        return true;
    }

    private ResultadoCompra finalizarCompraExitosa(String cliente, BigDecimal total) {
        String factura = facturacion.emitir(cliente, total);
        notificador.enviar(cliente, MENSAJE_CONFIRMACION);
        return new ResultadoCompra(true, factura, total, null);
    }

    private ResultadoCompra ResultadoCompraFallida(BigDecimal total, String motivo) {
        return new ResultadoCompra(false, null, total, motivo);
    }

    private void liberar(List<LineaCompra> reservadas) {
        for (LineaCompra linea : reservadas) {
            inventario.liberar(linea.sku(), linea.cantidad());
        }
    }
}