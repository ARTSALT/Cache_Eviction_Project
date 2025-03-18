package com.br.servidorDeAplicacao;

import com.br.entity.ArchiveManager;

public class ApplicationApp {
    public static void main(String[] args) {
        int porta = 12349;
        new Application(porta);
        ArchiveManager writer = new ArchiveManager();
        writer.clear("servidorDeAplicacao/log_aplicacao_" + porta + ".txt");
    }
}
