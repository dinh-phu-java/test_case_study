import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LoginWindow2 extends JFrame {
    private final ChatClient client;
    private JTextField loginField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Register");

    public LoginWindow2() {
        super("Window Login");

        this.client = new ChatClient("localhost", 9000);
        this.client.connect();

        setLayout(new GridLayout(3, 1, 5, 5));
        setSize(450, 200);
        setLocation(600, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image img = Toolkit.getDefaultToolkit().getImage("icons/codegym.png");
        setIconImage(img);

        JPanel inputPane = new JPanel(new BorderLayout(5, 5));
        JLabel inputLabel = new JLabel("User         ");
        inputLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));

        inputPane.add(inputLabel, BorderLayout.WEST);
        loginField.setFont(new Font(Font.SERIF, Font.BOLD, 28));

        inputPane.add(loginField, BorderLayout.CENTER);

        JPanel passwordPane = new JPanel(new BorderLayout(5, 5));
        JLabel passwordLabel = new JLabel("Password ");
        passwordField.setFont(new Font(Font.SERIF, Font.BOLD, 28));
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
        passwordPane.add(passwordLabel, BorderLayout.WEST);
        passwordPane.add(passwordField, BorderLayout.CENTER);

        JPanel btnPane = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        btnPane.setFont(new Font(Font.SERIF, Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 50;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        btnPane.add(loginButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 100;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        btnPane.add(registerButton, gbc);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                logoff();
            }
        });
        getContentPane().add(inputPane);
        getContentPane().add(passwordPane);
        getContentPane().add(btnPane);

    }

    public void logoff() {
        try {
            this.client.logoff();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void doLogin() {
        String login = loginField.getText();
        String password = loginField.getText();

        try {
            if (client.login(login, password)) {

                UserListPane userListPane = new UserListPane(client);
                JFrame frame = new JFrame(login.toUpperCase());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 600);
                frame.getContentPane().add(userListPane, BorderLayout.CENTER);
                frame.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent ev){
                        logoff();
                    }
                });
                frame.setVisible(true);

                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid user or password!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginWindow2 loginWindow = new LoginWindow2();
        loginWindow.setVisible(true);
    }
}