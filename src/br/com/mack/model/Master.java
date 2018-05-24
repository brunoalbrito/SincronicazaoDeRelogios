package br.com.mack.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Master {

    public Date horaInicial;
    public String clientFileName;
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public Master(String horaInicial) {
        System.out.println("Usou esse sucesso");
        try {
            this.horaInicial = format.parse(horaInicial);
        } catch (ParseException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        Timer timerSlave = new Timer();
        GetHora getHora = new GetHora();
        getHora.setHoraInicial(horaInicial);
        timerSlave.schedule(getHora, 0, 3000);
    }

    public static void corrigeHora() {

    }
}
