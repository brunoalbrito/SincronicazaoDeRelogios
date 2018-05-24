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
public class Slave {

    private final int id;
    private Date localTime;
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public Slave(int id, String localTime) {
        this.id = id;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            this.localTime = format.parse(localTime);
        } catch (ParseException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        System.out.println("*******DATA DE ENTRADA" + localTime);
        try {
            //Cria o socket e converte para o objeto IP
            DatagramSocket serverSocket = new DatagramSocket(9877 + id);
            while (true) {
                byte[] sendData = new byte[1024];
                byte[] receiveData = new byte[1024];
                System.out.println("**Slave " + id + " rodando");
                //Objeto para pegar os pacotes que o cliente envia
                DatagramPacket receivePacket
                        = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                //Transforma em string
                String sentence = new String(receivePacket.getData());
                System.out.println(sentence.trim());
                if (sentence.toUpperCase().trim().equals("GETHORA")) {
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
                } else if (sentence.toLowerCase().split("-")[0].equals("sincroniza")) {
                    System.out.println("Caiu no SINCRONIZA");
                    try {
                        String data = sentence.split("-")[1];
                        Date novaData = format.parse(data.split(" ")[3]);
                        if (localTime != novaData) {
                            localTime = novaData;
                            System.out.println(localTime);
                            System.out.println("Data Alterada");
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("*********Acabou no slave********\n\n\n");
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
