package co.sena.mercasena.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FachadaCompraTest {

    private Inventario inventario;
    private Facturacion facturacion;
    private Notificador notificador;
    private FachadaCompra fachada;

    @BeforeEach
    void preparar() {
        inventario = new Inventario();
        inventario.agregarStock("CAFE", 10);
        inventario.agregarStock("PANELA", 5);
        facturacion = new Facturacion();
        notificador = new Notificador();
        fachada = new FachadaCompra(inventario, new Pagos(), facturacion, notificador);
    }

    @Test
    void compraExitosa() {
        ResultadoCompra r = fachada.comprar("Laura", List.of(
                new LineaCompra("CAFE", 2, new BigDecimal("18000")),
                new LineaCompra("PANELA", 3, new BigDecimal("4500"))), "TARJETA");
        assertTrue(r.exitosa());
        assertEquals("FAC-1", r.factura());
        assertEquals(0, new BigDecimal("49500").compareTo(r.total()));
        assertEquals(8, inventario.disponible("CAFE"));
        assertEquals(1, notificador.getEnviados().size());
    }

    @Test
    void sinStockNoSeCompraYNoSeTocaElInventario() {
        ResultadoCompra r = fachada.comprar("Laura", List.of(
                new LineaCompra("CAFE", 2, new BigDecimal("18000")),
                new LineaCompra("PANELA", 50, new BigDecimal("4500"))), "TARJETA");
        assertFalse(r.exitosa());
        assertEquals("SIN_STOCK", r.motivo());
        assertEquals(10, inventario.disponible("CAFE"));
    }

    @Test
    void pagoRechazado() {
        ResultadoCompra r = fachada.comprar("Laura", List.of(new LineaCompra("CAFE", 1, new BigDecimal("18000"))), "TARJETA_RECHAZADA");
        assertFalse(r.exitosa());
        assertEquals("PAGO_RECHAZADO", r.motivo());
        assertTrue(facturacion.getFacturas().isEmpty());
    }

    @Test
    void compraVacia() {
        assertThrows(IllegalArgumentException.class, () -> fachada.comprar("Laura", List.of(), "TARJETA"));
    }

    @Test
    void pagoRechazadoDebeLiberarInventario() {
        //Regla 4: Si el pago es rechazado, la compra falla con el motivo `PAGO_RECHAZADO`, **se libera todo el inventario reservado** y **no** se emite factura.
        // En @BeforeEach agregamos 10 unidades de CAFE. Si reservamos 2 y el pago falla, deben volver a ser 10.
        ResultadoCompra r = fachada.comprar("Laura",
                List.of(new LineaCompra("CAFE", 2, new BigDecimal("18000"))),
                "TARJETA_RECHAZADA");

        assertFalse(r.exitosa());
        assertEquals(10, inventario.disponible("CAFE"), "El stock de CAFE debe liberarse si el pago es rechazado");
    }

    @Test
    void pagoRechazadoNoDebeNotificarAlCliente() {
        // Regla 5: Solo se notifica al cliente cuando la compra fue exitosa**, es decir, después de cobrar y facturar. Nunca se envía "Compra confirmada" si el pago falló.
        ResultadoCompra r = fachada.comprar("Laura",
                List.of(new LineaCompra("CAFE", 1, new BigDecimal("18000"))),
                "TARJETA_RECHAZADA");

        assertFalse(r.exitosa());
        assertTrue(notificador.getEnviados().isEmpty(), "No se debe notificar al cliente si el pago fue rechazado");
    }
}
