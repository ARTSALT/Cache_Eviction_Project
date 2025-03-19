package com.br.connections;

import com.br.App;
import com.br.controller.LoginController;
import com.br.entity.Logger;
import com.br.entity.Packet;
import com.br.entity.ServiceOrder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ConnectionProxy {

    Socket client;
    public static ObjectOutputStream saida;
    public static ObjectInputStream entrada;

    static String ip;
    String nome;
    String senha;
    boolean auth;

    public ConnectionProxy(String ip, int porta, String nome, String senha) {
        try {
            client = new Socket(ip, porta);
            this.ip = ip;
            this.nome = nome;
            this.senha = senha;

            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            saida = new ObjectOutputStream(client.getOutputStream());
            entrada = new ObjectInputStream(client.getInputStream());


            saida.writeUTF(nome);
            saida.flush();
            saida.writeUTF(senha);
            saida.flush();
            auth = entrada.readUTF().equals("1");

            if (!auth) {
                saida.close();
                entrada.close();
                client.close();

                return;
            }

            List<ServiceOrder> check = (List<ServiceOrder>) sendRequest("check", null).getContent();

            if (check.isEmpty()) {
                for (int i = 1; i <= 100; i++) {
                    sendRequest("insert", new ServiceOrder(String.valueOf(i), String.valueOf(i)));
                }
            }

            App.telaTabela();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Packet<?> sendRequest(String code, ServiceOrder order) throws Exception {
        try {
            saida.writeObject(new Packet<>(code, order));
            saida.flush();
            return (Packet<?>) entrada.readObject();
        } catch (SocketException e) {
            System.out.println("Tentando Reconexão");
            App.tryConnection(ip, 12345);
            ConnectionProxy connection = new ConnectionProxy(App.enderecoProxy[0],
                    Integer.parseInt(App.enderecoProxy[1]), "admin", "admin");

            saida.writeObject(new Packet<>(code, order));
            saida.flush();
            return (Packet<?>) entrada.readObject();
        }
    }
}
