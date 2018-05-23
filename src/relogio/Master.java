package relogio;

import java.util.Timer;

public class Master {

    public long horaInicial;

    public static void main(String[] args) {

        Timer timerSlave = new Timer();
        timerSlave.schedule(new GetHora(), 0, 5000);

    }

    public static long getHora() {
        return System.currentTimeMillis();
    }

    public static void corrigeHora() {

    }
}
