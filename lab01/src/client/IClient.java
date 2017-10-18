package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote { // host, port, nazwa
    public void setOrderId(int orderId) throws RemoteException;
}
