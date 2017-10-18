package client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientImpl extends UnicastRemoteObject implements IClient, Serializable {

    private List<Integer> orderIDs = Collections.synchronizedList(new ArrayList<>());
    private ClientGUI clientGUI;

    private ClientImpl() throws RemoteException {
        super();
        openGUI();
    }

    public static void main(String args[]) throws Exception {
        new ClientImpl();
    }

    private void openGUI() {
        clientGUI = new ClientGUI(this);
        clientGUI.setVisible(true);
    }

    @Override
    public void setOrderId(int orderId) throws RemoteException {
        if (orderIDs.add(orderId)) {
            System.out.println("Dodano zlecenie. ID: " + orderId);
        }
    }

}
