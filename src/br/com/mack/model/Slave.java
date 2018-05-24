/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mack.model;

import br.com.mack.util.FileUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class Slave extends Thread {

    private final int id;
    private Date localTime;

    public Slave(int id, String localTime) {
        this.id = id;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            this.localTime = format.parse(localTime);
        } catch (ParseException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {

            //Cria o socket e converte para o objeto IP
            DatagramSocket serverSocket = new DatagramSocket(9877 + id);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            System.out.println("**Slave " + id + " rodando..");
            System.out.println(localTime);
            while (true) {
                //Objeto para pegar os pacotes que o cliente envia
                DatagramPacket receivePacket
                        = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                //Transforma em string
                String sentence = new String(receivePacket.getData());
                if (sentence.toUpperCase().trim().equals("GETHORA")) {
                    System.out.println("Pediu getData");
                    FileUtil.writeFile("slave" + id + "getHora.txt", "Hora:" + localTime);
                    //************Criar resposta*******************************
                    //Pega o endereco para onde pode responder
                    InetAddress IPAddress = receivePacket.getAddress();
                    //Pega a porta de resposta
                    int port = receivePacket.getPort();
                    String send = String.valueOf(localTime);
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

}
