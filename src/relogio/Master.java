package relogio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Master {

    public static void main(String[] args) {

        try {
            //Porta que o socket responde
            DatagramSocket serverSocket = new DatagramSocket(9876);
            //O que o cliente responde
            byte[] receiveData = new byte[1024];

            //O que sera enviado
            byte[] sendData = new byte[1024];

            while (true) {
                //Objeto para pegar os pacotes que o cliente envia
                DatagramPacket receivePacket
                        = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                //Transforma em string
                String sentence = new String(receivePacket.getData());
                System.out.println("Resposta: " + sentence);

                //************Criar resposta*******************************
                //Pega o endereco para onde pode responder
                InetAddress IPAddress = receivePacket.getAddress();
                //Pega a porta de resposta
                int port = receivePacket.getPort();

                String send = "Envia a resposta";
                sendData = send.getBytes();
                //Cria o pacote para resposta
                DatagramPacket sendPacket
                        = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }

        } catch (SocketException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
