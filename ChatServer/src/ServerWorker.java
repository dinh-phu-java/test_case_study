import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerWorker extends Thread{

    private final Socket clientSocket;
    private final Server server;

    public ServerWorker(Server server, Socket clientSocket) {
        this.server=server;
        this.clientSocket=clientSocket;
    }

    @Override
    public void run(){
        try {
            handleSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSocket() throws IOException {
        InputStream inputStream=clientSocket.getInputStream();
        OutputStream outputStream=clientSocket.getOutputStream();
        BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String line;
        while ((line=reader.readLine())!=null ){
            outputStream.write((line+"\n\r").getBytes());
            System.out.println(line);
            if("quit".equalsIgnoreCase(line)){
                handleLogoff();
                break;
            }else{
                
            }
        }
        clientSocket.close();
    }

    private void handleLogoff() {
            System.out.println("worker size: "+this.server.getWorkerList().size());
            this.server.removeWorker(this);
            System.out.println("new worker size: "+this.server.getWorkerList().size());
    }

}
