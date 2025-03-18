package com.br.servidorDeAplicacao;

import com.br.entity.ServiceOrder;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;

class Database implements DatabaseInterface {
    public LinkedHashMap<Integer, ServiceOrder> database = new LinkedHashMap<>();

    public synchronized void insert(ServiceOrder order) {
        database.put(order.getCode(), order);
    }

    public synchronized void remove(ServiceOrder order) {
        database.remove(order.getCode());
    }

    public synchronized ServiceOrder search(ServiceOrder order) {
        return database.get(order.getCode());
    }

    @Override
    public synchronized void updateDatabase(List<ServiceOrder> orders) throws RemoteException {
        database.clear();
        for (ServiceOrder order : orders) {
            database.put(order.getCode(), order);
        }
    }

    public synchronized void substitute(ServiceOrder order) {
        database.put(order.getCode(), order);
    }

    public synchronized List<ServiceOrder> getAll() {
        return List.copyOf(database.values());
    }
}
