package com.br.servidorDeProxy;

import com.br.entity.ServiceOrder;

import java.rmi.RemoteException;
import java.util.LinkedList;

class Cache implements CacheInterface {
    public LinkedList<ServiceOrder> cache = new LinkedList<>();

    public Cache() throws RemoteException {
        super();
    }

    @Override
    public synchronized void insert(ServiceOrder order) {
        if (cache.size() == 30) {
            cache.remove();
            cache.add(order);
        } else {
            cache.add(order);
        }

        //Replicação por Inundação
    }

    @Override
    public synchronized void remove(ServiceOrder order) {
        cache.remove(order);

        //Replicação por Inuncação
    }

    @Override
    public synchronized ServiceOrder search(ServiceOrder order) {
        if (cache.isEmpty()) {
            // Miss
            return null;
        }

        for (ServiceOrder so : cache) {
            if (so.getCode() == order.getCode()) {
                // Hit
                return so;
            }
        }

        // Miss
        return null;
    }

    @Override
    public synchronized boolean substitute(ServiceOrder order) {
        for (ServiceOrder so : cache) {
            if (so.getCode() == order.getCode()) {
                cache.set(cache.indexOf(so), order);
                return true;
            }
        }

        return false;

        //Replicação por Inuncação
    }

    @Override
    public boolean contains(ServiceOrder order) throws RemoteException {
        for (ServiceOrder so : cache) {
            if (so.getCode() == order.getCode()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void ping() throws RemoteException {}
}
