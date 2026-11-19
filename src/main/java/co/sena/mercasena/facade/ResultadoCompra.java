package co.sena.mercasena.facade;

import java.math.BigDecimal;

public record ResultadoCompra(boolean exitosa, String factura, BigDecimal total, String motivo) {
}
