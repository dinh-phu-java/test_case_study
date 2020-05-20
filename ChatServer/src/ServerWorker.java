import java.io.*;
import java.net.Socket;

public class ServerWorker extends Thread{

    private final Socket clientSocket;

    public ServerWorker(Socket clientSocket) {
        this.clientSocket=clientSocket;
    }

    @Override
    public void run(){
        try {
            handleSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSocket() throws IOException {
        InputStream inputStream=clientSocket.getInputStream();
        OutputStream outputStream=clientSocket.getOutputStream();
        BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line=reader.readLine())!=null ){
            outputStream.write((line+"\n\r").getBytes());
        }
        clientSocket.close();
    }

}
