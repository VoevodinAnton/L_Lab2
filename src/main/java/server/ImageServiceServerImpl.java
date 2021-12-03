package server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ImageServiceServerImpl extends UnicastRemoteObject implements ImageServiceServer{
    private static final String LOCALHOST = "127.0.0.1";
    private static final String RMI_HOSTNAME = "java.rmi.server.hostname";
    private static final String SERVICE_NAME = "ImageService";

    public static final String IMAGE_EXTENSION = "jpg";

    protected ImageServiceServerImpl() throws RemoteException { }

    @Override
    public byte[] processImage(byte[] image) throws IOException {
        return new byte[0];
    }

    public static void main (String[] args) throws Exception
    {
        try {
            System.setProperty(RMI_HOSTNAME, LOCALHOST);
            // Создание удаленного RMI объекта
            ImageServiceServer service = new ImageServiceServerImpl();

            // Определение имени удаленного RMI объекта


            System.out.println("Initializing " + SERVICE_NAME);

            /*
             * Регистрация удаленного RMI объекта BillingService
             * в реестре rmiregistry
             */
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(SERVICE_NAME, service);
            System.out.println("Start " + SERVICE_NAME);
        } catch (RemoteException e) {
            System.err.println("RemoteException : "+e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Exception : " + e.getMessage());
            System.exit(2);
        }
    }
}
