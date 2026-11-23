# Bitácora de uso de IA — Patrón Facade

Aprendices: Angela Maria Garces Mosquera
Ficha: 3409871  Fecha de inicio: 12-11-2026

> La bitácora es evidencia del ítem 18 de la lista de chequeo. Regístralo **todo**, también las respuestas de la IA que estaban mal.
> Nunca pegues en una IA contraseñas, tokens, datos personales reales ni código que no tengas permiso de compartir.

## 1. Predicción sin IA (máx. 20 minutos)

Antes de abrir cualquier IA, lee el README y el código. Escribe aquí qué errores crees que hay y por qué.

| # | Archivo y línea     | ¿Qué creo que está mal?                                                                                                 | ¿Qué regla de negocio rompe? |
|---|---------------------|-------------------------------------------------------------------------------------------------------------------------|------------------------------|
| 1 | FachadaCompra 26-28 | Unicamente se suma el precioUnitario de cada línea,pero el total es la suma de precio unitario × cantidad de cada línea | Regla 1                      |
| 2 | FachadaCompra 41    | Se envia el mensaje antes de cobrar                                                                                     | Regla 2 y 5                  |
| 3 | FachadaCompra 42-44 | Retorna "PAGO_RECHAZADO" sin llamar al metodo liberar, por lo que el stock queda bloqueado                              | Regla 4                      |

## 2. Registro de interacciones con IA

Copia una fila por cada consulta importante.

| # | Herramienta (ChatGPT / Gemini / DeepSeek / Copilot / otra) | Prompt (resumen o enlace)                                      | ¿Qué respondió?                                                                                                     | ¿Era correcto? ¿Cómo lo verifiqué?       | ¿Lo usé? (sí / no / modificado)                              |
|---|------------------------------------------------------------|----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|------------------------------------------|--------------------------------------------------------------|
| 1 | Gemini                                                     | https://share.gemini.google/a22s3S8dcKX3                       | Cual eran los problemas la clase o el metodo donde se encontraban, que regla afectaba y propuestas de pruba JUnit 5 | Si, cada error era el que yo identifique | Si, para confirmar mi observacion en encontrar los problemas | 
| 2 | ChatGPT                                                    | https://chatgpt.com/share/6aff08b6-dc28-83e9-8356-4d7ed8dfa1ef | Los problemas que se encuentran en el codigo, ejemplos de prueba JUnit y una recomendacion                          | Si, cada error era el que yo identifique | Si, para confirmar mi observacion en encontrar los problema  |
| 3 | DeepSeek                                                   | https://chat.deepseek.com/share/jwvsbuvn7hlof8onkz             | Para confirmar que Gemini y CharGPT esten en lo correcto                                                            | Si, cada error era el que yo identifique | Si, para confirmar mi observacion en encontrar los problema  |

## 3. Jurado de IAs

Envía el **mismo prompt** a tres herramientas distintas y compara.

| Pregunta enviada                                                                                                | ChatGPT                    | Gemini                                                                                                                                                                                                                                                                                                                    | DeepSeek                   | ¿Quién acertó? ¿En qué discreparon?                                                                                                                                                                                                                                                   |
|-----------------------------------------------------------------------------------------------------------------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ¿Quieres que escriba la clase completa de pruebas unitarias en JUnit 5 y la versión corregida de FachadaCompra? | No genero ninguna pregunta | Generó la implementación corregida de FachadaCompra y una clase de pruebas FachadaCompraTest con JUnit 5. Incluyó pruebas para verificar el cálculo del total, el orden de ejecución de los subsistemas, la liberación del inventario cuando falta stock o el pago es rechazado y la validación de compras sin productos. | No genero ninguna pregunta | Gemini acertó al proponer pruebas para las reglas principales del README y corregir la lógica de compra. Sin embargo, habría que ejecutar las pruebas para comprobar que funcionan correctamente y verificar que los métodos utilizados coincidan con las clases reales del proyecto. |

## 4. Errores encontrados y corregidos

| # | Error                                                                                                                                | Prueba que lo demuestra (nombre del método de prueba)                             | Commit de la corrección                                                                                     | ¿Lo encontré yo, la IA o ambos? |
|---|--------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|---------------------------------|
| 1 | En FachadaCompra.comprar solo se sumaba el precioUnitario sin multiplicarlo por cantidad() (rompe la Regla 1).                       | compraExitosa() (verifica que 2 cafés de 18.000 + 3 panelas de 4.500 sumen 49500) | fix(facade): corrige orden de subsistemas, calculo del total y liberacion de inventario ante pago rechazado | Ambos                           |
| 2 | Cuando pagos.cobrar retorna false, no se invocaba liberar(reservadas), dejando el stock bloqueado (rompe la Regla 4).                | pagoRechazadoDebeLiberarInventario()                                              | fix(facade): corrige orden de subsistemas, calculo del total y liberacion de inventario ante pago rechazado | Ambos                           |
| 3 | notificador.enviar se ejecutaba antes de pagos.cobrar, enviando "Compra confirmada" aunque el pago fallara (rompe las Reglas 2 y 5). | pagoRechazadoNoDebeNotificarAlCliente()                                           | fix(facade): corrige orden de subsistemas, calculo del total y liberacion de inventario ante pago rechazado | Ambos                           |

## 5. Lo que la IA hizo mal o no vio

Como el método comprar solo recibe un String con el nombre del client, 
un usuario malicioso podría poner el nombre de otra persona o de un administrador y 
hacer compras a su nombre, porque el sistema no le pide ninguna contraseña o 
verificación para comprobar que de verdad es él.

## 6. Reflexión final (5 a 10 líneas)

¿Qué parte del trabajo aceleró la IA? ¿En qué momento decidiste **no** usarla y por qué? ¿Qué aprendiste del patrón que no sabías antes?

La IA me ayudó a acelerar muchísimo la búsqueda de errores y a entender más rápido por qué 
fallaban las reglas de negocio en el código. Sin embargo, decidí no usarla cuando tuve que 
analizar el flujo paso a paso y correr las pruebas en mi entorno, porque quería estar seguro 
de entender realmente qué estaba pasando.
Lo que entendi del patron es que pone todas clases como ventanas simplificadas en donde oculta 
la complejidad de los subsistemas y permite que el resto del código interactúe de forma directa, 
ordenada y sin complicaciones a través de un único punto de acceso.