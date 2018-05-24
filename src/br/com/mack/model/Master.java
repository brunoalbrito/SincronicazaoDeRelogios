package br.com.mack.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Master {

    public Date horaInicial;
    public String clientFileName;
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public Master(String horaInicial) {
        System.out.println("Master Inicalizaco com sucesso");
        try {
            this.horaInicial = format.parse(horaInicial);
        } catch (ParseException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        GetHora getHora = new GetHora();
        while (true) {
            try {
                Thread.sleep(8000);
                getHora.setHoraInicial(horaInicial);
                getHora.run();
            } catch (InterruptedException ex) {
                Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
