package custorm;

import annotations.Column;
import annotations.PrimaryKey;
import config.HikariCPDS;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class ORMread<T> {
    checkTable c = new checkTable();

    private Connection connection;
    //so we can get and increment
    private AtomicInteger id = new AtomicInteger(0);


    public static <T> ORMread getConnection() throws SQLException {
        return new ORMread();
    }

    public ORMread() throws SQLException {
        // this.connection = DriverManager.getConnection("jdbc:h2:~/test","","");
        this.connection = HikariCPDS.getConnection();
    }

    public T read(Class<T> class1, int i) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if(c.checkTableExists(connection, class1.getSimpleName())) {
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
                String sql = "select * from " + class1.getSimpleName() + " where " + primaryKey.getName() + " = " + i;

                PreparedStatement st = connection.prepareStatement(sql);
                ResultSet r1 = st.executeQuery();
                r1.next();

                T t = class1.getConstructor().newInstance();

                int userID = r1.getInt(primaryKey.getName());
                primaryKey.setAccessible(true);
                primaryKey.set(t, userID);

                for (Field field : decFields) {
                    if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);
                        if (field.getType() == int.class) {
                            field.set(t, r1.getInt(field.getName()));
                        } else if (field.getType() == String.class) {
                            field.set(t, r1.getString(field.getName()));
                        }
                    }
                }
                connection.commit();
                return t;
            } catch (Exception e) {
                connection.rollback();
            }
        }
        return null;
    }

}