import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LoginWindow3 extends JFrame {
    private final ChatClient client;
    private JTextField loginField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Register");

    public LoginWindow3() {
        super("Window Login");

        this.client = new ChatClient();
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
        registerButton.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent ev){
                callRegisterWindow();
           }
        }) ;

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

    public void callRegisterWindow(){
        this.setVisible(false);
        this.logoff();
        RegisterWindow registerWindow=new RegisterWindow();
        registerWindow.setVisible(true);
    }

    public void callLoginWindow(){
        this.logoff();
        this.setVisible(false);
        LoginWindow3 loginWindow=new LoginWindow3();
        loginWindow.setVisible(true);
    }
    public void logoff() {
        this.client.logoff();
    }

    private void doLogin() {

        try {
            String login = loginField.getText();
            String password = passwordField.getText();
            System.out.println(login);
            System.out.println(password);

            if (client.login(login,password)) {

                UserListPane userListPane = new UserListPane(client);
                JFrame frame = new JFrame(login.toUpperCase());
                Image img=Toolkit.getDefaultToolkit().getImage("icons/codegym.png");
                frame.setIconImage(img);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 600);
                frame.setResizable(false);



                frame.getContentPane().add(userListPane, BorderLayout.CENTER);
                frame.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent ev){
                        logoff();
                    }
                });

                JMenu messengerMenu = new JMenu("Messenger" );
                JMenuItem profile_setting = new JMenuItem("Profile Setting" );
//                    profile_setting. addActionListener(this);
                messengerMenu. add(profile_setting);
                JMenuItem layout_setting = new JMenuItem( "Layout Setting" );
//                    layout_setting. addActionListener(this);
                messengerMenu. add(layout_setting);
                JMenu statusMenu = new JMenu("Status" );
                JMenuItem onlineStatus = new JMenuItem("Online" );
//                    onlineStatus. addActionListener(this);
                statusMenu. add(onlineStatus);
                JMenuItem offlineStatus = new JMenuItem("Offline" );
//                    offlineStatus. addActionListener(this);
                statusMenu. add(offlineStatus);

                JMenuItem busyStatus = new JMenuItem("Busy" );
//                    busyStatus. addActionListener(this);
                statusMenu. add(busyStatus);
                // And finally build a JMenuBar for the application
                JMenuBar mainBar = new JMenuBar();
                mainBar. add(messengerMenu);
                mainBar. add(statusMenu);

                frame.setJMenuBar(mainBar);
                frame.setVisible(true);

                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid user or password!");
                callLoginWindow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginWindow3 loginWindow = new LoginWindow3();
        loginWindow.setVisible(true);
    }
}
