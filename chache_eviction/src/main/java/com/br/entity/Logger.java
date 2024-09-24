package com.br.entity;

public class Logger {

    static int counter = 0;
    
    public static void writeLog(String opType) {
        counter++;

        ArchiveManager writer = new ArchiveManager();
        writer.writeInsert("Log.txt", " Log " + Logger.counter + " ==================================" + 
                                             "\n Tipo de Operação: " + opType +
                                             "\n Rotação: " + ((Database.database.rotacao) ? "Sim" : "Não") +
                                             "\n Altura da Árvore: " + Database.database.height() +
                                             "\n N° de Elementos na Árvore: " + Database.database.elements() +
                                             "\n Cache: " +
                                             "\n" + Database.cache.toString() +
                                             "\n \n \n");
    }    

}
