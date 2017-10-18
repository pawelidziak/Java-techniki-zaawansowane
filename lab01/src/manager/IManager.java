package manager;

import billboard.IBillboard;
import others.Order;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IManager extends Remote {  // port, nazwa, GUI
    public int bindBillboard(IBillboard billboard) throws RemoteException;
    public boolean unbindBillboard(int billboardId) throws RemoteException;

    public boolean placeOrder(Order order) throws RemoteException;
    public boolean withdrawOrder(int orderId) throws RemoteException;
}