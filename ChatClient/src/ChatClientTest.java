import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatClientTest {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private OutputStream serverOut;
    private InputStream serverIn;

    public ChatClientTest(String serverName, int serverPort) {
        this.serverName=serverName;
        this.serverPort=serverPort;
    }

    public static void main(String[] args) {
        ChatClientTest client=new ChatClientTest("localhost",9000);
        if(!client.connect()){
            System.err.println("Connected Failed");
        }else{
            System.out.println("Connected Successful");
            client.login("guest","guest");
            client.close();
        }
    }

    private void login(String user, String password) {
        String cmd ="login"+ user +" "+password+"\n";
        try {
            this.serverOut.write(cmd.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean connect() {
        try {
            this.socket=new Socket(serverName,serverPort);
            System.out.println("Client port is: "+this.socket.getLocalPort());
            this.serverOut=socket.getOutputStream();
            this.serverIn=socket.getInputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void close(){
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
