import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private String serverName;
    private int serverPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private BufferedReader bufferedRead;

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    private InputStream getInputStream() {
        return this.inputStream;
    }

    private String getLocalPort() {
        return String.valueOf(this.socket.getLocalPort());
    }

    private Socket getSocket() {
        return this.socket;
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 9000);
        if (client.connect()) {
            try {
                System.out.println("Connected successfull with port: " + client.getLocalPort());

                if(client.login("guest","guest")) {
                    System.out.println("login successful");
                }else{
                    System.err.println("Login failed");
                }

                Thread readThread = new Thread() {
                    @Override
                    public void run() {
                        readDataFromServer(client);
                    }
                };
                Thread writeThread = new Thread() {
                    @Override
                    public void run() {
                        writeDataToServer(client);
                    }
                };
                readThread.start();
                writeThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Can't connect to server");
        }
    }

    private boolean login(String user, String password) throws IOException {

        String cmd ="login "+user+" "+password+"\n";
        this.send(cmd);
        String response=this.bufferedRead.readLine();
//        System.out.println("Response Line "+response);
        if("ok login".equals(response)){
            return true;
        }else{
            return false;
        }

    }

    private static void readDataFromServer(ChatClient client) {
        try {
            InputStream inputStream = client.getInputStream();
            int bytes;
            while ((bytes = inputStream.read()) != -1) {
                if (!(bytes == 10)) {
                    System.out.print((char) bytes);
                } else {
                    System.out.println();
                }
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeDataToServer(ChatClient client) {
        Scanner in = new Scanner(System.in);
        String cmd = "";
        try {
            while (!("quit".equals(cmd))) {
                cmd = in.nextLine();
                String msg = cmd + "\n\r";
                client.send(msg);
            }
                Thread.sleep(1000);
                System.out.println("This client "+client.getLocalPort()+" quit!");
                client.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
            this.bufferedRead =new BufferedReader(new InputStreamReader(this.inputStream));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void send(String msg) {
        try {
            this.outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
