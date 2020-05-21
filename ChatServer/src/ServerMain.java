import java.io.*;


public class ServerMain {
    public static void main(String[] args) throws IOException {
            int port= 9000;
            Server server= new Server(port);
            server.start();
    }
}
