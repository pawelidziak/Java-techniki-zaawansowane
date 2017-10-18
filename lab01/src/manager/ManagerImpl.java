package manager;

import billboard.IBillboard;
import others.Configuration;
import others.Order;
import others.Server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManagerImpl extends UnicastRemoteObject implements IManager {

    private ManagerGUI managerGUI;
    private List<IBillboard> billboards = Collections.synchronizedList(new ArrayList<>());
    private int counterForOrders = 0;

    private ManagerImpl() throws RemoteException {
        super(Configuration.PORT);
        openGUI();
    }

    public static void main(String args[]) throws Exception {
        Server.startServer();
        Server.registerService(new ManagerImpl(), Configuration.REMOTE_MANAGER);
    }

    private void openGUI() {
        managerGUI = new ManagerGUI(this);
        managerGUI.setVisible(true);
    }

    @Override
    public int bindBillboard(IBillboard billboard) throws RemoteException {
        billboards.add(billboard);
        //billboard.start();
        System.out.println("Billboard bind");
        System.out.println("b size: " + billboards.size());
        managerGUI.updateBillboardsList(billboards);
        return billboards.indexOf(billboard);
    }

    @Override
    public boolean unbindBillboard(int billboardId) throws RemoteException {
        IBillboard billboard = billboards.get(billboardId);
        if (billboard != null) {
            boolean delete = billboards.remove(billboard);
            managerGUI.updateBillboardsList(billboards);
            System.out.println("Billboard unbind");
            return delete;
        } else {
            return false;
        }
    }

    @Override
    public boolean placeOrder(Order order) throws RemoteException {
        if (!billboards.isEmpty()) {
            for (IBillboard billboard : billboards) {
                if (billboard.addAdvertisement(order.getAdvertText(), order.getDisplayPeriod(), counterForOrders)) {
                    order.getClient().setOrderId(counterForOrders);
                    System.out.println("Dodano zamowienie. ID: " + counterForOrders + ": " + order.getAdvertText());
                } else {
                    System.out.println("All billboard slots are busy.");
                    return false;
                }
            }
            counterForOrders++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean withdrawOrder(int orderId) throws RemoteException {
        int tmp = billboards.size();
        for (IBillboard billboard : billboards) {
            billboard.removeAdvertisement(orderId);
        }
        int tmp1 = billboards.size();
        return tmp != tmp1;
    }

}