import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;

public class ServerMain {
    public static void main(String[] args) throws IOException {
            int port= 9000;
            ServerSocket serverSocket= new ServerSocket(port);

            while (true){
                System.out.println("wating for connecting");
                Socket clientSocket=serverSocket.accept();
                System.out.println("Connected Successfull with: "+clientSocket.getRemoteSocketAddress());
                InputStream inputStream=clientSocket.getInputStream();
                OutputStream outputStream=clientSocket.getOutputStream();
                BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line=reader.readLine())!=null ){
                    System.out.println(line);
                }
                clientSocket.close();
            }

    }
}
