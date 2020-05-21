import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerWorker extends Thread{

    private final Socket clientSocket;
    private final Server server;
    private OutputStream outputStream;
    private InputStream inputStream;


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
         this.inputStream=clientSocket.getInputStream();
         this.outputStream=clientSocket.getOutputStream();
        BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String line;
        while ((line=reader.readLine())!=null ){
            outputStream.write((line+"\n\r").getBytes());
            System.out.println(line);
            if("quit".equalsIgnoreCase(line)){
                handleLogoff();
                break;
            }else{
                ArrayList<ServerWorker> workerList= this.server.getWorkerList();
                for (ServerWorker worker : workerList){
                    worker.sendMessage(line);
                }
            }
        }
        clientSocket.close();
    }

    private void handleLogoff() {
            System.out.println("worker size: "+this.server.getWorkerList().size());
            this.server.removeWorker(this);
            System.out.println("new worker size: "+this.server.getWorkerList().size());
    }
    public void sendMessage(String msg){
        try {
            this.outputStream.write((msg+"\n\r").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
