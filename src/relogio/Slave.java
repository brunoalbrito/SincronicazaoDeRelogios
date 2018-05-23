/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relogio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class Slave {

    public static void main(String[] args) {
        try {
            //        Timer timer = new Timer();
            //        timer.schedule(new GetHora(), 0, 2000);

            //Cria o socket e converte para o objeto IP
            DatagramSocket serverSocket = new DatagramSocket(9877);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            System.out.println("Slave 2 Rodando");
            while (true) {
                //Objeto para pegar os pacotes que o cliente envia
                DatagramPacket receivePacket
                        = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                //Transforma em string
                String sentence = new String(receivePacket.getData());
                if (sentence.toUpperCase().trim().equals("GETHORA")) {
                    System.out.println("Pediu getData");
                    //************Criar resposta*******************************
                    //Pega o endereco para onde pode responder
                    InetAddress IPAddress = receivePacket.getAddress();
                    //Pega a porta de resposta
                    int port = receivePacket.getPort();
                    String send = String.valueOf(getHora());
                    sendData = send.getBytes();
                    DatagramPacket sendPacket
                            = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                }
            }

        } catch (SocketException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static long getHora() {
        return System.currentTimeMillis();
    }
}
