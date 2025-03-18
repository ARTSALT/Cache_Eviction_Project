package com.br.servidorDeProxy;

import com.br.entity.ArchiveManager;

public class ProxyApp {
    public static void main(String[] args) {
        int porta = 12346;
        new Proxy(porta);
        ArchiveManager writer = new ArchiveManager();
        writer.clear("servidorDeProxy/log_proxy_" + porta + ".txt");
    }
}
