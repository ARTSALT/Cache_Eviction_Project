package com.br.estruturas_de_dados;

import java.util.ArrayList;
import java.util.List;

public class HashTable <T> {
    GenericNode<T>[] table;
    int size;
    int primeLower;
    int quantidadeElementos;

    @SuppressWarnings("unchecked")
    public HashTable(int size) {
        this.size = primeUpper(size);
        this.primeLower = primeLower(size);
        table = new GenericNode[size];
    }

    private int primeUpper(int size) {
        while (size < (size * 100)) {
            if (isPrime(size)) {
                return size;
            }
            size++;
        }
        return 1;
    }

    private int primeLower(int size) {
        while (size > (size / 100)) {
            if (isPrime(size)) {
                return size;
            }
            size--;
        }
        return 1;
    }

    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(i); i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    public void insert(int key, T s) {
        GenericNode<T> n = new GenericNode<>(key, s);
        insert(key, n);
    }

    private void insert(int key, GenericNode<T> n) {
        int pos;

        for (int k = 0; k < size; k++) {
            pos = dispersaoDupla(key, k);

            if (table[pos] == null) {
                table[pos] = n;
                quantidadeElementos++;
                break;
            }
        }

        if (carga() >= 0.75) {
            rehash();
        }
    }

    public void edit(int key, T s) {
        GenericNode<T> n = new GenericNode<>(key, s);
        edit(key, n);
    }

    private void edit(int key, GenericNode<T> n) {
        for (int k = 0; k < size; k++) {
            int pos = dispersaoDupla(key, k);

            if (table[pos].key == key) {
                table[pos] = n;
                break;
            }
        }
    }

    public void remove(int key) {
        for (int k = 0; k < size; k++) {
            int pos = dispersaoDupla(key, k);

            if (table[pos].key == key) {
                table[pos] = null;
                quantidadeElementos--;
                break;
            }
        }
    }

    public T get(int key) {
        for (int k = 0; k < size; k++) {
            int pos = dispersaoDupla(key, k);

            if (table[pos] != null) {
                if (table[pos].key == key) {
                    return table[pos].data;
                }
            }
        }

        return null;
    }

    public List<T> returnAll() {
        List<T> all = new ArrayList<>();

        for (GenericNode<T> n : table) {
            if (n != null) {
                all.add(n.data);
            }
        }

        return all;
    }

    private int hashA(int key) {
        return (Integer.hashCode(key) & 0x7FFFFFFF) % size;
    }

    private int hashB(int key, int hash) {
        return (Integer.hashCode(key) & 0x7FFFFFFF) % hash;
    }

    private int dispersaoDupla(int key, int k) {
        return (hashA(key) + (k * hashB(key, primeLower))) % size;
    }

    private int carga() {
        return quantidadeElementos/size;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        GenericNode<T>[] tableB = table;
        this.size = primeUpper(this.size * 2);
        this.primeLower = primeLower(size);
        this.table = new GenericNode[size];
        this.quantidadeElementos = 0;

        for (GenericNode<T> n : tableB) {
            if (n != null) {
                insert(n.key, n);
            }
        }
    }

    public int getQuantElementos() {
        return quantidadeElementos;
    }

    public int getSize() {
        return size;
    }
}