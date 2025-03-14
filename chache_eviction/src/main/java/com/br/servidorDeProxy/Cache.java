package com.br.servidorDeProxy;

import com.br.entity.ServiceOrder;

import java.util.LinkedList;

class Cache {
    public static LinkedList<ServiceOrder> cache = new LinkedList<>();

    public static synchronized void insert(ServiceOrder order) {
        if (cache.size() == 30) {
            cache.remove();
            cache.add(order);
        } else {
            cache.add(order);
        }

        //Replicação por Inundação
    }

    public static synchronized void remove(ServiceOrder order) {
        cache.remove(order);

        //Replicação por Inuncação
    }

    public static synchronized ServiceOrder search(ServiceOrder order) {
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

    public static synchronized void subistitute(ServiceOrder order) {
        for (ServiceOrder so : cache) {
            if (so.getCode() == order.getCode()) {
                cache.set(cache.indexOf(so), order);
                return;
            }
        }

        //Replicação por Inuncação
    }

    public static synchronized boolean contains(ServiceOrder order) {
        for (ServiceOrder so : cache) {
            if (so.getCode() == order.getCode()) {
                return true;
            }
        }

        return false;
    }
}
