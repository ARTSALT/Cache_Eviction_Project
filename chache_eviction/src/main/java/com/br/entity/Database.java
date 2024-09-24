package com.br.entity;

import java.util.ArrayList;
import java.util.List;

import com.br.estruturas_de_dados.AvlTree;

public class Database {
    public static AvlTree database = new AvlTree();
    public static Cache cache = new Cache();

    public static void insert(ServiceOrder order) {
        database.inserir(order);
        if (cache.search(order.getCode()) == null) {
            cache.insert(order);
        }
        Logger.writeLog("Inserção");
        database.rotacao = false;
    }

    public static void remove(ServiceOrder order) {        
        database.remover(order);
        if (cache.search(order.getCode()) != null) {
            cache.remove(order);
        }
        Logger.writeLog("Remoção");
        database.rotacao = false;
    }

    public static void subistitute(ServiceOrder order) {
        database.subistituir(order);
        cache.subistituir(order);
    }

    public static ServiceOrder search(ServiceOrder order) {
        ServiceOrder buscaNaCache = cache.search(order.getCode());

        if (buscaNaCache != null) {
            return buscaNaCache;
        } else {
            order = database.search(order);
            cache.insert(order);
            return order;
        }
    }

    public static ServiceOrder searchCache(ServiceOrder order) {
        return cache.search(order.getCode());       
    }

    public static List<ServiceOrder> returnAll() {
        List<ServiceOrder> all = new ArrayList<>();

        return database.returnAll(all);
    }

    public List<ServiceOrder> returnCache() {
        return cache.returnCache();
    }
}