import javax.swing.*;
import java.awt.*;

public class RegisterWindow extends JFrame {

    public RegisterWindow(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,350);
        setLocation(200,200);
        setResizable(false);
        setLayout(new BorderLayout(10,10));
        JPanel westPanel= new JPanel(new GridLayout(4,1,5,5));
        JLabel fullNameLabel = new JLabel("Full Name");
        JLabel userLabel = new JLabel("User Name");
        JLabel passLabel = new JLabel("Password ");
        JLabel confirmPassLabel = new JLabel("Confirm Password");
        westPanel.add(fullNameLabel);
        westPanel.add(userLabel);
        westPanel.add(passLabel);
        westPanel.add(confirmPassLabel);

        JPanel centerPanel= new JPanel(new GridLayout(4,1,5,5));
        JTextField fullNameText = new JTextField(15);
        JTextField userText = new JTextField(15);
        JPasswordField passworText = new JPasswordField(15);
        JPasswordField confirmPasswordText = new JPasswordField(15);
        centerPanel.add(fullNameText);
        centerPanel.add(userText);
        centerPanel.add(passworText);
        centerPanel.add(confirmPasswordText);

        JPanel bottomPanel= new JPanel(new GridBagLayout());
        GridBagConstraints gbc= new GridBagConstraints();

        gbc.anchor=GridBagConstraints.LINE_START;
        gbc.insets=new Insets(2,0,10,0);
        JButton registerBTN= new JButton("Register");
        registerBTN.setPreferredSize(new Dimension(150,30));
        bottomPanel.add(registerBTN,gbc);

        getContentPane().add(westPanel,BorderLayout.WEST);
        getContentPane().add(centerPanel,BorderLayout.CENTER);
        getContentPane().add(bottomPanel,BorderLayout.SOUTH);
        setVisible(true);
    }
    public static void main(String[] args){
        RegisterWindow registerWindow=new RegisterWindow();
        registerWindow.setVisible(true);
    }

}
