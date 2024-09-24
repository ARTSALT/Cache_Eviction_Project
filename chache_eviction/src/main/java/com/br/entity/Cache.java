package com.br.entity;

import java.util.LinkedList;
import java.util.List;

public class Cache {
    private final List<ServiceOrder> cache = new LinkedList<>();

    public void insert(ServiceOrder order) {
        if (cache.size() == 20) {
            cache.remove(0);
            cache.add(order);
        } else {
            cache.add(order);
        }
    }

    public void remove(ServiceOrder order) {
        cache.remove(order);
    }

    public ServiceOrder search(int code) {
        if (cache.isEmpty()) {
            // Miss
            return null;
        }

        for (ServiceOrder so : cache) {
            if (so.getCode() == code) {
                // Hit
                return so;
            }
        }

        // Miss
        return null;
    }

    /*
    public boolean search(String name) {
        if (cache.isEmpty()) {
            // Miss
            return false;
        }

        for (ServiceOrder so : cache) {
            if (so.name.equals(name)) {
                // Hit
                return true;
            }
        }

        // Miss
        return false;
    }
    */

    public int searchIndex(int code) {
        if (cache.isEmpty()) {
            // Miss
            return -1;
        }

        int i = 0;

        for (ServiceOrder so : cache) {
            if (so.getCode() == code) {
                // Hit
                return i;
            }
            i++;
        }

        // Miss
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        for (ServiceOrder elem : cache) {
            buf.append("(").append(elem.getCode()).append(", ").append(elem.getName()).append(") ");
        }

        return buf.toString();
    }

    public List<ServiceOrder> returnCache() {
        return cache;
    }

    public void subistituir(ServiceOrder order) {
        try {
            cache.set(searchIndex(order.getCode()), order);   
        } catch (Exception e) {
        }
    } 
}
