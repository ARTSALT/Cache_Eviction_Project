package com.br.servidorDeProxy;

import com.br.entity.ServiceOrder;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CacheInterface extends Remote {
    public void insert(ServiceOrder order) throws RemoteException;

    public void remove(ServiceOrder order) throws RemoteException;

    public Object search(ServiceOrder order) throws RemoteException;

    public boolean substitute(ServiceOrder order) throws RemoteException;

    public boolean contains(ServiceOrder order) throws RemoteException;
    public void ping() throws RemoteException;
}
