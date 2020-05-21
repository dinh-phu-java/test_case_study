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
            outputStream.write((line + "\n\r").getBytes());
            System.out.println(line);
            String[] tokens = StringUtils.split(line);

            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(line)) {
                    handleLogoff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(tokens);
                } else {

                    ArrayList<ServerWorker> workerList = this.server.getWorkerList();
                    for (ServerWorker worker : workerList) {
                            if(worker.getUser() != null ){
                                if(worker.getUser() != this.user){
                                    worker.sendMessage(line);
                                }
                            }
                    }
                }
            }
        }
        clientSocket.close();
    }

    private void handleLogin(String[] tokens) {
        if (tokens.length == 3) {
            String user = tokens[1];
            String password = tokens[2];
            if (("guest".equals(user) && "guest".equals(password)) || ("jim".equals(user) && "jim".equals(password)) || ("phu".equals(user) && "phu".equals(password))) {

                String msg = "Login Successful with: " + user;
                System.out.println(msg);
                this.user = user;
                this.sendMessage(msg);

                ArrayList<ServerWorker> workerList = this.server.getWorkerList();

                String onlineAll;
                for (ServerWorker worker : workerList){
                    if(worker.getUser() != null){
                        onlineAll="Online "+worker.getUser();
                        this.sendMessage(onlineAll);
                    }
                }

                String onlineMsg="Online: "+this.user;
                for (ServerWorker worker : workerList){
                    if (worker.getUser()!=null){
                        if(this.user != worker.getUser()){
                            worker.sendMessage(onlineMsg);
                        }
                    }
                }
            } else {

            }
        }
    }

    private void handleLogoff() {
        System.out.println("worker size: " + this.server.getWorkerList().size());
        this.server.removeWorker(this);
        System.out.println("new worker size: " + this.server.getWorkerList().size());
    }

    public void sendMessage(String msg) {
        try {
            this.outputStream.write((msg + "\n\r").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}