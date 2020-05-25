import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ServerMain extends JFrame {
    private Server server;
    private final int port=9000;
    public ServerMain(){
        this.server=new Server(port);
        server.start();

        setLayout(new FlowLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/codegym.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(300,300);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        JLabel label=new JLabel("WINDOW SERVER: DO NOT CLOSE IT!");
        getContentPane().add(label);

    }
    public static void main(String[] args) throws IOException {
           ServerMain myServerMain=new ServerMain();
           myServerMain.setVisible(true);
    }
}
