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

    public static void main(String[] args) {
        int porta = 12345;

        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("Servidor Online no ip: " + inet.getLocalHost().getHostAddress()
                    + " na porta: " + porta);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Conexão estabelecida com o cliente: " + socket.getInetAddress().getHostAddress());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                    String message = input.readUTF();

                    String[] response = processMessage(message);

                    output.writeUTF(response[0]);
                    output.writeUTF(response[1]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String[] processMessage(String message) throws UnknownHostException {
        String value1 = InetAddress.getLocalHost().getHostAddress();
        String value2;

        switch (message) {
            case "00":
                // Localização deste servidor
                value2 = "12345";
                break;
            case "01":
                // Localização do servidor de Proxy 1
                value2 = "12346";
                break;
            case "10":
                // Localização do servidor de Proxy 2
                value2 = "12347";
                break;
            case "11":
                // Localização do servidor de Proxy 3
                value2 = "12348";
                break;
            default:
                value1 = "Unknown";
                value2 = "Unknown";
                break;
        }

        return new String[]{value1, value2};
    }
}
