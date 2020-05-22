import javax.swing.*;
import java.awt.*;

public class MessagePane extends JPanel {

    private final ChatClient client;
    private final String login;

    private DefaultListModel<String> listModel=new DefaultListModel<>();
    private JList<String> messageList= new JList<>(listModel);
    private JTextField  inputField=new JTextField();

    public MessagePane(ChatClient client, String login) {
        this.client=client;
        this.login=login;
        this.inputField.setPreferredSize(new Dimension(500,50));
        setLayout(new BorderLayout(2,2));
        add(new JScrollPane(messageList), BorderLayout.CENTER);
        add(inputField,BorderLayout.SOUTH);
    }


}
