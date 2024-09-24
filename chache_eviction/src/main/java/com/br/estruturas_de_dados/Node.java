package com.br.estruturas_de_dados;

import com.br.entity.ServiceOrder;

public class Node {
    int key;
    public int h;
    ServiceOrder data;
    Node esq, dir;

    public Node(ServiceOrder so) {
        this.key = so.getCode();
        this.data = so;
        esq = null;
        dir = null;
    }

    public Node(ServiceOrder so, Node e, Node d) {
        data = so;
        esq = e;
        dir = d;
    }
}