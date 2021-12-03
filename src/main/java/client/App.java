package client;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;

public class App {
    private static final String INPUT_PATH = "src/main/resources/images/";
    private static final String OUTPUT_PATH = "src/main/resources/images/";
    public static final String IMAGE_EXTENSION = "jpg";

    public static void main(String[] args)  {

        if (args == null || args.length < 2){
            System.err.println("Invalid input parameters");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        System.out.println("Имя файла исходного изображения: " + inputFileName);
        System.out.println("Имя файла обработанного изображения: " + outputFileName);

        try {
            if (new File(INPUT_PATH + inputFileName +  "." + IMAGE_EXTENSION).exists()){
                byte[] outputImage = ImageServiceClient.processImage(INPUT_PATH + inputFileName +  "." + IMAGE_EXTENSION);
                ImageServiceClient.writeImageToFile(outputImage, OUTPUT_PATH + outputFileName + ".jpg");
            }
        } catch (IOException | NotBoundException ex){
            System.err.println("Error writing file");
            ex.printStackTrace();
        }
    }
}
