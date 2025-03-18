package com.br.servidorDeProxy;


import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Proxy {
    ServerSocket socketServidor;
    Socket cliente;
    int porta;
    int cont = 0;
    Cache cache;


    public Proxy(int porta) {
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
                cache = new Cache();

                CacheInterface skeleton = (CacheInterface) UnicastRemoteObject.exportObject(cache, porta - 2000);

                Registry registro = LocateRegistry.createRegistry(porta - 2000);
                registro.bind("CacheService", skeleton);
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (true) {
                cliente = socketServidor.accept();

                cont++;
                ImplProxy servidor = new ImplProxy(cliente, porta, cache);
                Thread t = new Thread(servidor);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
