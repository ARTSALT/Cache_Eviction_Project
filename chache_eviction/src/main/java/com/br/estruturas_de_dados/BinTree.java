package com.br.estruturas_de_dados;

import java.util.LinkedList;
import java.util.Queue;

public class BinTree {
    public Node raiz;

    public BinTree() {}

    public BinTree(Node raiz) {
        setRaiz(raiz);
    }

    public final void setRaiz(Node raiz){
        this.raiz = raiz;
    }

    public void preOrdem(){
        preOrdem(raiz);
    }

    private void preOrdem(Node arv) {
        if (arv != null) {
            System.out.print(arv.key + " ");
            preOrdem(arv.esq);
            preOrdem(arv.dir);
        }
    }

    public void ordem(){
        ordem(raiz);
    }

    private void ordem(Node arv) {
        if (arv != null) {
            ordem(arv.esq);
            System.out.print(arv.key + " " + "a");
            ordem(arv.dir);
        }
    }

    public void posOrdem(){
        posOrdem(raiz);
    }

    private void posOrdem(Node arv) {
        if (arv != null) {
            posOrdem(arv.esq);
            posOrdem(arv.dir);
            System.out.print(arv.key + " ");
        }
    }

    public void byDepth() {
        byDepth(raiz);
    }

    public void byDepth(Node arv) {        
        if (arv != null) {
            Queue<Node> fila = new LinkedList<>();
            Node iterator;

            fila.add(arv);

            while(!fila.isEmpty()) {
                iterator = fila.remove();
                System.out.print(iterator.key + " ");

                if(iterator.esq != null) {
                    fila.add(iterator.esq);
                }
                if (iterator.dir != null) {
                    fila.add(iterator.dir);
                }
            }
        } else {
            System.err.println("Nó vazio");
        }
    }
}