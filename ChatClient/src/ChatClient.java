import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatClient {
    private String serverName;
    private int serverPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;

    public ChatClient(String serverName, int serverPort) {
        this.serverName=serverName;
        this.serverPort=serverPort;
    }

    public static void main(String[] args) {
        ChatClient client= new ChatClient("localhost",9000);
        if (client.connect()){
            System.out.println("Connected successfull!");
            client.send("hello server");
        }else{

        }
    }

    private boolean connect() {
        try {
            this.socket = new Socket(serverName,serverPort);
            this.inputStream=socket.getInputStream();
            this.outputStream=socket.getOutputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void send(String msg){
        try {
            this.outputStream.write(msg.getBytes());
            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close(){
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
