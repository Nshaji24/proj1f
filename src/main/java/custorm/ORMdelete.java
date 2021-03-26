package custorm;

import annotations.Column;
import annotations.PrimaryKey;
import config.HikariCPDS;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
@Deprecated
public class ORMdelete<T> {
    private Connection connection;
    checkTable c = new checkTable();

    //so we can get and increment
    private AtomicInteger id = new AtomicInteger(0);


    public static <T> ORMdelete getConnection() throws SQLException {
        return new ORMdelete();
    }

    public ORMdelete() throws SQLException {
        // this.connection = DriverManager.getConnection("jdbc:h2:~/test","","");
        this.connection = HikariCPDS.getConnection();
    }

    public T delete(Class<T> class1, int i) throws SQLException, NumberFormatException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
       // if(c.checkTableExists(connection, class1.getSimpleName())) {
            try {
                connection.setAutoCommit(false);
                Field[] decFields = class1.getDeclaredFields();
                Field primaryKey = null;
                for (Field field : decFields) {
                    if (field.isAnnotationPresent(PrimaryKey.class)) {
                        primaryKey = field;
                        break;
                    }
                }


                String sql = "delete from " + class1.getSimpleName() + " where " + primaryKey.getName() + " = " + i;

                PreparedStatement st = connection.prepareStatement(sql);
                st.executeUpdate();


                T t = class1.getConstructor().newInstance();
                primaryKey.setAccessible(true);
                int userID = (primaryKey.getInt(t));

                primaryKey.set(t, userID);

                for (Field field : decFields) {
                    if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);
                        if (field.getType() == int.class) {
                            field.set(t, field.getName());
                        } else if (field.getType() == String.class) {
                            field.set(t, field.getName());
                        }
                    }
                }
                connection.commit();
                return t;
            } catch (Exception e) {
                connection.rollback();
            }
      //  }
        return null;
    }

}

