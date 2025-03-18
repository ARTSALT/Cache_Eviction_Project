package com.br.entity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ArchiveManager {

    public void clear(String filePath) {
        try {
            BufferedWriter clearer = new BufferedWriter(new FileWriter(filePath));

            clearer.flush();
            clearer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
