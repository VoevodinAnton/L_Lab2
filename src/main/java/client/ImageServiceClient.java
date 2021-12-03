package client;

import server.ImageServiceServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;

public class ImageServiceClient {
    private static final String localhost    = "127.0.0.1";
    private static final String RMI_HOSTNAME = "java.rmi.server.hostname";
    private static final String SERVICE_PATH = "rmi://localhost/ImageService";

    public static byte[] processImage(String imagePath) throws IOException, NotBoundException {
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        System.setProperty(RMI_HOSTNAME, localhost);
        String objectName = SERVICE_PATH;

        ImageServiceServer imageServiceServer = (ImageServiceServer) Naming.lookup(objectName);

        //byte[] processedImage = imageServiceServer.processImage(imageBytes);

        return imageBytes;
    }

    public static void writeImageToFile(byte[] image, String outputImagePath) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(image);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, App.IMAGE_EXTENSION, new File(outputImagePath));
    }
}
