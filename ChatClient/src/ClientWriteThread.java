import java.net.Socket;
import java.util.Scanner;

public class ClientWriteThread extends Thread{

    private final ChatClient client;
    private final Socket socket;

    public ClientWriteThread(ChatClient client, Socket socket){
        this.client=client;
        this.socket=socket;
    }
    @Override
    public void run(){
        writeDataToServer();
    }

    private  void writeDataToServer() {
        Scanner in =new Scanner(System.in);
        String cmd="";
        while(!("quit".equals(cmd))){
            cmd=in.nextLine();
            String msg="From client 1: "+ cmd+"\n\r";
            client.send(msg);
        }
    }
}
