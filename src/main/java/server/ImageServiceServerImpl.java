package server;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class ImageServiceServerImpl extends UnicastRemoteObject implements ImageServiceServer {
    private static final String LOCALHOST = "127.0.0.1";
    private static final String RMI_HOSTNAME = "java.rmi.server.hostname";
    private static final String SERVICE_NAME = "ImageService";

    public static final String IMAGE_EXTENSION = "jpg";

    protected ImageServiceServerImpl() throws RemoteException {
    }

    @Override
    public byte[] processImage(byte[] byteImage) throws IOException, RuntimeException {
        System.out.println("success");

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
        BufferedImage image;
        try {
            image = ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int[][] matrix = getMatrix(image, image.getWidth(), image.getHeight());
        int[] temp = new int[9];
        for (int i = 2; i < image.getWidth() + 2; i++) {
            for (int j = 2; j < image.getHeight() + 2; j++) {
                temp[0] = matrix[i][j];
                temp[1] = matrix[i - 1][j];
                temp[2] = matrix[i - 2][j];
                temp[3] = matrix[i][j - 1];
                temp[4] = matrix[i][j - 2];
                temp[5] = matrix[i + 1][j];
                temp[6] = matrix[i + 2][j];
                temp[7] = matrix[i][j + 1];
                temp[8] = matrix[i][j + 2];
                Arrays.sort(temp);
                output.setRGB(i - 2, j - 2, temp[4]);
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(output, "jpg", byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    public int[][] getMatrix(BufferedImage image, int m, int n) {

        int[][] matrix = new int[m + 4][n + 4];

        for (int i = 0; i < m + 4; i++) {
            for (int j = 0; j < n + 4; j++) {
                boolean flag = (j <= 1) || (i <=1) || (i >= m + 2) || (j >= n + 2);
                if (flag) {
                    matrix[i][j] = 0;
                } else {
                    matrix[i][j] = image.getRGB(i - 2, j - 2);
                }
            }
        }
        return matrix;
    }

    public static void main(String[] args) throws Exception {
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
            System.err.println("RemoteException : " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Exception : " + e.getMessage());
            System.exit(2);
        }
    }
}
