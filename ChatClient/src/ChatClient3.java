import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient3 {
    private String serverName;
    private int serverPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;

    public ChatClient3(String serverName, int serverPort) {
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
        ChatClient3 client = new ChatClient3("localhost", 9000);
        if (client.connect()) {
            try {
                System.out.println("Connected successfull with port: " + client.getLocalPort());

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

    private static void readDataFromServer(ChatClient3 client) {
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

    private static void writeDataToServer(ChatClient3 client) {
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
