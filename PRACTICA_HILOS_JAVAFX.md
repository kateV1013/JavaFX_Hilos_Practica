# Practica guiada: Hilos en JavaFX

## Pregunta de analisis

No debemos ejecutar procesos prolongados directamente dentro de los eventos de JavaFX porque esos eventos se ejecutan en el JavaFX Application Thread, que tambien se encarga de pintar la ventana, responder clics, mover controles y actualizar la interfaz.

Si dentro de un evento se ejecuta una tarea lenta, por ejemplo `Thread.sleep(5000)` o un proceso pesado, ese hilo queda ocupado y la interfaz se congela hasta que finalice la tarea. Por eso las tareas prolongadas deben ejecutarse en un hilo secundario creado con `Thread` y `Runnable`.

Cuando el hilo secundario necesita modificar controles graficos, debe usar `Platform.runLater()`, porque los controles de JavaFX solo deben actualizarse desde el JavaFX Application Thread.

## Reto final implementado

La aplicacion incluye dos procesos concurrentes independientes:

- Clientes: procesa 100 registros.
- Productos: procesa 50 registros.

Cada proceso usa un objeto `Thread` con una tarea `Runnable`, tiene su propia barra de progreso, porcentaje, estado, boton de inicio y boton de detencion. El boton de reinicio se habilita cuando no hay procesos activos.
