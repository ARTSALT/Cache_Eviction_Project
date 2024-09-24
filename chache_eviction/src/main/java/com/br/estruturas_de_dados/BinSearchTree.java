package com.br.estruturas_de_dados;

public class BinSearchTree extends BinTree{

    public BinSearchTree() {
        super();
    }

    public BinSearchTree(Node newRaiz) {
        super(newRaiz);
    }

    public void insert(Node element) {
        Node parent = raiz;

        // Checar P
        if (parent == null) {
            raiz  = element;
            return;
        }
        
        // Comparar com P
        if (parent.key > element.key) {
            if (parent.esq == null) {
                parent.esq = element;
            } else {
                insert(element, parent.esq);
            }
        } else if (parent.key < element.key) {
            if (parent.dir == null) {
                parent.dir = element;
            } else {
                insert(element, parent.dir);
            }
        }
    }

    private boolean insert(Node element, Node newParent) {
        Node parent = newParent;

        // Checar P
        if (parent == null) {
            raiz  = element;
            return true;
        }
        
        // Comparar com P
        if (parent.key > element.key) {
            if (parent.esq == null) {
                parent.esq = element;
            } else {
                insert(element, parent.esq);
            }
            return true;
        } else if (parent.key < element.key) {
            if (parent.dir == null) {
                parent.dir = element;
            } else {
                insert(element, parent.dir);
            }
            return true;
        }
        return false;
    }

    public boolean search(int element) {
        Node parent = raiz;

        if (parent == null) {
            return false;
        }

        if (parent.key == element) {
            return true;
        } else if (element < parent.key) {
            return search(element, parent.esq);
        } else {
            return search(element, parent.dir);
        }
    }

    private boolean search(int element, Node newParent) {
        Node parent = newParent;

        if (parent == null) {
            return false;
        }

        if (parent.key == element) {
            return true;
        } else if (element < parent.key) {
            return search(element, parent.esq);
        } else {
            return search(element, parent.dir);
        }
    }
}
