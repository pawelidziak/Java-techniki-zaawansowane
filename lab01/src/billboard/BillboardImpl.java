package billboard;

import others.Advertisement;
import others.Configuration;
import manager.IManager;
import others.Server;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BillboardImpl extends UnicastRemoteObject implements IBillboard, Serializable {

    private BillboardGUI billboardGUI;
    private IManager manager;

    private final List<Advertisement> advertisementList = Collections.synchronizedList(new ArrayList<>());
    private boolean isRunning = false;
    private Duration displayInterval = Configuration.DISPLAY_INTERVAL;
    private int billboardID;


    private BillboardImpl() throws RemoteException, MalformedURLException, NotBoundException {
        manager = (IManager) Server.getService(Configuration.REMOTE_MANAGER);
        billboardID = manager.bindBillboard(this);
        openGUI();
    }

    private void openGUI() {
        billboardGUI = new BillboardGUI(this);
        billboardGUI.setVisible(true);
    }

    public static void main(String args[]) throws Exception {
        new BillboardImpl();
    }

    @Override
    public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {
        // jesli mam zamknac lub wielkosc przekracza MAX_CAPACITY to false
        if (advertisementList.size() >= Configuration.BILLBOARD_MAX_CAPACITY) {
            return false;
        }

        // w przeciwnym wypadku dodaje do listy
        advertisementList.add(new Advertisement(orderId, advertText, displayPeriod));
        System.out.println("dodaje advert ");
//        if (isRunning && !lock) {
//            display();
//        }
        return true;
    }

    @Override
    public boolean removeAdvertisement(int orderId) throws RemoteException {
        for (Advertisement advert : advertisementList) {
            if (advert.getOrderId() == orderId) {
                advertisementList.remove(advert);
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] getCapacity() throws RemoteException {
        return new int[]{Configuration.BILLBOARD_MAX_CAPACITY, Configuration.BILLBOARD_MAX_CAPACITY - advertisementList.size()}; //total / free
    }

    @Override
    public void setDisplayInterval(Duration displayInterval) throws RemoteException {
        this.displayInterval = displayInterval;
        System.out.println(displayInterval.toString());
    }

    @Override
    public boolean start() throws RemoteException {
        if (isRunning) return false;
        System.out.println("Billboard START.");

        isRunning = true;
        display();
        return true;
    }

    @Override
    public boolean stop() throws RemoteException {
        if (!isRunning) return false;
        System.out.println("Billboard STOP.");

        isRunning = false;
        advertisementList.clear();
        return true;
    }

    private synchronized void display() {
        isRunning = true;
        Thread thread = new Thread(() -> {
            while (isRunning) {
                List<Advertisement> tmpList = new ArrayList<>(advertisementList);

                for (Advertisement advert : tmpList) {

                    if (!advert.getDisplayPeriod().minus(displayInterval).isNegative()) {
                        billboardGUI.updateAdvert(advert.getAdvertText());
                        try {
                            Thread.sleep(displayInterval.toMillis());
                        } catch (InterruptedException ignore) {
                        }

                        billboardGUI.clear();
                        advert.setDisplayPeriod(advert.getDisplayPeriod().minus(displayInterval));
                        if (advert.getDisplayPeriod().toMillis() < displayInterval.toMillis()) {
                            advertisementList.remove(advert);
                            System.out.println("usuwam");
                            if (advertisementList.isEmpty()) {
                                billboardGUI.updateAdvert("No adverts.");
                                try {
                                    Thread.sleep(100);
                                } catch (Exception ignore) {

                                }
                                break;
                            }
                        }
                    }
                }
            }
        });
        thread.start();
    }

    public void close() {
        try {
            manager.unbindBillboard(billboardID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


}