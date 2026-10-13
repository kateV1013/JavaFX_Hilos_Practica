package ni.edu.uam.practicahilos;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProcesadorController {

    @FXML
    private Label lblEstadoClientes;

    @FXML
    private Label lblPorcentajeClientes;

    @FXML
    private ProgressBar barraClientes;

    @FXML
    private Button btnIniciarClientes;

    @FXML
    private Button btnDetenerClientes;

    @FXML
    private Label lblEstadoProductos;

    @FXML
    private Label lblPorcentajeProductos;

    @FXML
    private ProgressBar barraProductos;

    @FXML
    private Button btnIniciarProductos;

    @FXML
    private Button btnDetenerProductos;

    @FXML
    private Button btnReiniciar;

    private Thread hiloClientes;
    private Thread hiloProductos;

    @FXML
    private void iniciarClientes() {
        if (estaEnEjecucion(hiloClientes)) {
            return;
        }

        prepararProceso(
                barraClientes,
                lblPorcentajeClientes,
                lblEstadoClientes,
                btnIniciarClientes,
                btnDetenerClientes,
                "Procesando clientes..."
        );

        Runnable tareaClientes = crearTareaProceso(
                100,
                100,
                barraClientes,
                lblPorcentajeClientes,
                lblEstadoClientes,
                btnIniciarClientes,
                btnDetenerClientes,
                "Proceso de clientes finalizado",
                "Proceso de clientes detenido"
        );

        hiloClientes = new Thread(tareaClientes);
        hiloClientes.setName("Hilo-Clientes");
        hiloClientes.setDaemon(true);
        hiloClientes.start();
    }

    @FXML
    private void detenerClientes() {
        detenerProceso(
                hiloClientes,
                btnDetenerClientes,
                lblEstadoClientes,
                "Solicitando detención de clientes..."
        );
    }

    @FXML
    private void iniciarProductos() {
        if (estaEnEjecucion(hiloProductos)) {
            return;
        }

        prepararProceso(
                barraProductos,
                lblPorcentajeProductos,
                lblEstadoProductos,
                btnIniciarProductos,
                btnDetenerProductos,
                "Procesando productos..."
        );

        Runnable tareaProductos = crearTareaProceso(
                50,
                160,
                barraProductos,
                lblPorcentajeProductos,
                lblEstadoProductos,
                btnIniciarProductos,
                btnDetenerProductos,
                "Proceso de productos finalizado",
                "Proceso de productos detenido"
        );

        hiloProductos = new Thread(tareaProductos);
        hiloProductos.setName("Hilo-Productos");
        hiloProductos.setDaemon(true);
        hiloProductos.start();
    }

    @FXML
    private void detenerProductos() {
        detenerProceso(
                hiloProductos,
                btnDetenerProductos,
                lblEstadoProductos,
                "Solicitando detención de productos..."
        );
    }

    @FXML
    private void reiniciarProcesos() {
        if (estaEnEjecucion(hiloClientes) || estaEnEjecucion(hiloProductos)) {
            return;
        }

        reiniciarProceso(
                barraClientes,
                lblPorcentajeClientes,
                lblEstadoClientes,
                btnIniciarClientes,
                btnDetenerClientes
        );

        reiniciarProceso(
                barraProductos,
                lblPorcentajeProductos,
                lblEstadoProductos,
                btnIniciarProductos,
                btnDetenerProductos
        );
    }

    private Runnable crearTareaProceso(
            int totalRegistros,
            int milisegundosPorRegistro,
            ProgressBar barraProgreso,
            Label lblPorcentaje,
            Label lblEstado,
            Button btnIniciar,
            Button btnDetener,
            String mensajeFinalizado,
            String mensajeDetenido
    ) {
        return () -> {
            try {
                for (int i = 1; i <= totalRegistros; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(milisegundosPorRegistro);

                    final int avance = i;
                    final int porcentaje = (int) Math.round((avance * 100.0) / totalRegistros);

                    Platform.runLater(() -> {
                        barraProgreso.setProgress(avance / (double) totalRegistros);
                        lblPorcentaje.setText(porcentaje + "%");
                    });
                }

                Platform.runLater(() -> finalizarProceso(
                        lblEstado,
                        btnIniciar,
                        btnDetener,
                        mensajeFinalizado
                ));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                Platform.runLater(() -> finalizarProceso(
                        lblEstado,
                        btnIniciar,
                        btnDetener,
                        mensajeDetenido
                ));
            }
        };
    }

    private void prepararProceso(
            ProgressBar barraProgreso,
            Label lblPorcentaje,
            Label lblEstado,
            Button btnIniciar,
            Button btnDetener,
            String mensajeEstado
    ) {
        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText(mensajeEstado);
        btnIniciar.setDisable(true);
        btnDetener.setDisable(false);
        btnReiniciar.setDisable(true);
    }

    private void detenerProceso(
            Thread hilo,
            Button btnDetener,
            Label lblEstado,
            String mensajeEstado
    ) {
        if (estaEnEjecucion(hilo)) {
            btnDetener.setDisable(true);
            lblEstado.setText(mensajeEstado);
            hilo.interrupt();
        }
    }

    private void finalizarProceso(
            Label lblEstado,
            Button btnIniciar,
            Button btnDetener,
            String mensajeEstado
    ) {
        lblEstado.setText(mensajeEstado);
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);
        btnReiniciar.setDisable(estaEnEjecucion(hiloClientes) || estaEnEjecucion(hiloProductos));
    }

    private void reiniciarProceso(
            ProgressBar barraProgreso,
            Label lblPorcentaje,
            Label lblEstado,
            Button btnIniciar,
            Button btnDetener
    ) {
        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText("Esperando inicio");
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);
        btnReiniciar.setDisable(false);
    }

    private boolean estaEnEjecucion(Thread hilo) {
        return hilo != null && hilo.isAlive();
    }

    public void detenerAlCerrar() {
        if (estaEnEjecucion(hiloClientes)) {
            hiloClientes.interrupt();
        }

        if (estaEnEjecucion(hiloProductos)) {
            hiloProductos.interrupt();
        }
    }
}
