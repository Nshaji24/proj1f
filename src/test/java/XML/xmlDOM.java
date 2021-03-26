package XML;
//import config.ConnectionUtil;

import config.HikariCPDS;
import org.junit.jupiter.api.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class xmlDOM {

    public void xmlcreate() {

        try {
            File inputFile = new File("User.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("user");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
                NodeList listOfPersons = doc.getElementsByTagName("user");
                for(int s=0; s<listOfPersons.getLength(); s++) {
                    Node firstPersonNode = listOfPersons.item(s);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        System.out.println("User id no : " + eElement.getAttribute("id"));
                        String id = eElement.getAttribute("id");
                        System.out.println("username : " + eElement.getElementsByTagName("username").item(0).getTextContent());
                        NodeList usernameList = eElement.getElementsByTagName("username");
                        Element nameElement = (Element) usernameList.item(0);

                        NodeList textFNList = nameElement.getChildNodes();
                        String username = ((Node) textFNList.item(0)).getNodeValue().trim();


                        System.out.println("password : "
                                + eElement
                                .getElementsByTagName("password")
                                .item(0)
                                .getTextContent());
                        NodeList passwordList = eElement.getElementsByTagName("password");
                        Element passwordElement =(Element)passwordList.item(0);
                        NodeList textLNList = passwordElement.getChildNodes();
                        String password= ((Node)textLNList.item(0)).getNodeValue().trim();



                        XPath xpath = XPathFactory.newInstance().newXPath();
                        Object res = xpath.evaluate("/table/user",
                                doc,
                                XPathConstants.NODESET);

                        Connection con = HikariCPDS.getConnection();
                        Statement st = con.createStatement();
                        int i = st.executeUpdate
                                ("insert into ormuser(id,username,password) values('" + id + "','" + username + "','" + password + "')");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void xmlupdate(){
        try {
            File inputFile = new File("User.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("user");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
                NodeList listOfPersons = doc.getElementsByTagName("user");
                for(int s=0; s<listOfPersons.getLength(); s++) {
                    Node firstPersonNode = listOfPersons.item(s);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        System.out.println("User id no : " + eElement.getAttribute("id"));
                        String id = eElement.getAttribute("id");
                        System.out.println("username : " + eElement.getElementsByTagName("username").item(0).getTextContent());
                        NodeList usernameList = eElement.getElementsByTagName("username");
                        Element nameElement = (Element) usernameList.item(0);

                        NodeList textFNList = nameElement.getChildNodes();
                        String username = ((Node) textFNList.item(0)).getNodeValue().trim();


                        System.out.println("password : "
                                + eElement
                                .getElementsByTagName("password")
                                .item(0)
                                .getTextContent());
                        NodeList passwordList = eElement.getElementsByTagName("password");
                        Element passwordElement =(Element)passwordList.item(0);
                        NodeList textLNList = passwordElement.getChildNodes();
                        String password= ((Node)textLNList.item(0)).getNodeValue().trim();



                        XPath xpath = XPathFactory.newInstance().newXPath();
                        Object res = xpath.evaluate("/table/user",
                                doc,
                                XPathConstants.NODESET);

                        Connection con = HikariCPDS.getConnection();
                        Statement st = con.createStatement();
                        int i = st.executeUpdate
                                ("UPDATE model.ormuser "
                                        + "SET id = ?, username=?, password=? "
                                        + "values('" + id + "','" + username + "','" + password + "')");
                        /*String SQLupdate = "UPDATE model.ormuser "
                                + "SET id = ?,username=?,password=? "
                                + "values('" + id + "','" + username + "','" + password + "')";*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static private String getAttrValue(Node node,String attrName) {
        if ( ! node.hasAttributes() ) return "";
        NamedNodeMap nmap = node.getAttributes();
        if ( nmap == null ) return "";
        Node n = nmap.getNamedItem(attrName);
        if ( n == null ) return "";
        return n.getNodeValue();
    }
    static private String getTextContent(Node parentNode,String childName) throws SQLException {
        NodeList nlist = parentNode.getChildNodes();
        for (int i = 0 ; i < nlist.getLength() ; i++) {
            Node n = nlist.item(i);
            String name = n.getNodeName();
            if ( name != null && name.equals(childName) )
                return n.getTextContent();
        }
        return "";
    }
}
