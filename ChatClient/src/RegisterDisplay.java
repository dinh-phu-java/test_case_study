import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class RegisterDisplay extends JFrame {

	private JPanel contentPane;
	private JTextField fullNameText; // userText
	private JTextField userText; //fullNameText
	private JPasswordField passwordText;
	private JPasswordField confirmPasswordText;
	private final ChatClient client;

	int xx,xy;

	public static void main(String[] args) {

				try {
					RegisterDisplay frame = new RegisterDisplay();
					frame.setUndecorated(true);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

	}
	

	public RegisterDisplay() {
		super("Register Users");

		this.client = new ChatClient();
		this.client.connect();
		setBackground(new Color(252, 252, 252));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 729, 476);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(249, 249, 249));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(198, 87, 87));
		panel.setBounds(0, 0, 346, 490);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Codegym Messenger");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblNewLabel.setForeground(new Color(250, 247, 247));
		lblNewLabel.setBounds(35, 305, 300, 27);
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
	            RegisterDisplay.this.setLocation(x - xx, y - xy);
			}
		});
		label.setBounds(-20, 0, 400, 275);
		label.setVerticalAlignment(SwingConstants.TOP);
		ImageIcon img= new ImageIcon("icons/bg.jpg");
		label.setIcon(img);
//		label.setIcon(new ImageIcon(RegisterDisplay.class.getResource("/icons/bg.jpg")));
		panel.add(label);

		JLabel lblWeGotYou = new JLabel("Hãy code theo cách của bạn");
		lblWeGotYou.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeGotYou.setForeground(new Color(219, 238, 233));
		lblWeGotYou.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblWeGotYou.setBounds(35, 343, 300, 27);
		panel.add(lblWeGotYou);

		JLabel lblWeGotYou1 = new JLabel("Và trả tiền theo cách của chúng tôi");
		lblWeGotYou1.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeGotYou1.setForeground(new Color(229, 239, 237));
		lblWeGotYou1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblWeGotYou1.setBounds(35, 363, 300, 27);
		panel.add(lblWeGotYou1);


		// Signup
		Button registerBTN = new Button("SignUp");
		registerBTN.setForeground(Color.WHITE);
		registerBTN.setBackground(new Color(17, 1, 146));
		registerBTN.setFont(new Font("Tahoma", Font.PLAIN, 18));
		registerBTN.setBounds(395, 363, 283, 36);
		contentPane.add(registerBTN);

		userText = new JTextField();
		userText.setColumns(10);
		userText.setText("userText");
		userText.setBounds(395, 157, 283, 36);
		contentPane.add(userText);



		// FullName
		JLabel fullName = new JLabel("FULLNAME");
		fullName.setBounds(395, 58, 114, 14);
		fullName.setForeground(new Color(17, 1, 146));
		contentPane.add(fullName);

		// userName
		JLabel userName = new JLabel("USERNAME");
		userName.setBounds(395, 132, 114, 14);
		userName.setForeground(new Color(17, 1, 146));
		contentPane.add(userName);

		fullNameText = new JTextField();
		fullNameText.setBounds(395, 83, 283, 36);
		fullNameText.setText("full Name Text");
		contentPane.add(fullNameText);
		fullNameText.setColumns(10);

		//passWord
		JLabel password = new JLabel("PASSWORD");
		password.setBounds(395, 204, 96, 14);
		password.setForeground(new Color(17, 1, 146));
		contentPane.add(password);
		
		JLabel repeatPassword = new JLabel("REPEAT PASSWORD");
		repeatPassword.setBounds(395, 275, 133, 14);
		repeatPassword.setForeground(new Color(17, 1, 146));
		contentPane.add(repeatPassword);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(395, 229, 283, 36);
		contentPane.add(passwordText);
		
		confirmPasswordText = new JPasswordField();
		confirmPasswordText.setBounds(395, 293, 283, 36);
		contentPane.add(confirmPasswordText);
		
		JLabel lbl_close = new JLabel("X");
		lbl_close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				callLoginWindow();
			}
		});

		registerBTN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ev){
				doRegisterUser();
			}
		});

		lbl_close.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_close.setForeground(new Color(241, 57, 83));
		lbl_close.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbl_close.setBounds(691, 0, 37, 27);
		contentPane.add(lbl_close);
	}

	public void doRegisterUser() {
		String username=this.userText.getText();
		String fullname=this.fullNameText.getText();
		String password=this.passwordText.getText();
		System.out.println("access do register");
		try{
			if(!isCheckUserNameSequence()){
				JOptionPane.showMessageDialog(null,"UserName is not correct!","Error",JOptionPane.ERROR_MESSAGE);
				clearUserText();
			}else if(checkPasswordField()){
				JOptionPane.showMessageDialog(null,"Password should be the same!","Error",JOptionPane.ERROR_MESSAGE);
				clearPassText();
			} else if(isFieldEmpty()){
				JOptionPane.showMessageDialog(null,"All Field should not be empty!","Error",JOptionPane.ERROR_MESSAGE);
			}
			else if(this.client.registerUser(fullname,username,password)){
				JOptionPane.showMessageDialog(null,"Register Completed","Completed",JOptionPane.INFORMATION_MESSAGE);
				callLoginWindow();
			}
		}catch (Exception e){
			e.printStackTrace();
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
		this.fullNameText.setText("");
	}
	public void clearPassText(){
		this.passwordText.setText("");
		this.confirmPasswordText.setText("");
	}
	public void callLoginWindow(){
		this.setVisible(false);
		this.client.logoff();
		LoginWindowVer2 loginWindow=new LoginWindowVer2();
		loginWindow.setVisible(true);
	}
}
