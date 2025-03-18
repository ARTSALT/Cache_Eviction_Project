package com.br.servidorDeProxy;

import com.br.entity.Logger;
import com.br.entity.Packet;
import com.br.entity.ServiceOrder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ImplProxy implements Runnable {
    public int porta;
    public Socket socketCliente;
    public ObjectInputStream entrada;
    public ObjectOutputStream saida;
    public static int cont = 0;

    //Endereço do Servidor de Aplicação
    public String ip_app = "192.168.0.3";
    public int porta_app = 12349;
    public int porta_app_b = 12350;
    public Socket socketServer;
    public ObjectInputStream entradaApp;
    public ObjectOutputStream saidaApp;

    int[] portas = {12346, 12347, 12348};
    int[] newPortas;
    Cache cache;

    public Packet<ServiceOrder> request;
    public Packet<?> responseObj;
    CacheInterface cacheRemota_1;
    CacheInterface cacheRemota_2;

    public ImplProxy(Socket cliente, int porta, Cache cache) throws UnknownHostException {
        socketCliente = cliente;
        this.porta = porta;
        this.cache = cache;
        ip_app = InetAddress.getLocalHost().getHostAddress();
    }

    public void run() {
        System.out.println("Conexão estabelecida com Cliente:"+
                socketCliente.getInetAddress().getHostAddress() +
                "/" +
                socketCliente.getInetAddress().getHostName()
        );

        try {
            saida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());

            //authentication

            String nome = entrada.readUTF();
            String senha = entrada.readUTF();
            boolean auth = auth(nome, senha);

            if (auth) {
                saida.writeUTF("1");
                saida.flush();
            } else {
                saida.writeUTF("0");
                saida.flush();

                entrada.close();
                saida.close();
                socketCliente.close();
                return;
            }

            //Create Connection with Application Server
            try {
                socketServer = new Socket(ip_app, porta_app);
                saidaApp = new ObjectOutputStream(socketServer.getOutputStream());
                entradaApp = new ObjectInputStream(socketServer.getInputStream());
            } catch (SocketException e) {
                swapApplicationServer();
            }

            System.out.println("Conexão com o servidor de Aplicação estabelecida");

            //Iniciando RMI - Cliente
            // Acessa a Cache dos outros Proxies por RMI

            newPortas = new int[portas.length - 1];
            int index = 0;

            for (int p : portas) {
                if (p != porta) {
                    newPortas[index] = p - 2000;
                    index++;
                }
            }

            Registry registro1 = LocateRegistry.getRegistry(newPortas[0]);
            Registry registro2 = LocateRegistry.getRegistry(newPortas[1]);

            //TODO no caso de um proxy estar off enquanto a conexão se inicia, a conexão não pode ser estabelecida
            cacheRemota_1 = (CacheInterface) registro1.lookup("CacheService");
            cacheRemota_2 = (CacheInterface) registro2.lookup("CacheService");

            System.out.println("Registros RMI conectados");

            //Keep Connection and Message stream
            while (socketCliente.isConnected()) {
                try {
                    request = (Packet<ServiceOrder>) entrada.readObject();
                    purpose(request);
                    saida.writeObject(responseObj);
                    saida.flush();
                } catch (SocketException e) {
                    swapApplicationServer();

                    request = (Packet<ServiceOrder>) entrada.readObject();
                    purpose(request);
                    saida.writeObject(responseObj);
                    saida.flush();
                }
            }

            entrada.close();
            saida.close();
            socketCliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void purpose(Packet<ServiceOrder> request) throws IOException, ClassNotFoundException {
        switch(request.getPurpose()) {
            case "00":
                //Função de Inserção
                insert(request);

                break;
            case "01":
                //Função de Remoção
                remove(request);

                break;
            case "10":
                //Função de Busca
                busca(request);

                break;
            case "11":
                //Função de Atualização
                update(request);

                break;
            case "000":
                //Função de Retorno da Base de Dados
                getAll(request);

                break;
            default:
                responseObj = new Packet<>("0", null);
        }
    }

    void insert(Packet<ServiceOrder> request) throws IOException, ClassNotFoundException {
        saidaApp.writeObject(request);
        saidaApp.flush();

        responseObj = (Packet<?>) entradaApp.readObject();
    }

    void remove(Packet<ServiceOrder> request) throws IOException, ClassNotFoundException {
        saidaApp.writeObject(request);
        saidaApp.flush();
        if (cache.search(request.getContent()) != null) {
            cache.remove(request.getContent());
            Logger.writeLog("01", "servidorDeProxy/", "proxy_" + porta);
        }
        if (isRegistryUp(cacheRemota_1) && cacheRemota_1.contains(request.getContent())) {
            cacheRemota_1.remove(request.getContent());
            Logger.writeLog("01", "servidorDeProxy/", "proxy_" + (newPortas[0] + 2000));
        }
        if (isRegistryUp(cacheRemota_2) && cacheRemota_2.contains(request.getContent())) {
            cacheRemota_2.remove(request.getContent());
            Logger.writeLog("01", "servidorDeProxy/", "proxy_" + (newPortas[1] + 2000));
        }

        responseObj = (Packet<?>) entradaApp.readObject();
    }

    void busca(Packet<ServiceOrder> request) throws IOException, ClassNotFoundException {
        responseObj = null;
        responseObj = new Packet<>("response", cache.search(request.getContent()));
        Logger.writeLog("10", "servidorDeProxy/", "proxy_" + porta);

        if (responseObj.getContent() != null) {
            System.out.println("Cache Hit");
            return;
        } else {
            if (isRegistryUp(cacheRemota_1)) {
                responseObj = new Packet<>("response", cacheRemota_1.search(request.getContent()));
                Logger.writeLog("10", "servidorDeProxy/", "proxy_" + (newPortas[0] + 2000));
                if (responseObj.getContent() != null) {
                    System.out.println("Cache Hit");
                    cache.insert(request.getContent());
                    Logger.writeLog("00", "servidorDeProxy/", "proxy_" + porta);
                    return;
                }
            } else {
                if (isRegistryUp(cacheRemota_2)) {
                    responseObj = new Packet<>("response", cacheRemota_2.search(request.getContent()));
                    Logger.writeLog("10", "servidorDeProxy/", "proxy_" + (newPortas[1] + 2000));
                    if (responseObj.getContent() != null) {
                        System.out.println("Cache Hit");
                        cache.insert(request.getContent());
                        Logger.writeLog("00", "servidorDeProxy/", "proxy_" + porta);
                        return;
                    }
                }
            }
        }

        System.out.println("Cache Miss");
        saidaApp.writeObject(request);
        saidaApp.flush();
        Packet<ServiceOrder> order = (Packet<ServiceOrder>) entradaApp.readObject();

        if (order.getPurpose().equals("1")) {
            cache.insert(order.getContent());
            responseObj = order;
            Logger.writeLog("00", "servidorDeProxy/", "proxy_" + porta);
        } else {
            responseObj = new Packet<>("0", null);
        }
    }

    void update(Packet<ServiceOrder> request) throws IOException, ClassNotFoundException {
        saidaApp.writeObject(request);
        saidaApp.flush();

        if (cache.substitute(request.getContent())) {
            Logger.writeLog("11", "servidorDeProxy/", "proxy_" + cont);
        }
        if (isRegistryUp(cacheRemota_1)) {
            if (cacheRemota_1.substitute(request.getContent()))
                Logger.writeLog("11", "servidorDeProxy/", "proxy_" + (newPortas[0] + 2000));
        }
        if (isRegistryUp(cacheRemota_2)) {
            if (cacheRemota_2.substitute(request.getContent()))
                Logger.writeLog("11", "servidorDeProxy/", "proxy_" + (newPortas[1] + 2000));
        }

        responseObj = (Packet<?>) entradaApp.readObject();
    }

    void getAll(Packet<ServiceOrder> request) throws IOException, ClassNotFoundException {
        saidaApp.writeObject(request);
        saidaApp.flush();

        responseObj = (Packet<?>) entradaApp.readObject();
    }

    private boolean auth(String nome, String senha) {
        return nome.equals("admin") && senha.equals("admin");
    }

    private void swapApplicationServer() throws IOException {
        int aux = porta_app;
        porta_app = porta_app_b;
        porta_app_b = aux;

        socketServer = new Socket(ip_app, porta_app);
        saidaApp = new ObjectOutputStream(socketServer.getOutputStream());
        entradaApp = new ObjectInputStream(socketServer.getInputStream());
    }

    private boolean isRegistryUp(CacheInterface cache) {
        try {
            cache.ping();
            return true;
        } catch (RemoteException e) {
            return false;
    }
    }
}
