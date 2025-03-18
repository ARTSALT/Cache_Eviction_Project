package com.br.servidorDeAplicacao;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Application {
    ServerSocket socketServidor;
    Socket cliente;
    int porta;
    int cont = 0;

    Database database;

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

            try {
                database = new Database();

                DatabaseInterface skeleton = (DatabaseInterface) UnicastRemoteObject.exportObject(database, porta - 2000);

                Registry registro = LocateRegistry.createRegistry(porta - 2000);
                registro.bind("DatabaseService", skeleton);
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (true) {
                cliente = socketServidor.accept();

                cont++;
                ImplApplication servidor = new ImplApplication(cliente, porta, database);
                Thread t = new Thread(servidor);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
