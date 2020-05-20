import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient2 {
    private String serverName;
    private int serverPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;

    public ChatClient2(String serverName, int serverPort) {
        this.serverName=serverName;
        this.serverPort=serverPort;
    }

    private InputStream getInputStream() {
        return this.inputStream;
    }

    public static void main(String[] args) {
        ChatClient2 client= new ChatClient2("localhost",9000);
        if (client.connect()){
            try{
                System.out.println("Connected successfull with port: "+client.getLocalPort());
                Scanner in =new Scanner(System.in);
                writeDataToServer(client,in);
                client.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{

        }
    }



    private static void writeDataToServer(ChatClient2 client, Scanner in) {
        String cmd="";
        while(!("quit".equals(cmd))){
            cmd=in.nextLine();
            String msg="From client 2: "+ cmd+"\n\r";
            client.send(msg);
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
    private String getLocalPort() {
        return String.valueOf(this.socket.getLocalPort());
    }
}
