package com.br.servidorDeAplicacao;

import com.br.entity.Logger;
import com.br.entity.Packet;
import com.br.entity.ServiceOrder;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ImplApplication implements Runnable {
    public Socket socketCliente;
    public ObjectInputStream entrada;
    public ObjectOutputStream saida;
    public int porta;
    public static int cont = 0;

    public int ttb = 1;
    public Database database;
    DatabaseInterface databaseRemota;

    public Packet<ServiceOrder> request;
    public Packet<?> responseObj;

    public ImplApplication(Socket cliente, int porta, Database database) {
        socketCliente = cliente;
        this.porta = porta;
        this.database = database;
    }

    public void run() {
        System.out.println("Conexão N: " +
                com.br.servidorDeProxy.ImplProxy.cont +
                " com o cliente "+
                socketCliente.getInetAddress().getHostAddress() +
                "/" +
                socketCliente.getInetAddress().getHostName());

        try {
            saida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());

            System.out.println("Conexão com o Servidor de Proxy Estabelecida");

            int connectionPort = 0;

            switch (porta) {
                case 12349:
                    connectionPort = 10350;
                case 12350:
                    connectionPort = 10349;
            }

            Registry registro1 = LocateRegistry.getRegistry(connectionPort);
            databaseRemota = (DatabaseInterface) registro1.lookup("DatabaseService");

            //Keep Connection and Message stream
            while (socketCliente.isConnected()) {
                request = (Packet<ServiceOrder>) entrada.readObject();
                purpose(request);

                if (ttb % 10 == 0) {
                    databaseRemota.updateDatabase(database.getAll());
                }
                ttb++;

                saida.writeObject(responseObj);
                saida.flush();
            }

            entrada.close();
            saida.close();
            socketCliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void purpose(Packet<ServiceOrder> request) {
        switch(request.getPurpose()) {
            case "00":
                //Função de Inserção
                database.insert(request.getContent());
                Logger.writeLog("00", "servidorDeAplicacao/", "aplicacao_" + porta);

                responseObj = new Packet<>("response", database.getAll());
                break;
            case "01":
                //Função de Remoção
                database.remove(request.getContent());
                Logger.writeLog("01", "servidorDeAplicacao/", "aplicacao_" + porta);

                responseObj = new Packet<>("response", database.getAll());
                break;
            case "10":
                //Função de Busca
                if (database.database.containsKey(request.getContent().getCode())) {
                    responseObj = new Packet<>("response", database.search(request.getContent()));
                    Logger.writeLog("10", "servidorDeAplicacao/", "aplicacao_" + porta);
                } else {
                    responseObj = new Packet<>("0", null);
                }
                break;
            case "11":
                //Função de Atualização
                if (database.database.containsKey(request.getContent().getCode())) {
                    database.substitute(request.getContent());
                    Logger.writeLog("11", "servidorDeAplicacao/", "aplicacao_" + porta);
                    responseObj = new Packet<>("response", database.getAll());
                }
                break;
            case "000":
                //Função de Retorno da Base de Dados
                Logger.writeLog("000", "servidorDeAplicacao/", "aplicacao_" + porta);
                responseObj = new Packet<>("response", database.getAll());
                break;
            default:
                responseObj = new Packet<>("0", null);
        }
    }

    private boolean auth(String nome, String senha) {
        return nome.equals("admin") && senha.equals("admin");
    }
}
