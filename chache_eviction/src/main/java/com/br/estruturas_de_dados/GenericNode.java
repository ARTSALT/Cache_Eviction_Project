package com.br.estruturas_de_dados;

public class GenericNode <T> {

    int key;
    T data;

    public GenericNode(int key, T data) {
        this.key = key;
        this.data = data;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}