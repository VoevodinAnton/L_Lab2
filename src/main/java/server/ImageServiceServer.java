package server;

import java.io.IOException;
import java.rmi.Remote;

public interface ImageServiceServer extends Remote {
    byte[] processImage(byte[] image) throws IOException;
}
