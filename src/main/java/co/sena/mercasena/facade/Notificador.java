package co.sena.mercasena.facade;

import java.util.ArrayList;
import java.util.List;

/** Subsistema de notificaciones (simulado). */
public class Notificador {

    private final List<String> enviados = new ArrayList<>();

    public void enviar(String cliente, String mensaje) {
        enviados.add(cliente + ": " + mensaje);
    }

    public List<String> getEnviados() {
        return List.copyOf(enviados);
    }
}
