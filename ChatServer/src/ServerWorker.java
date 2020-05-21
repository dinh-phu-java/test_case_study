import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String user = null;


    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public String getUser() {
        return this.user;
    }

    @Override
    public void run() {
        try {
            handleSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSocket() throws IOException {
        this.inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
//            outputStream.write((line + "\n\r").getBytes());
            System.out.println(line);
            String[] tokens = StringUtils.split(line);

            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(line)) {
                    handleLogoff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(tokens);
                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleDirectMessage(tokensMsg);
                } else {
                    if (getUser() != null) {
//
                        sendMessage(line);
                    } else {
                        String msg = "Please Login!";
                        sendMessage(msg);
                    }
                }
            }
        }
        clientSocket.close();
    }

    private void handleDirectMessage(String[] tokensMsg) {
        String sendToUser = tokensMsg[1];
        String bodyMessage = tokensMsg[2];
        ArrayList<ServerWorker> workerList = this.server.getWorkerList();
        for (ServerWorker worker : workerList) {
            if (worker.getUser() != null) {
                if (sendToUser.equalsIgnoreCase(worker.getUser())) {
                    String msg = this.user + " typed: " + bodyMessage;
                    worker.sendMessage(msg);
                }
            }
        }
    }

    private void handleLogin(String[] tokens) {
        if (tokens.length == 3) {
            String user = tokens[1];
            String password = tokens[2];
            if (("guest".equals(user) && "guest".equals(password)) || ("jim".equals(user) && "jim".equals(password)) || ("phu".equals(user) && "phu".equals(password))) {

                String msg = "Login Successful with: " + user;
                System.out.println(msg);
                this.user = user;
                this.sendMessage("ok login");

                ArrayList<ServerWorker> workerList = this.server.getWorkerList();

                String onlineAll;
                for (ServerWorker worker : workerList) {
                    if (worker.getUser() != null) {
                        onlineAll = "online " + worker.getUser();
                        this.sendMessage(onlineAll);
                    }
                }

                String onlineMsg = "online: " + this.user;
                for (ServerWorker worker : workerList) {
                    if (worker.getUser() != null) {
                        if (this.user != worker.getUser()) {
                            worker.sendMessage(onlineMsg);
                        }
                    }
                }
            } else {
                this.sendMessage("Incorrect User or Password\n\r");
            }
        } else {
            this.sendMessage("Please filled user and password\n\r");
        }
    }

    private void handleLogoff() {

        System.out.println("old worker size: " + this.server.getWorkerList().size());
        this.server.removeWorker(this);
        ArrayList<ServerWorker> workerList = this.server.getWorkerList();
        System.out.println("new worker size: " + workerList.size());

        String offLineUser = this.getUser();
        for (ServerWorker worker : workerList) {
            if (worker.getUser() != null) {
                String offlineMsg = "offline " + offLineUser;
                worker.sendMessage(offlineMsg);
            }
        }
    }

    public void sendMessage(String msg) {
        try {
            this.outputStream.write((msg + "\n\r").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}