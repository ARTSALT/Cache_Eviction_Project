package com.br.entity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ArchiveManager {

    public void writeInsert(String filePath, String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.append(text);
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
