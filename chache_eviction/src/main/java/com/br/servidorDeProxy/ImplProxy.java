package com.br.servidorDeProxy;

import com.br.entity.Logger;
import com.br.entity.Packet;
import com.br.entity.ServiceOrder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ImplProxy implements Runnable {
    public Socket socketCliente;
    public ObjectInputStream entrada;
    public ObjectOutputStream saida;
    public static int cont = 0;

    //Endereço do Servidor de Aplicação
    public String ip_app = "192.168.0.3";
    public int porta_app = 12349;
    public Socket socketServer;
    public ObjectInputStream entradaApp;
    public ObjectOutputStream saidaApp;


    public Packet<ServiceOrder> request;
    public Packet<?> responseObj;

    public ImplProxy(Socket cliente, int cont) throws UnknownHostException {
        socketCliente = cliente;
        ImplProxy.cont = cont;
        ip_app = InetAddress.getLocalHost().getHostAddress();
    }

    public void run() {
        System.out.println("Conexão N: " +
                ImplProxy.cont +
                " com o cliente "+
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
            socketServer = new Socket(ip_app, porta_app);
            saidaApp = new ObjectOutputStream(socketServer.getOutputStream());
            entradaApp = new ObjectInputStream(socketServer.getInputStream());

            System.out.println("Conexão com o servidor de Aplicação estabelecida");

            //Keep Connection and Message stream
            while (socketCliente.isConnected()) {
                request = (Packet<ServiceOrder>) entrada.readObject();
                purpose(request);
                saida.writeObject(responseObj);
                saida.flush();
            }

            entrada.close();
            saida.close();
            socketCliente.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void purpose(Packet<ServiceOrder> request) throws IOException, ClassNotFoundException {
        switch(request.getPurpose()) {
            case "00":
                //Função de Inserção
                saidaApp.writeObject(request);
                saidaApp.flush();

                responseObj = (Packet<?>) entradaApp.readObject();
                break;
            case "01":
                //Função de Remoção
                saidaApp.writeObject(request);
                saidaApp.flush();
                if (Cache.cache.contains(request.getContent())) {
                    Logger.writeLog("01", "servidorDeProxy/", "proxy_" + cont);
                }
                Cache.remove(request.getContent());


                responseObj = (Packet<?>) entradaApp.readObject();
                break;
            case "10":
                //Função de Busca

                if (Cache.contains(request.getContent())) {
                    System.out.println("Cache Hit");
                    responseObj = new Packet<>("response", Cache.search(request.getContent()));
                    Logger.writeLog("10", "servidorDeProxy/", "proxy_" + cont);
                } else {
                    System.out.println("Cache Miss");
                    saidaApp.writeObject(request);
                    saidaApp.flush();
                    Packet<ServiceOrder> order = (Packet<ServiceOrder>) entradaApp.readObject();
                    System.out.println(order.getContent().getCode() + " " + order.getContent().getName() + " " + order.getContent().getDescription());

                    if (order.getPurpose().equals("1")) {
                        Cache.insert(order.getContent());
                        responseObj = order;
                        Logger.writeLog("00", "servidorDeProxy/", "proxy_" + cont);
                    } else {
                        responseObj = new Packet<>("0", null);
                    }
                }
                break;
            case "11":
                //Função de Atualização
                saidaApp.writeObject(request);
                saidaApp.flush();

                if (Cache.cache.contains(request.getContent())) {
                    Cache.subistitute(request.getContent());
                    Logger.writeLog("11", "servidorDeProxy/", "proxy_" + cont);
                }

                responseObj = (Packet<?>) entradaApp.readObject();
                break;
            case "000":
                //Função de Retorno da Base de Dados

                saidaApp.writeObject(request);
                saidaApp.flush();

                responseObj = (Packet<?>) entradaApp.readObject();
                break;
            default:
                responseObj = new Packet<>("0", null);
        }
    }

    private boolean auth(String nome, String senha) {
        return nome.equals("admin") && senha.equals("admin");
    }
}
