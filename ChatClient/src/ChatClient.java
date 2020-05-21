import jdk.internal.util.xml.impl.Input;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatClient {
    private String serverName;
    private int serverPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private BufferedReader bufferedRead;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();

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



    private boolean login(String user, String password) throws IOException {

        String cmd = "login " + user + " " + password + "\n";
        this.send(cmd);
        String response = this.bufferedRead.readLine();
//        System.out.println("Response Line "+response);
        if ("ok login".equals(response)) {
            startMessageReader();
            return true;
        } else {
            return false;
        }

    }

    private void logoff() throws IOException{
        String cmd = "quit\n";
        this.send(cmd);
    }

    private void startMessageReader() {
        Thread t= new Thread(){
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t.start();
    }

    private void readMessageLoop() {
        try {
            String line;
            while ((line = bufferedRead.readLine()) != null) {
                String[] tokens= StringUtils.split(line);
                if(tokens!=null && tokens.length>0){
                    String cmd=tokens[0];
                    if("online".equalsIgnoreCase(cmd)){
                        handleOnline(tokens);
                    }else if("offline".equalsIgnoreCase(cmd)){
                        handleOffline(tokens);
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private void handleOffline(String[] tokens) {
        String login= tokens[1];
        for (UserStatusListener listener  :  userStatusListeners ){
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) {
        String login= tokens[1];
        for (UserStatusListener listener  :  userStatusListeners ){
            listener.online(login);
        }
    }

    private void readDataFromServer(ChatClient client) {
        try {
            InputStream inputStream = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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

    private void writeDataToServer(ChatClient client) {
        Scanner in = new Scanner(System.in);
        String cmd = "";
        try {
            while (!("quit".equals(cmd))) {
                cmd = in.nextLine();
                String msg = cmd + "\n\r";
                client.send(msg);
            }
            Thread.sleep(1000);
            System.out.println("This client " + client.getLocalPort() + " quit!");
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
            this.bufferedRead = new BufferedReader(new InputStreamReader(this.inputStream));
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

    public void addUserStatusListener(UserStatusListener listener) {
        this.userStatusListeners.add(listener);
    }

    public void removeUserStatusListener(UserStatusListener listener) {
        this.userStatusListeners.remove(listener);
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 9000);
        UserStatusListener listener = new UserStatusListener() {
            @Override
            public void online(String login) {
                System.out.println("ONLINE: "+login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: "+login);
            }
        };
        client.addUserStatusListener(listener);

        if (client.connect()) {
            try {
                System.out.println("Connected successfull with port: " + client.getLocalPort());

                if (client.login("guest", "guest")) {
                    System.out.println("login successful");
                } else {
                    System.err.println("Login failed");
                }
                client.logoff();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Connect failed.");
        }
    }

}