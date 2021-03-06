import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class UserListPane extends JPanel implements UserStatusListener {
    private final ChatClient client;
    private JList<String> userListUI;
    private DefaultListModel<String> userListModel;
    public UserListPane(ChatClient client) {
        this.client=client;
        this.client.addUserStatusListener(this);
        userListModel=new DefaultListModel<>();
        userListUI=new JList<>(userListModel);

        setLayout(new BorderLayout());
        add(new JScrollPane(userListUI),BorderLayout.CENTER);

        JLabel group= new JLabel("Group");
        group.setPreferredSize(new Dimension(400,50));
        group.setBackground(new Color(206, 255, 195));
        setLayout(new BorderLayout());
        add(new JScrollPane(userListUI),BorderLayout.CENTER);
        add(group,BorderLayout.SOUTH);

        group.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ev){
                if(ev.getClickCount()>1){
                    String login= userListUI.getSelectedValue();
                    MessagePaneAll messagePaneAll=new MessagePaneAll(client,login);
                    JFrame msgFrame= new JFrame("Message: "+login);
                    Image img=Toolkit.getDefaultToolkit().getImage("icons/codegym.png");
                    msgFrame.setIconImage(img);
                    msgFrame.setSize(550,500);
                    msgFrame.setResizable(false);
                    msgFrame.getContentPane().add(messagePaneAll,BorderLayout.CENTER);
                    msgFrame.setVisible(true);
                }
            }
        });

        userListUI.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ev){
                if(ev.getClickCount()>1){
                    String login= userListUI.getSelectedValue();
                    MessagePane messagePane=new MessagePane(client,login);
                    JFrame msgFrame= new JFrame("Message: "+login);
                    Image img=Toolkit.getDefaultToolkit().getImage("icons/codegym.png");
                    msgFrame.setIconImage(img);
                    msgFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    msgFrame.setSize(550,500);
                    msgFrame.setResizable(false);
                    msgFrame.getContentPane().add(messagePane,BorderLayout.CENTER);
                    msgFrame.setVisible(true);
                }
            }
        });
    }

    

    @Override
    public void online(String login) {
        userListUI.setFixedCellHeight(50);
        userListModel.addElement(login);
    }

    @Override
    public void offline(String login) {
        userListModel.removeElement(login);
    }

}
