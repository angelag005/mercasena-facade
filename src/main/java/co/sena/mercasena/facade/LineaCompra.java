package co.sena.mercasena.facade;

import java.math.BigDecimal;

public record LineaCompra(String sku, int cantidad, BigDecimal precioUnitario) {
}
