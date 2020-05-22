import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessagePane extends JPanel implements MessageListener {

    private final ChatClient client;
    private final String login;

    private DefaultListModel<String> listModel=new DefaultListModel<>();
    private JList<String> messageList= new JList<>(listModel);
    private JTextField  inputField=new JTextField();

    public MessagePane(ChatClient client, String login) {
        this.client=client;
        this.login=login;
        this.client.addMessageListener(this);
        this.inputField.setPreferredSize(new Dimension(500,50));
        setLayout(new BorderLayout(2,2));
        add(new JScrollPane(messageList), BorderLayout.CENTER);
        add(inputField,BorderLayout.SOUTH);

        this.inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev){
                String text=inputField.getText();
                client.msg(login,text);
                listModel.addElement("You: "+text);
                inputField.setText("");
            }
        });
    }


    @Override
    public void onMessage(String fromLogin, String msgBody) {
        if(login.equalsIgnoreCase(fromLogin)){
            String upperCaseFromLogin=String.valueOf(fromLogin.charAt(0)).toUpperCase()+fromLogin.substring(1,fromLogin.length()) ;
            String line=upperCaseFromLogin + ": "+msgBody;
            listModel.addElement(line);
        }
    }
}
