package com.br.servidorDeAplicacao;

import com.br.entity.ServiceOrder;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DatabaseInterface extends Remote {
    public void insert(ServiceOrder order) throws RemoteException;
    public void remove(ServiceOrder order) throws RemoteException;
    public void substitute(ServiceOrder order) throws RemoteException;
    public ServiceOrder search(ServiceOrder order) throws RemoteException;
    public void updateDatabase(List<ServiceOrder> orders) throws RemoteException;
    public List<ServiceOrder> getAll() throws RemoteException;
}
