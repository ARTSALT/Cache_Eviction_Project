package com.br.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ServiceOrder {
    static int counter = 0;
    int code;
    String name, description;
    String solTime;

    public ServiceOrder(int code, String name, String description) {
        setCode(code);
        setName(name);
        setDescription(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
        solTime = LocalDateTime.now().format(formatter);
    }

    public ServiceOrder(String name, String description) {
        counter++;
        code = counter;
        setCode(code);
        setName(name);
        setDescription(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
        solTime = LocalDateTime.now().format(formatter);
    }

    public ServiceOrder(int code) {
        setCode(code);
    }

    //Getters and Setters
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return this.code;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }

    public void setSolTime(String solTime) {
        this.solTime = solTime;
    }
    public String getSolTime() {
        return this.solTime;
    }
}
