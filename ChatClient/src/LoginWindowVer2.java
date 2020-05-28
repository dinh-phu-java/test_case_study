import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class LoginWindowVer2 extends JFrame {
	private final ChatClient client;
	private JPanel contentPane;
	private JTextField loginField;
	private JPasswordField passwordField;

	int xx,xy;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		LoginWindowVer2 loginWindow = new LoginWindowVer2();
		loginWindow.setVisible(true);
	}


	// going to borrow code from a gist to move frame.


	/**
	 * Create the frame.
	 */
	public LoginWindowVer2() {
		this.client=new ChatClient();
		this.client.connect();
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 729, 476);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(191, 75, 29));
		panel.setBounds(0, 0, 346, 490);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Codegym Messenger");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setForeground(new Color(250, 247, 247));
		lblNewLabel.setBounds(75, 305, 200, 27);
		panel.add(lblNewLabel);
		
		JLabel label = new JLabel("");
		
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				 xx = e.getX();
			     xy = e.getY();
			}
		});
		label.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				
				int x = arg0.getXOnScreen();
	            int y = arg0.getYOnScreen();
				LoginWindowVer2.this.setLocation(x - xx, y - xy);
			}
		});
		label.setBounds(-20, 0, 420, 275);
		label.setVerticalAlignment(SwingConstants.TOP);
		ImageIcon img= new ImageIcon("icons/bg.jpg");
		label.setIcon(img);
		panel.add(label);
		
		JLabel lblWeGotYou = new JLabel("Hãy nói theo cách của bạn");
		lblWeGotYou.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeGotYou.setForeground(new Color(219, 238, 233));
		lblWeGotYou.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblWeGotYou.setBounds(80, 343, 191, 27);
		panel.add(lblWeGotYou);

		JLabel lblWeGotYou1 = new JLabel("Và trả tiền theo cách của chúng tôi");
		lblWeGotYou1.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeGotYou1.setForeground(new Color(229, 239, 237));
		lblWeGotYou1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblWeGotYou1.setBounds(60, 363, 230, 27);
		panel.add(lblWeGotYou1);
		
		Button registerButton = new Button("SignUp");
		registerButton.setForeground(Color.WHITE);
		registerButton.setBackground(new Color(241, 57, 83));
		registerButton.setBounds(395, 363, 283, 36);
		contentPane.add(registerButton);

		Button loginButton = new Button("Login");
		loginButton.setForeground(Color.WHITE);
		loginButton.setBackground(new Color(71, 112, 186));
		loginButton.setBounds(395, 303, 283, 36);
		contentPane.add(loginButton);


		
		JLabel lblUsername = new JLabel("USERNAME");
		lblUsername.setBounds(395, 58, 114, 14);
		contentPane.add(lblUsername);

//		BOX USERNAME:
        loginField = new JTextField();
        loginField.setBounds(395, 83, 283, 36);
        contentPane.add(loginField);
        loginField.setColumns(10);

		JLabel lblPassword = new JLabel("PASSWORD");
		lblPassword.setBounds(395, 150, 96, 14);
		contentPane.add(lblPassword);

//      BOX PASSWORD:
		passwordField = new JPasswordField();
		passwordField.setBounds(395, 175, 283, 36);
		contentPane.add(passwordField);
		
		JLabel lbl_close = new JLabel("X");
		lbl_close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				logoff();
			}
		});

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

		lbl_close.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_close.setForeground(new Color(49, 67, 212));
		lbl_close.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbl_close.setBounds(691, 0, 37, 27);
		contentPane.add(lbl_close);
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
		LoginWindow loginWindow=new LoginWindow();
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

				JMenu actionMenu = new JMenu("Action" );
				JMenuItem hideMenu = new JMenuItem("Hide" );
//                    profile_setting. addActionListener(this);
				actionMenu. add(hideMenu);
				JMenuItem quitMenu = new JMenuItem( "Quit" );
//                    layout_setting. addActionListener(this);
				actionMenu. add(quitMenu);


				JMenuBar mainBar = new JMenuBar();
				mainBar. add(messengerMenu);
				mainBar. add(statusMenu);
				mainBar. add(actionMenu);

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
}
