package com.br.servidorDeAplicacao;

import com.br.entity.ServiceOrder;

import java.util.LinkedHashMap;
import java.util.List;

class Database {
    public static LinkedHashMap<Integer, ServiceOrder> database = new LinkedHashMap<>();

    public static synchronized void insert(ServiceOrder order) {
        database.put(order.getCode(), order);
    }

    public static synchronized void remove(ServiceOrder order) {
        database.remove(order.getCode());
    }

    public static synchronized ServiceOrder search(ServiceOrder order) {
        return database.get(order.getCode());
    }

    public static synchronized void subistitute(ServiceOrder order) {
        database.put(order.getCode(), order);
    }

    public static synchronized List<ServiceOrder> getAll() {
        return List.copyOf(database.values());
    }
}
