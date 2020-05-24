import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class RegisterWindow extends JFrame {

    private final JTextField userText;
    private final JPasswordField confirmPasswordText;
    private final JTextField fullNameText;
    private final JPasswordField passwordText;

    public RegisterWindow(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,350);
        setLocation(200,200);
        setResizable(false);
        setLayout(new BorderLayout(10,10));
        this.addWindowListener(new WindowAdapter(){
           @Override
           public void windowClosing(WindowEvent e){
               callLoginWindow();
           }
        });
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
         this.fullNameText = new JTextField(15);
        this.userText = new JTextField(15);
         this.passwordText = new JPasswordField(15);
        this.confirmPasswordText = new JPasswordField(15);
        centerPanel.add(fullNameText);
        centerPanel.add(userText);
        centerPanel.add(passwordText);
        centerPanel.add(confirmPasswordText);

        JPanel bottomPanel= new JPanel(new GridBagLayout());
        GridBagConstraints gbc= new GridBagConstraints();

        gbc.anchor=GridBagConstraints.LINE_START;
        gbc.insets=new Insets(2,0,10,0);
        JButton registerBTN= new JButton("Register");
        registerBTN.setPreferredSize(new Dimension(150,30));
        bottomPanel.add(registerBTN,gbc);

        registerBTN.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent ev){
               if(!isCheckUserNameSequence()){
                   JOptionPane.showMessageDialog(null,"UserName is not correct!","Error",JOptionPane.ERROR_MESSAGE);
                   clearUserText();
               }else if(checkPasswordField()){
                   JOptionPane.showMessageDialog(null,"Password should be the same!","Error",JOptionPane.ERROR_MESSAGE);
                   clearPassText();
               } else if(isFieldEmpty()){
                   JOptionPane.showMessageDialog(null,"All Field should not be empty!","Error",JOptionPane.ERROR_MESSAGE);
               }
               else{
                   handleFileManagement();
               }
           }
        });

        getContentPane().add(westPanel,BorderLayout.WEST);
        getContentPane().add(centerPanel,BorderLayout.CENTER);
        getContentPane().add(bottomPanel,BorderLayout.SOUTH);
        setVisible(true);
    }
    public void handleFileManagement(){
        FileManagement fileManagement=new FileManagement();
        String username=this.userText.getText();
        String fullname=this.fullNameText.getText();
        String password=this.passwordText.getText();
        if (fileManagement.isFileExist()){
            System.out.println("File ton tai");
            if(fileManagement.isUserExist(username)){
                JOptionPane.showMessageDialog(null,"User Already Exist!","Error",JOptionPane.ERROR_MESSAGE);
            }else{
                fileManagement.appendDocument(fullname,username,password);
                JOptionPane.showMessageDialog(null,"Register Completed","Completed",JOptionPane.INFORMATION_MESSAGE);
                System.out.println("them thanh cong");
                callLoginWindow();
            }

        }else{
            System.out.println("File khong ton tai");
            fileManagement.createFile(fullname,username,password);
            JOptionPane.showMessageDialog(null,"Register Completed","Completed",JOptionPane.INFORMATION_MESSAGE);
            callLoginWindow();
        }
    }
    public boolean isCheckUserNameSequence(){
        String expression="[a-z0-9]{1,20}";
        String userName=this.userText.getText();
        Boolean validUserName= Pattern.matches(expression,userName);
        return validUserName;
    }
    public boolean isFieldEmpty(){

        String fullName=this.fullNameText.getText();
        String password=this.passwordText.getText();
        String confirmPassword=this.confirmPasswordText.getText();
        if(fullName.trim().equals("") || password.trim().equals("") || confirmPassword.trim().equals("") ){
            return true;
        }else{
            return false;
        }
    }
    public boolean checkPasswordField(){
        String password=this.passwordText.getText();
        String confirmPassword=this.confirmPasswordText.getText();
        if(!password.trim().equals(confirmPassword.trim())){
            return true;
        }else{
            return false;
        }
    }
    public void clearUserText(){
        this.userText.setText("");
    }
    public void clearPassText(){
        this.passwordText.setText("");
        this.confirmPasswordText.setText("");
    }
    public void callLoginWindow(){
        this.setVisible(false);
        LoginWindow loginWindow=new LoginWindow();
        loginWindow.setVisible(true);
    }

    public static void main(String[] args){
        RegisterWindow registerWindow=new RegisterWindow();
        registerWindow.setVisible(true);

    }

}
