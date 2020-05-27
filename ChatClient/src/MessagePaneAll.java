import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessagePaneAll extends JPanel implements MessageListener {

    private final ChatClient client;
    private final String login;

    private DefaultListModel<String> listModel=new DefaultListModel<>();
    private JList<String> messageList= new JList<>(listModel);
    private JTextField  inputField=new JTextField();

    public MessagePaneAll(ChatClient client, String login) {
        this.client=client;
        this.login=login;
        this.client.addMessageListener(this);
        this.inputField.setPreferredSize(new Dimension(450,50));
        JPanel bottomPanel= new JPanel(new FlowLayout(5,5,5));
        JButton sendBtn= new JButton("Send");
        sendBtn.setPreferredSize(new Dimension(80,50));
        bottomPanel.add(inputField);
        bottomPanel.add(sendBtn);
        setLayout(new BorderLayout(2,2));
        add(new JScrollPane(messageList), BorderLayout.CENTER);
        //add(inputField,BorderLayout.SOUTH);
        add(bottomPanel,BorderLayout.SOUTH);
        this.inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev){
                String text=inputField.getText();
                if(!text.equals("")){
                    client.msgAll(text);
                    listModel.addElement("You: "+text);
                    inputField.setText("");
                }
            }
        });
    }


    @Override
    public void onMessage(String fromLogin, String msgBody) {
            String upperCaseFromLogin=String.valueOf(fromLogin.charAt(0)).toUpperCase()+fromLogin.substring(1,fromLogin.length()) ;
            String line=upperCaseFromLogin + ": "+msgBody;
            listModel.addElement(line);
    }
}
