package com.br.entity;

public class Logger {

    static int counter = 0;
    
    public static synchronized void writeLog(String opType, String path, String nome) {
        counter++;

        ArchiveManager writer = new ArchiveManager();
        writer.writeInsert("./chache_eviction/src/main/java/com/br/" + path + "log_" + nome + ".txt", " Log " + Logger.counter + " ==================================" +
                                             "\n Tipo de Operação: " + opType(opType) +
                                             "\n \n \n");
    }    

    public static String opType(String code) {
        return switch (code) {
            case "1" -> "response";
            case "00" -> "insert";
            case "01" -> "remove";
            case "10" -> "search";
            case "11" -> "update";
            case "000" -> "getAll";
            default -> "failure";
        };
    }
}
