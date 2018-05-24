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
import java.util.ArrayList;
import java.util.Date;
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

    private Date horaInicial;
    private Map<String, Integer> slaves = new HashMap<>();

    @Override
    public void run() {
//        System.out.println("Chamou aqui");
        try {
            carregaSlaves();

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
                String response = new String(receivePacket.getData());
//                System.out.println("O escravo: " + entry.getKey() + " , respondeu " + modifiedSentence);
//                new FileUtil().writeFile("masterGetHora.txt", "O escravo: " + entry.getKey() + " , respondeu " + modifiedSentence);
                String msg = entry.getKey().trim() + "\\Msg: " + response.trim();
                System.out.println(msg);
                FileUtil.writeFile("MasterGetHoraResponces" + entry.getKey().trim() + ".txt", msg);
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

    public void carregaSlaves() {
        ArrayList<String> slavesFile = FileUtil.readFile("slaves.txt");
        for (String slave : slavesFile) {
            String nome = slave.split("/")[0];
            String id = slave.split("/")[1];
            slaves.put(nome, Integer.parseInt(id));
        }
    }

    public Date getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(Date horaInicial) {
        this.horaInicial = horaInicial;
    }
    
    public void diferentaDeTempos(Date dataLocal, Date dataCliente){
        
    }

}
