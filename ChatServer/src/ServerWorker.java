import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                }else if("check_register".equalsIgnoreCase(cmd)){
                    String[] tokensMsg = StringUtils.split(line, null, 4);
                    handleCheckRegisterUser(tokensMsg);
                }else if("group".equalsIgnoreCase(cmd)){
                    String[] tokensMsg = StringUtils.split(line, null, 2);
                    handleGroup(tokensMsg);
                }
                else {
                    if (getUser() != null) {
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
    public void handleGroup(String[] tokensMsg){

    }
    public void handleCheckRegisterUser(String[] tokensMsg){
        if(tokensMsg!=null && tokensMsg.length==4){
            String registerFullName=tokensMsg[3];
            String registerUserName=tokensMsg[1];
            String registerPassword=tokensMsg[2];

            System.out.println(registerUserName +" "+registerPassword+" "+registerFullName);

            FileManagement fileManagement=new FileManagement();
            if (fileManagement.isFileExist()){
                System.out.println("File ton tai");
                if(fileManagement.isUserExist(registerUserName)){
                    System.out.println("User already exist. Please register again!");
                    this.sendMessage("register failed");
                }else{
                    fileManagement.appendDocument(registerFullName,registerUserName,registerPassword);
                    this.sendMessage("ok register");
                    System.out.println("them thanh cong");
                }

            }else{
                System.out.println("File khong ton tai");
                fileManagement.createFile(registerFullName,registerUserName,registerPassword);
                this.sendMessage("ok register");

            }
        }
    }

    private void handleDirectMessage(String[] tokensMsg) {
        String sendToUser = tokensMsg[1];
        String bodyMessage = tokensMsg[2];

        ArrayList<ServerWorker> workerList = this.server.getWorkerList();
        if("all".equals(sendToUser)){
            for (ServerWorker worker : workerList) {
                if (worker.getUser() != null) {
                        String msg = "msg " + this.user+" " + bodyMessage;
                        worker.sendMessage(msg);
                }
            }
        }else{
            for (ServerWorker worker : workerList) {
                if (worker.getUser() != null) {
                    if (sendToUser.equalsIgnoreCase(worker.getUser())) {
                        String msg = "msg " + this.user+" " + bodyMessage;
                        worker.sendMessage(msg);
                    }
                }
            }
        }

    }
    public boolean checkUserAndPassword(String userInput,String passwordInput){
        try{
            Path myPath= Paths.get(System.getProperty("user.dir"),"files","users.xml");
            DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=dbFactory.newDocumentBuilder();
            Document doc=builder.parse(String.valueOf(myPath));

            String express="information/user";
            XPath xPath= XPathFactory.newInstance().newXPath();

            NodeList nList= (NodeList)xPath.compile(express).evaluate(doc, XPathConstants.NODESET);
            boolean loginStatus=false;
            for(int i=0;i< nList.getLength();i++){
                Element element=(Element)nList.item(i);
                String elementUserName= element.getElementsByTagName("username").item(0).getTextContent();
                String passwordUserName=element.getElementsByTagName("password").item(0).getTextContent();

                if(elementUserName.equals(userInput.trim()) && passwordUserName.equals(passwordInput.trim())){
                    loginStatus= true;
                    break;
                }
                System.out.println("Login failed with user "+ userInput +" and password "+passwordInput);
            }
            System.out.println(loginStatus);
            return loginStatus;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    private void handleLogin(String[] tokens) {
        if (tokens.length == 3) {
            String user = tokens[1];
            String password = tokens[2];

            if (checkUserAndPassword(user,password)) {

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

                String onlineMsg = "online " + this.user;
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