import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.print.DocFlavor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class FileManagement {
    private Path filePath;
    private int id;
    private static int nextID=1;
    public FileManagement(){
        filePath= Paths.get(System.getProperty("user.dir"),"files","users.xml");

    }

    public boolean isFileExist(){
        if(Files.exists(filePath)){
            return true;
        }else{
            return false;
        }

    }

    public void createFile(String name,String username,String pass){
        try {

            File myFile= new File(String.valueOf(filePath));
            myFile.setWritable(true);
            myFile.setReadable(true);
            myFile.createNewFile();

            DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=dbFactory.newDocumentBuilder();
            Document document=builder.newDocument();

            Element rootElement=document.createElement("information");
            document.appendChild(rootElement);

            appendUserInfo(document,rootElement,name,username,pass);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }  catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendDocument(String name,String user,String pass){

        try {
            DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
            DocumentBuilder builder= dbFactory.newDocumentBuilder();
            Document document = builder.parse(String.valueOf(filePath));
            Element rootElement= document.getDocumentElement();
            System.out.println(document.getNodeName());
            System.out.println(rootElement.getTagName());

            appendUserInfo(document,rootElement,name,user,pass);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void appendUserInfo(Document document,Element rootElement,String name,String username,String pass) throws TransformerException {

        Element user= document.createElement("user");

        Element fullName=document.createElement("fullname");
        fullName.setTextContent(name);

        Element userName= document.createElement("username");
        userName.setTextContent(username);

        Element passwordElement= document.createElement("password");
        passwordElement.setTextContent(pass);

        user.appendChild(fullName);
        user.appendChild(userName);
        user.appendChild(passwordElement);
        rootElement.appendChild(user);

        TransformerFactory tfFactory=TransformerFactory.newInstance();
        Transformer transformer =tfFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source=new DOMSource(document);
        StreamResult result=new StreamResult(new File(String.valueOf(filePath)));
        transformer.transform(source,result);


    }

    public static void main(String[] args) {
        FileManagement fileManagement=new FileManagement();
        if (fileManagement.isFileExist()){
            System.out.println("File ton tai");
            fileManagement.appendDocument("Hong Lam1","lam1","123456");
        }else{
            System.out.println("File khong ton tai");
            fileManagement.createFile("Dinh Phu","phu","123456");
        }
    }
}
