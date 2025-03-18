package com.br.connections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class ConnectionLocal {

    Socket cliente;
    DataOutputStream saida;
    DataInputStream entrada;
    String ip;
    int porta;

    public ConnectionLocal(String i, int p) {
        this.ip = i;
        this.porta = p;
    }

    public String[] connect() throws Exception {
        try {
            cliente = new Socket(ip, porta);
            saida = new DataOutputStream(cliente.getOutputStream());
            entrada = new DataInputStream(cliente.getInputStream());

            System.out.println("Conectado com o servidor de Localização ");

            String ip_p = entrada.readUTF();
            String porta_p = entrada.readUTF();

            System.out.println(porta_p);

            return new String[]{ip_p, porta_p};
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ConnectException();
    }
}
