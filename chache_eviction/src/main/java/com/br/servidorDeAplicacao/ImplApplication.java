package com.br.servidorDeAplicacao;

import com.br.entity.Logger;
import com.br.entity.Packet;
import com.br.entity.ServiceOrder;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLOutput;

public class ImplApplication implements Runnable {
    public Socket socketCliente;
    public ObjectInputStream entrada;
    public ObjectOutputStream saida;
    public static int cont = 0;

    public Packet<ServiceOrder> request;
    public Packet<?> responseObj;

    public ImplApplication(Socket cliente, int cont) {
        socketCliente = cliente;
        ImplApplication.cont = cont;
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
            e.printStackTrace();
        }
    }

    private void purpose(Packet<ServiceOrder> request) {
        switch(request.getPurpose()) {
            case "00":
                //Função de Inserção
                Database.insert(request.getContent());
                Logger.writeLog("00", "servidorDeAplicacao/", "aplicacao_" + cont);

                responseObj = new Packet<>("response", Database.getAll());
                break;
            case "01":
                //Função de Remoção
                Database.remove(request.getContent());
                Logger.writeLog("01", "servidorDeAplicacao/", "aplicacao_" + cont);

                responseObj = new Packet<>("response", Database.getAll());
                break;
            case "10":
                //Função de Busca
                if (Database.database.containsKey(request.getContent().getCode())) {
                    responseObj = new Packet<>("response", Database.search(request.getContent()));
                    Logger.writeLog("10", "servidorDeAplicacao/", "aplicacao_" + cont);
                } else {
                    responseObj = new Packet<>("0", null);
                }
                break;
            case "11":
                //Função de Atualização
                if (Database.database.containsKey(request.getContent().getCode())) {
                    Database.subistitute(request.getContent());
                    Logger.writeLog("11", "servidorDeAplicacao/", "aplicacao_" + cont);
                    responseObj = new Packet<>("response", Database.getAll());
                }
                break;
            case "000":
                //Função de Retorno da Base de Dados
                Logger.writeLog("000", "servidorDeAplicacao/", "aplicacao_" + cont);
                responseObj = new Packet<>("response", Database.getAll());
                break;
            default:
                responseObj = new Packet<>("0", null);
        }
    }

    private boolean auth(String nome, String senha) {
        return nome.equals("admin") && senha.equals("admin");
    }
}
