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
    private ArrayList<MessageListener> messageListeners = new ArrayList<>();

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



    public boolean login(String user, String password) throws IOException {

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

    public void logoff() throws IOException{
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
               //System.out.println(line+" dong 75");
                if(tokens!=null && tokens.length>0){
                    String cmd=tokens[0];
                    if("online".equalsIgnoreCase(cmd)){
                        handleOnline(tokens);
                    }else if("offline".equalsIgnoreCase(cmd)){
                        handleOffline(tokens);
                    }else if("msg".equalsIgnoreCase(cmd)){
                        String[] tokensMsg=StringUtils.split(line,null,3);
                        handleMessage(tokensMsg);
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

    private void handleMessage(String[] tokensMsg) {
        String fromLogin= tokensMsg[1];
        String msgBody=tokensMsg[2];
        for (MessageListener listener: messageListeners){
            listener.onMessage(fromLogin,msgBody);
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



    public boolean connect() {
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

    public void msg(String sendTo, String msgBody) {
        String cmd= "msg " + sendTo + " " + msgBody+"\n\r";
        this.send(cmd);
    }

    public void addUserStatusListener(UserStatusListener listener) {
        this.userStatusListeners.add(listener);
    }

    public void removeUserStatusListener(UserStatusListener listener) {
        this.userStatusListeners.remove(listener);
    }

    public void addMessageListener(MessageListener listener){
        this.messageListeners.add(listener);
    }
    public void removeMessageListener(MessageListener listener){
        this.messageListeners.remove(listener);
    }

}