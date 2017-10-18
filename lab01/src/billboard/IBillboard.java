package billboard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Duration;

public interface IBillboard extends Remote { // host, port, nazwa;
    public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException;
    public boolean removeAdvertisement(int orderId) throws RemoteException;
    public int[] getCapacity() throws RemoteException; // returns table of two integers {totalSlots, freeSlots}
    public void setDisplayInterval(Duration displayInterval) throws RemoteException;
    public boolean start() throws RemoteException;
    public boolean stop() throws RemoteException;
}