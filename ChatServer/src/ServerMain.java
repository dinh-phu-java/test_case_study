import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Date;

public class ServerMain {
    public static void main(String[] args) throws IOException {
            int port= 9000;
            Server server= new Server(port);
            server.start();
    }
}
