import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Date;

public class ServerMain {
    public static void main(String[] args) throws IOException {
            int port= 9000;
            ServerSocket serverSocket= new ServerSocket(port);

            while (true){
                System.out.println("wating for connecting");
                Socket clientSocket=serverSocket.accept();
                System.out.println("Connected Successfull with: "+clientSocket.getRemoteSocketAddress());

                ServerWorker worker = new ServerWorker(clientSocket);
                worker.start();

//                OutputStream serverOut= clientSocket.getOutputStream();
//                for (int i =0;i<10;i++){
//                    try{
//                        serverOut.write(("Times now is "+new Date()+"\n\r").getBytes());
//                        Thread.sleep(1000);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }

            }
    }


}
