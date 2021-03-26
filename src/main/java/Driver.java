import XML.xmlDOM;

import config.HikariCPDS;
import config.ormCache;
import custorm.*;
import model.ormcar;
import model.ormuser;
import custorm.customORM;
import org.h2.util.Task;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.*;
import config.ormCache;
public class Driver {


    public Driver() throws SQLException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
    }

    public static void main(String[] args) throws SQLException,NumberFormatException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {

        customORM corm = new customORM();
        xmlDOM x = new xmlDOM();
        ormCache <Integer,ormuser> orc = new ormCache<Integer,ormuser>(1,1,10);





        //TODO: EXECUTOR SERVICE TO TRY MULTITHREADING

        ExecutorService executor = Executors.newFixedThreadPool(10);
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());

        //CREATE NEW USERS
        ormuser user1 = new ormuser(1,"Noel","Password");
        ormuser user2 = new ormuser(2,"Shaji","Password");
        ormuser user3 = new ormuser(3,"Toast","Password");
        ormuser user4 = new ormuser(4,"Oates","Password");

        //CREATE NEW CAR
        ormcar car1 = new ormcar(1,"topyota","corolla");




        //CREATE METHOD OF ORM, PASS IN AN OBJECT
       /*    Runnable task1 = () -> {
            try {
                corm.create(user3);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        Runnable task2 = () -> {
            try {
                corm.create(user1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        executorService.execute(task1);
        executorService.execute(task2);
        executorService.shutdown();*/


        //READ METHOD OF ORM, READ AND STORE THE VALUE IN A CACHE
     /*   ormuser userRead = (ormuser) corm.read(ormuser.class,1);
        orc.put(1,userRead);
        System.out.println(orc.get(1));*/


        //DELETE METHOD OF ORM, PASS IN THE CLASS AND THE ID OF OBJECT TO BE DELETED
       // corm.delete(ormuser.class,1);



        //UPDATE METHOD OF ORM, PASS IN THE CLASS, THE ID , AND THE NEW OBJECT
       // corm.update(ormuser.class,1,user1);




        //XML CREATE, TAKES XML FILE, READS DATA AND INSERTS INTO DATABASE
        //x.xmlcreate();

        //XML DELETE BY ID
        // x.xmldelete("45");

        //reads data in xml file
        //x.xmlread("45");

        //XML UPDATE BY ID
       // x.xmlupdate();








    }
}
