package br.com.mack.principal;

import br.com.mack.model.Master;
import br.com.mack.model.Slave;

/**
 *
 * @author Bruno
 */
public class Principal {

    public static void main(String[] args) {
        if (args[0].trim().equals("-m") && args.length >= 2) {
            Master master = new Master(args[1]);
            master.start();
        } 
        else if (args[0].trim().equals("-s")) {
            new Slave(Integer.parseInt(args[1]), args[2]).run();
        }
    }

}
