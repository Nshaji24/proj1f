import XML.xmlDOM;

import config.HikariCPDS;
import custorm.*;
import model.ormcar;
import model.ormuser;

import org.h2.util.Task;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Driver {


    public Driver() throws SQLException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
    }

    public static void main(String[] args) throws SQLException,NumberFormatException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        ORMcreate c = new ORMcreate();
        ORMupdate u = new ORMupdate();
        ORMread r= new ORMread();
        ORMdelete d = new ORMdelete();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());

        //private static CacheHelper cache;
        ormuser user1 = new ormuser(0,"Noel","Password");
       ormuser user2 = new ormuser(1,"Shaji","Password");
        ormuser user3 = new ormuser(2,"Toast","Password");
        ormuser user4 = new ormuser(3,"Oates","Password");

        ormuser user5 = new ormuser(4,"werws","Password");
        ormuser user6 = new ormuser(7,"updated","upPassword");
        ormcar car1 = new ormcar(1,"topyota","corolla");

        // xmlDOM x = new xmlDOM();

        // customORM<ormuser> orm = customORM.getConnection();

        //c.create(car1);
//d.delete(ormcar.class,1);
        // ormuser userRead = orm.read(ormuser.class,45);
        //  u.update(ormuser.class,1,user6);
        //c.create(user6);

        //x.xmlcreate();
        //   orm.create(user2);

        //ormcar userRead = (ormcar) r.read(ormcar.class,1);

//ormuser2 userRead = (ormuser2) r.read(ormuser2.class,1);
        Runnable task1 = () -> {
            try {
                c.create(user3);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        Runnable task2 = () -> {
            try {
                c.create(user1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        executorService.execute(task1);
        executorService.execute(task2);
        executorService.shutdown();
        //  orm.create(user1);
    /*  String input =  user1.toString();
      String id = String.valueOf(user1.getId());
        cache.getOrmUserCache().put(input,id);
        System.out.println(cache.getOrmUserCache());*/
/*
checkTable ct= new checkTable();
Connection conn = HikariCPDS.getConnection();
ct.checkTableExists(conn,"ormuser");
*/

        //  User user3r = orm.delete(User.class,1);
         //System.out.println(userRead);
    }
}
