import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{

    private final int serverPort;
    private ArrayList<ServerWorker> workerList=new ArrayList<>();
    public Server(int serverPort) {
        this.serverPort=serverPort;
    }
    public ArrayList<ServerWorker> getWorkerList(){
        return this.workerList;
    }
    public void removeWorker(ServerWorker worker){
        this.workerList.remove(worker);
    }
    @Override
    public void run(){
        try{
            ServerSocket serverSocket= new ServerSocket(serverPort);
            while (true){
                System.out.println("wating for connecting");
                Socket clientSocket=serverSocket.accept();
                System.out.println("Connected Successfull with: "+clientSocket.getRemoteSocketAddress());
                ServerWorker worker = new ServerWorker(this,clientSocket);
                workerList.add(worker);
                System.out.println("Worker List: "+workerList.size());
                worker.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
