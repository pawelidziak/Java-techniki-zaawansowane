package others;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    private static Registry registry;

    public static void startServer(){
        try {
            registry = LocateRegistry.createRegistry(Configuration.PORT);
            System.out.println("Register on port: " + Configuration.PORT);
        } catch (Exception ex) {
            System.out.println("Registry Server Error " + ex.toString());
        }
    }

    public static <T extends Remote> void registerService(T obj, String remoteName) {
        try {
            registry.bind(remoteName, obj);
            System.out.println("Service " + remoteName + " registered");
        } catch (Exception ex) {
            System.out.println("Register Service Error" + ex.toString());
        }

    }

    public static void unregisterService(String remoteName) {
        try {
            //UnicastRemoteObject.unexportObject(obj, true);
            registry.unbind(remoteName);
            System.out.println("Service " + remoteName + " unregistered");
        } catch (Exception ex) {
            System.out.println("Unregister Service Error" + ex.toString());
        }
    }

    // method return a reference to a remote object
    public static Object getService(String remoteName) {
        try {
            Registry r = LocateRegistry.getRegistry(Configuration.PORT);
            return r.lookup(remoteName);
        } catch (Exception ex) {
            return null;
        }
    }

}