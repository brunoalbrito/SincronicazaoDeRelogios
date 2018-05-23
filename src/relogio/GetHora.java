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
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class GetHora extends TimerTask {

    @Override
    public void run() {
        try {
            Map<String, Integer> slaves = new HashMap<>();
            slaves.put("Slave1", 9877);
            slaves.put("Slave2", 9878);
            slaves.put("Slave3", 9879);
            //Cria o socket e converte para o objeto IP
            DatagramSocket serverSocket = new DatagramSocket(9876);
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            String send = "getHora";
            sendData = send.getBytes();
            for (Map.Entry<String, Integer> entry : slaves.entrySet()) {
                int port = entry.getValue();
                DatagramPacket sendGetData
                        = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendGetData);

                //Resposta
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                System.out.println("O escravo: " + entry.getKey() + ", respondeu " + modifiedSentence);
            }
            serverSocket.close();

        } catch (SocketException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
