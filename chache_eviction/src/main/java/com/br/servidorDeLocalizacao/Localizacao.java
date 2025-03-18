package com.br.servidorDeLocalizacao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Localizacao {
    static ServerSocket serverSocket;
    static InetAddress inet;
    static String[]servidores = {"12346", "12347", "12348"};
    static int index = 0;
    static String proxyIp;

    public static void main(String[] args) {
        int porta = 12345;

        try {
            //TODO Sempre trocar o proxy IP
            proxyIp = InetAddress.getLocalHost().getHostAddress();

            serverSocket = new ServerSocket(porta);
            System.out.println("Servidor Online no ip: " + inet.getLocalHost().getHostAddress()
                    + " na porta: " + porta);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Conexão estabelecida com o cliente: " + socket.getInetAddress().getHostAddress());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                    output.writeUTF(proxyIp);
                    output.writeUTF(servidores[index % 3]);
                    index++;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
