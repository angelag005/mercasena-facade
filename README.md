# MercaSENA · Patrón Facade (Estructural)

**Dominio:** Proceso de compra que coordina inventario, pagos, facturación y notificaciones

## Contexto

Comprar en MercaSENA exige coordinar cuatro subsistemas: **inventario** (reservar unidades), **pagos** (cobrar), **facturación** (emitir la factura) y **notificaciones** (avisar al cliente). Si el controlador web hablara con los cuatro directamente, la lógica se duplicaría y sería fácil equivocarse en el orden. La **Fachada** ofrece una sola operación: `comprar(...)`.

Clases principales:

- `FachadaCompra`: la fachada.
- Subsistemas: `Inventario`, `Pagos`, `Facturacion` y `Notificador`. En esta práctica son simulaciones en memoria.
- `LineaCompra` y `ResultadoCompra`: datos de entrada y de salida.

## Reglas de negocio

1. El total es la suma de `precio unitario × cantidad` de cada línea.
2. **Orden obligatorio:** (a) reservar el inventario de todas las líneas, (b) cobrar, (c) emitir la factura y (d) notificar al cliente.
3. Si alguna línea no tiene stock suficiente, la compra falla con el motivo `SIN_STOCK` y **se liberan** las reservas que ya se hubieran hecho.
4. Si el pago es rechazado, la compra falla con el motivo `PAGO_RECHAZADO`, **se libera todo el inventario reservado** y **no** se emite factura.
5. **Solo se notifica al cliente cuando la compra fue exitosa**, es decir, después de cobrar y facturar. Nunca se envía "Compra confirmada" si el pago falló.
6. Una compra sin líneas lanza `IllegalArgumentException`.
7. En la simulación, el medio de pago `"TARJETA_RECHAZADA"` siempre es rechazado.

## Tu misión

Este proyecto implementa el patrón **Facade** dentro de la tienda en línea **MercaSENA**. El código *compila*, pero tiene **errores sembrados**: algunos los detectan las pruebas que ya existen y otros **no**. Tu trabajo es encontrarlos todos, demostrarlos con pruebas y corregirlos con ayuda de la IA, sin perder el control técnico.

> La fuente de verdad son las **reglas de negocio** de este README. La IA no las conoce si no se las das.

## Protocolo de trabajo (obligatorio)

1. **Prepara el repositorio.** Crea un repositorio en GitHub con este proyecto y haz el primer commit sin cambios: `git commit -m "chore: estado inicial"`.
2. **Predice sin IA (20 min).** Lee este README y el código. Escribe tus hipótesis en `BITACORA_IA.md`, sección 1.
3. **Ejecuta las pruebas.** Corre `mvn test` o las pruebas desde IntelliJ / Eclipse. Anota cuáles fallan.
4. **Demuestra cada error con una prueba.** Por cada error que encuentres, incluidos los que las pruebas actuales no detectan, escribe primero una prueba que **falle**. Haz commit: `test: demuestra <error>`.
5. **Consulta al jurado de IAs.** Envía el mismo prompt a ChatGPT, Gemini y DeepSeek. Usa el prompt de 5 piezas: contexto, tarea, restricciones, formato y criterio de verificación. Registra en la bitácora en qué coincidieron y en qué no.
6. **Corrige.** Aplica la corrección que **tú** entiendes y puedes explicar. La prueba debe pasar. Haz commit: `fix: corrige <error>`.
7. **Refactoriza con IA.** Pide a la IA una mejora de legibilidad que no cambie el comportamiento. Todas las pruebas deben seguir en verde. Haz commit: `refactor: ...`.
8. **Revisa la seguridad.** Pregúntate qué podría hacer un usuario malicioso con este código y anota tu respuesta en la bitácora.
9. **Prepara la defensa.** El instructor te pedirá explicar, **sin IA**, cualquier línea que hayas cambiado.

## Cómo ejecutar

```bash
mvn test
```

Requisitos: JDK 17 o superior y Maven 3.9 o superior, o el Maven que trae IntelliJ IDEA o Eclipse.

## Qué NO hacer

- No borres ni debilites pruebas para que pasen.
- No pegues el proyecto completo en una IA sin explicarle las reglas de negocio.
- No aceptes una corrección que no sabes explicar.

## Referencia del patrón

- https://refactoring.guru/es/design-patterns/facade
