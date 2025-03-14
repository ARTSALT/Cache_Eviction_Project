package com.br.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class ServiceOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceOrder that = (ServiceOrder) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description);
    }
}
