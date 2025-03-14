package com.br.servidorDeAplicacao;


import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Application {
    ServerSocket socketServidor;
    Socket cliente;
    int porta;
    int cont = 0;

    public Application(int porta) {
        this.porta = porta;
        this.rodar();
    }
    private void rodar() {
        try {
            socketServidor = new ServerSocket(porta);
            System.out.println("Servidor rodando na porta " +
                    socketServidor.getLocalPort());
            System.out.println("HostAddress = " +
                    InetAddress.getLocalHost().getHostAddress());
            System.out.println("HostName = " +
                    InetAddress.getLocalHost().getHostName());
            System.out.println("Aguardando conexão do cliente...");
            while (true) {
                cliente = socketServidor.accept();

                cont++;
                ImplApplication servidor = new ImplApplication(cliente, cont);
                Thread t = new Thread(servidor);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
