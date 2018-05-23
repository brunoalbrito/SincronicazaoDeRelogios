/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relogio;

import java.util.Timer;

/**
 *
 * @author Bruno
 */
public class Slave {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new GetHora(), 0, 2000);
    }
}
