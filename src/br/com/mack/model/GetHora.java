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
import java.util.Calendar;
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
    private double avg = 0;
    private int avg_slaves = 1;

    @Override
    public void run() {
        ArrayList<Cliente> clientes = new ArrayList<>();
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
                avg_slaves++;
                int port = entry.getValue();
                DatagramPacket sendGetData
                        = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendGetData);

                //Resposta
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String response = new String(receivePacket.getData());
                String msg = entry.getKey().trim() + "\\Msg: " + response.trim();
                System.out.println(msg);
                Cliente c = new Cliente();
                c.setDataLocal(response.trim());
                c.setEstado((response != null));
                c.setNome(entry.getKey());
                c.setPorta(entry.getValue());
                clientes.add(c);
                FileUtil.writeFile("MasterGetHoraResponces" + entry.getKey().trim() + ".txt", msg);
            }

            for (Cliente cliente : clientes) {
                if (cliente.isEstado() == true) {
                    int diferenca = diferentaDeTempos(horaInicial, cliente.getDataLocal());
                    avg += diferenca;
                }
            }

            avg = avg / avg_slaves;

            Date masterAuxiliarDate = minutosParaData(horaInicial, avg);
            System.out.println("Nova data: " + masterAuxiliarDate);

            String msg = "sincroniza-" + masterAuxiliarDate.toString();
            DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, IPAddress, 9877);
            serverSocket.send(sendPacket);

            horaInicial = masterAuxiliarDate;
            serverSocket.close();
            System.out.println("Acabou no master");
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

    public int diferentaDeTempos(Date dataLocal, Date dataCliente) {
        int diferenca = 0;

        Calendar aux = Calendar.getInstance();
        aux.setTime(dataCliente);
        int cliente_hora = aux.get(Calendar.HOUR_OF_DAY);
        int cliente_minutos = aux.get(Calendar.MINUTE);

        aux.setTime(dataLocal);
        int master_hora = aux.get(Calendar.HOUR_OF_DAY);
        System.out.println(master_hora);
        int master_minutos = aux.get(Calendar.MINUTE);

        int diferenca_hora = cliente_hora - master_hora;

        //por que multiplica por 60
        diferenca = (diferenca_hora * 60) + (cliente_minutos - master_minutos);

        return diferenca;
    }

    public static Date minutosParaData(Date date, double tempoDiferencaMaster) {
        Date resultado = null;

        Calendar aux = Calendar.getInstance();
        aux.setTime(date);
        aux.add(Calendar.MINUTE, (int) Math.round(tempoDiferencaMaster));
        resultado = aux.getTime();

        return resultado;
    }

}
