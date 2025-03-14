package com.br.entity;

import java.io.Serial;
import java.io.Serializable;

public class Packet<O> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    O content;
    String purpose;

    public Packet(String purpose, O content) {
        this.content = content;
        this.purpose = setPurposeCode(purpose);
    }

    public O getContent() {
        return content;
    }

    public void setContent(O content) {
        this.content = content;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = setPurposeCode(purpose);
    }

    private String setPurposeCode(String purpose) {
        return switch (purpose) {
            case "response" -> "1";
            case "insert" -> "00";
            case "remove" -> "01";
            case "search" -> "10";
            case "update" -> "11";
            case "getAll" -> "000";
            default -> "0";
        };
    }

}
