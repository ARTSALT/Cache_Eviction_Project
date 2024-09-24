package com.br.estruturas_de_dados;

import java.util.List;

import com.br.entity.ServiceOrder;

public class AvlTree{
    Node raiz = null;
    
    public boolean rotacao = false;
    int counter = 0;

    public void inserir(ServiceOrder v) {
        int ch = v.getCode();
        raiz = inserir(raiz, ch, v);
    }

    private Node inserir(Node arv, int ch, ServiceOrder v) {
        if (arv == null) {
            counter++;
            return new Node(v);
        }

        if (ch < arv.key) {
            arv.esq = inserir(arv.esq, ch, v);
        } else if (ch > arv.key) {
            arv.dir = inserir(arv.dir, ch, v);
        } else {
            return arv;
        }

        arv.h = 1 + maior(altura(arv.esq), altura(arv.dir));

        int fb = obterFB(arv);
        int fbEsq = obterFB(arv.esq);
        int fbDir = obterFB(arv.dir);

        if(fb > 1 && fbEsq >= 0) {
            rotacao = true;

            return rds(arv);
        }
        if (fb < -1 && fbDir <= 0) {
            rotacao = true;

            return res(arv);
        }
        if (fb > 1 && fbEsq < 0) {
            rotacao = true;

            arv.esq = res(arv.esq);
            return rds(arv);
        }
        if (fb < -1 && fbDir > 0) {
            rotacao = true;

            arv.dir = rds(arv.dir);
            return res(arv);
        }

        return arv;
    }

    public void remover(ServiceOrder v) {
        int ch = v.getCode();
        raiz = remover(raiz, ch, v);
    }

    private Node remover(Node arv, int ch, ServiceOrder v) {
        if (arv == null) {
            return arv; 
        }

        if (ch < arv.key) {
            arv.esq = remover(arv.esq, ch, v);
        } else if (ch > arv.key) {
            arv.dir = remover(arv.dir, ch, v);
        } else {
            if (arv.esq == null && arv.dir == null) {
                counter--;
                arv = null;
            } else if (arv.esq == null) {
                counter--;
                Node temp = arv;
                arv = temp.dir;
                temp = null;
            } else if (arv.dir == null) {
                counter--;
                Node temp = arv;
                arv = temp.esq;
                temp = null;
            } else {
                Node temp = menorChave(arv.dir);

                arv.key = temp.key;
                arv.data = temp.data;
                temp.key = ch;

                arv.dir = remover(arv.dir, ch, v);
            }
        }

        if (arv == null) {
            return arv;
        }

        arv.h = 1 + maior(altura(arv.esq), altura(arv.dir));

        int fb = obterFB(arv);
        int fbEsq = obterFB(arv.esq);
        int fbDir = obterFB(arv.dir);

        if(fb > 1 && fbEsq >= 0) {
            rotacao = true;

            return rds(arv);
        }
        if (fb < -1 && fbDir <= 0) {
            rotacao = true;

            return res(arv);
        }
        if (fb > 1 && fbEsq < 0) {
            rotacao = true;

            arv.esq = res(arv.esq);
            return rds(arv);
        }
        if (fb < -1 && fbDir > 0) {
            rotacao = true;

            arv.dir = rds(arv.dir);
            return res(arv);
        }

        return arv;
    }
    
    public ServiceOrder search(ServiceOrder element) {
        return search(element, raiz);
    }

    private ServiceOrder search(ServiceOrder element, Node newParent) {
        Node parent = newParent;

        if (parent == null) {
            return null;
        }

        if (parent.key == element.getCode()) {
            return parent.data;
        } else if (element.getCode() < parent.key) {
            return search(element, parent.esq);
        } else {
            return search(element, parent.dir);
        }
    }

    public void subistituir(ServiceOrder v) {
        if (search(v) != null) {
            int ch = v.getCode();
            raiz = subistituir(raiz, ch, v);
        }
    }

    private Node subistituir(Node arv, int ch, ServiceOrder v) {

        if (ch < arv.key) {
            arv.esq = subistituir(arv.esq, ch, v);
        } else if (ch > arv.key) {
            arv.dir = subistituir(arv.dir, ch, v);
        } else {
            arv.data = v;
        }

        return arv;
    }

    int maior(int p, int s) {
        return (p > s) ? p : s;
    }

    private int obterFB(Node arv) {
        if (arv == null) {
            return 0;
        }

        return altura(arv.esq) - altura(arv.dir);
    }

    private int altura(Node arv) {
        if (arv == null) {
            return -1;
        }

        return arv.h;
    }

    private Node menorChave(Node arv) {
        Node temp = arv;

        if (temp == null) {
            return null;
        }

        while (temp.esq != null) {
            temp = temp.esq;
        }

        return temp;
    }

    Node rds(Node y) {
        Node x = y.esq;
        Node z = x.dir;

        x.dir = y;
        y.esq = z;

        y.h = maior(altura(y.esq), altura(y.dir)) + 1;
        x.h = maior(altura(x.esq), altura(x.dir)) + 1;

        return x;        
    }

    Node res(Node x) {
        Node y = x.dir;
        Node z = y.esq;

        y.esq = x;
        x.dir = z;

        x.h = maior(altura(x.esq), altura(x.dir)) + 1;
        y.h = maior(altura(y.esq), altura(y.dir)) + 1;

        return y;
    }

    public void ordem(){
        ordem(raiz);
    }

    private void ordem(Node arv) {
        if (arv != null) {
            ordem(arv.esq);
            System.out.print(arv.data + " ");
            ordem(arv.dir);
        }
    }

    public List<ServiceOrder> returnAll(List<ServiceOrder> all) {
        return returnAll(all, raiz);
    }

    private List<ServiceOrder> returnAll(List<ServiceOrder> all, Node e) {
        if (e != null) {
            returnAll(all, e.esq);
            all.add(e.data);
            returnAll(all, e.dir);
        }

        return all;
    }

    public int height() {
        return raiz.h;
    }

    public int elements() {
        return counter;
    }
}
