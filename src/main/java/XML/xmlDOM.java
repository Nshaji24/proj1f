package XML;
//import config.ConnectionUtil;

import config.HikariCPDS;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.sql.*;

//SOURCES : https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
//https://www.novixys.com/blog/load-xml-mysql-using-java/

public class xmlDOM {

    public void xmlcreate() {

        try {
            File file = new File("User.xml");
            //DocumentBuildFactory allows us to get parse and produce DOM object trees from XML
            DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
            //obtain a Document from XML
            DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
           // Parse the content of the given file as an XML document and return a new DOM Document object.
            Document doc = docBuilder.parse(file);
            doc.getDocumentElement().normalize();
            //direct access to the child node that is the document element of the document
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            //NodeList interface provides the abstraction of an ordered collection of nodes
            NodeList docNodeList = doc.getElementsByTagName("user");
            System.out.println("----------------------------");

            for (int x = 0; x < docNodeList.getLength(); x++) {
                Node docNode = docNodeList.item(x);
               // The name of this node
                System.out.println("\nCurrent Element :" + docNode.getNodeName());
                System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
                NodeList elementList = doc.getElementsByTagName("user");
                for(int j=0; j<elementList.getLength(); j++) {
                    Node firstPersonNode = elementList.item(j);
                    if (docNode.getNodeType() == Node.ELEMENT_NODE) {
                        //Element represents an element in an HTML or XML document.
                        Element docElement = (Element) docNode;
                        System.out.println("User id is : " + docElement.getAttribute("id"));
                        String id = docElement.getAttribute("id");
                        System.out.println("username is : " + docElement.getElementsByTagName("username").item(0).getTextContent());
                        NodeList usernameList = docElement.getElementsByTagName("username");
                        Element nameElement = (Element) usernameList.item(0);

                        NodeList textUsernameList = nameElement.getChildNodes();
                        String username = ((Node) textUsernameList.item(0)).getNodeValue().trim();

                        //get elements by password tag
                        System.out.println("password : " + docElement.getElementsByTagName("password").item(0).getTextContent());
                        NodeList passwordList = docElement.getElementsByTagName("password");
                        Element passwordElement =(Element)passwordList.item(0);
                        NodeList textPasswordList = passwordElement.getChildNodes();
                        String password= ((Node)textPasswordList.item(0)).getNodeValue().trim();


        //XPath provides syntax to define part of an XML document. XPath Expression is a query language to
        // select part of the XML document based on the query String. Using XPath Expressions,
        // we can find nodes in any xml document satisfying the query string.

                        XPath xpath = XPathFactory.newInstance().newXPath();
                        //Evaluate an XPath expression in the specified context and return the result as the specified type.
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
                        //int i = st.executeUpdate

                        String sql1= ("UPDATE ormuser "
                                        + "SET id = ?, username=?, password=?"
                                        + "where id =?");
                        PreparedStatement pstmt = con.prepareStatement(sql1);
                        pstmt.setInt(1, Integer.parseInt(id));
                        pstmt.setString(2, username);
                        pstmt.setString(3, password);
                        pstmt.setInt(4, Integer.parseInt(id));
                        pstmt.executeUpdate();


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


    public void xmldelete(String id) {

        try {
            File file = new File("User.xml");
            //DocumentBuildFactory allows us to get parse and produce DOM object trees from XML
            DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
            //obtain a Document from XML
            DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
            // Parse the content of the given file as an XML document and return a new DOM Document object.
            Document doc = docBuilder.parse(file);
            doc.getDocumentElement().normalize();
            //direct access to the child node that is the document element of the document
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            //NodeList interface provides the abstraction of an ordered collection of nodes
            NodeList docNodeList = doc.getElementsByTagName("user");
            System.out.println("----------------------------");

            for (int x = 0; x < docNodeList.getLength(); x++) {
                Node docNode = docNodeList.item(x);
                // The name of this node
                System.out.println("\nCurrent Element :" + docNode.getNodeName());
                System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
                NodeList elementList = doc.getElementsByTagName("user");
                for(int j=0; j<elementList.getLength(); j++) {
                    Node firstPersonNode = elementList.item(j);
                    if (docNode.getNodeType() == Node.ELEMENT_NODE) {
                        //Element represents an element in an HTML or XML document.
                        Element docElement = (Element) docNode;
                        System.out.println("User id is : " + docElement.getAttribute("id"));
                         id = docElement.getAttribute("id");
                        System.out.println("username is : " + docElement.getElementsByTagName("username").item(0).getTextContent());
                        NodeList usernameList = docElement.getElementsByTagName("username");
                        Element nameElement = (Element) usernameList.item(0);

                        NodeList textUsernameList = nameElement.getChildNodes();
                        String username = ((Node) textUsernameList.item(0)).getNodeValue().trim();

                        //get elements by password tag
                        System.out.println("password : " + docElement.getElementsByTagName("password").item(0).getTextContent());
                        NodeList passwordList = docElement.getElementsByTagName("password");
                        Element passwordElement =(Element)passwordList.item(0);
                        NodeList textPasswordList = passwordElement.getChildNodes();
                        String password= ((Node)textPasswordList.item(0)).getNodeValue().trim();


                        //XPath provides syntax to define part of an XML document. XPath Expression is a query language to
                        // select part of the XML document based on the query String. Using XPath Expressions,
                        // we can find nodes in any xml document satisfying the query string.

                        XPath xpath = XPathFactory.newInstance().newXPath();
                        //Evaluate an XPath expression in the specified context and return the result as the specified type.
                        Object res = xpath.evaluate("/table/user",
                                doc,
                                XPathConstants.NODESET);

                        Connection con = HikariCPDS.getConnection();
                        Statement st = con.createStatement();
                        String sql =  "delete from ormuser where id = ?";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setInt(1, Integer.parseInt(id));
                        pstmt.executeUpdate();
                     /*   int i = st.executeUpdate
                                ("delete from ormuser(id,username,password) where id = ?";*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void xmlread(String id) {

        try {
            File file = new File("User.xml");
            //DocumentBuildFactory allows us to get parse and produce DOM object trees from XML
            DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
            //obtain a Document from XML
            DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
            // Parse the content of the given file as an XML document and return a new DOM Document object.
            Document doc = docBuilder.parse(file);
            doc.getDocumentElement().normalize();
            //direct access to the child node that is the document element of the document
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            //NodeList interface provides the abstraction of an ordered collection of nodes
            NodeList docNodeList = doc.getElementsByTagName("user");
            System.out.println("----------------------------");

            for (int x = 0; x < docNodeList.getLength(); x++) {
                Node docNode = docNodeList.item(x);
                // The name of this node
                System.out.println("\nCurrent Element :" + docNode.getNodeName());
                System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
                NodeList elementList = doc.getElementsByTagName("user");
                for(int j=0; j<elementList.getLength(); j++) {
                    Node firstPersonNode = elementList.item(j);
                    if (docNode.getNodeType() == Node.ELEMENT_NODE) {
                        //Element represents an element in an HTML or XML document.
                        Element docElement = (Element) docNode;
                        System.out.println("User id is : " + docElement.getAttribute("id"));
                        id = docElement.getAttribute("id");
                        System.out.println("username is : " + docElement.getElementsByTagName("username").item(0).getTextContent());
                        NodeList usernameList = docElement.getElementsByTagName("username");
                        Element nameElement = (Element) usernameList.item(0);

                        NodeList textUsernameList = nameElement.getChildNodes();
                        String username = ((Node) textUsernameList.item(0)).getNodeValue().trim();

                        //get elements by password tag
                        System.out.println("password : " + docElement.getElementsByTagName("password").item(0).getTextContent());
                        NodeList passwordList = docElement.getElementsByTagName("password");
                        Element passwordElement =(Element)passwordList.item(0);
                        NodeList textPasswordList = passwordElement.getChildNodes();
                        String password= ((Node)textPasswordList.item(0)).getNodeValue().trim();


                        //XPath provides syntax to define part of an XML document. XPath Expression is a query language to
                        // select part of the XML document based on the query String. Using XPath Expressions,
                        // we can find nodes in any xml document satisfying the query string.

                        XPath xpath = XPathFactory.newInstance().newXPath();
                        //Evaluate an XPath expression in the specified context and return the result as the specified type.
                        Object res = xpath.evaluate("/table/user",
                                doc,
                                XPathConstants.NODESET);

                        Connection con = HikariCPDS.getConnection();
                        Statement st = con.createStatement();
                        String sql =  "select from ormuser where id = ?";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setInt(1, Integer.parseInt(id));
                        ResultSet rs = pstmt.executeQuery();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        while (rs.next()) {
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(",  ");
                                String columnValue = rs.getString(i);
                                System.out.print(columnValue + " " + rsmd.getColumnName(i));
                            }
                            System.out.println(" ");
                        }
                     /*   int i = st.executeUpdate
                                ("delete from ormuser(id,username,password) where id = ?";*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
