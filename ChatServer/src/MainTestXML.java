import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainTestXML {
    public static void main(String[] args) {

        try {
            String username="dinhphu";
            String password="abc1";
            Path myPath= Paths.get(System.getProperty("user.dir"),"files","users.xml");
            DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=dbFactory.newDocumentBuilder();
            Document doc=builder.parse(String.valueOf(myPath));

            String express="information/user";
            XPath xPath= XPathFactory.newInstance().newXPath();

            NodeList nList= (NodeList)xPath.compile(express).evaluate(doc, XPathConstants.NODESET);
            for(int i=0;i< nList.getLength();i++){
                Element element=(Element)nList.item(i);
                String elementUserName= element.getElementsByTagName("username").item(0).getTextContent();
                String passwordUserName=element.getElementsByTagName("password").item(0).getTextContent();
                if(elementUserName.trim().equals(username) && passwordUserName.trim().equals(password)){
                    System.out.println("User: "+elementUserName);
                    System.out.println("Password : "+passwordUserName);
                }else{
                    System.out.println("can't find username");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
