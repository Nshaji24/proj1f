package custorm;


import annotations.Column;
import annotations.PrimaryKey;
import config.HikariCPDS;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ORMcreate<T> {
    checkTable c = new checkTable();
    private Connection connection;
    //so we can get and increment
    private AtomicInteger id = new AtomicInteger(0);


    public static <T> ORMcreate getConnection() throws SQLException {
        return new ORMcreate();
    }

    public ORMcreate() throws SQLException {
        // this.connection = DriverManager.getConnection("jdbc:h2:~/test","","");
        this.connection = HikariCPDS.getConnection();
    }
    @Test
    public void create(T t) throws IllegalAccessException, SQLException {
        Class<? extends Object> cl = t.getClass();
        //using reflection to get declared fields
        Field[] decFields = cl.getDeclaredFields();

        Field primaryKey = null;
        //make arraylist of columns
        ArrayList<Field> columns = new ArrayList<>();
        //provides delimiter for columns
        StringJoiner join = new StringJoiner(",");

        //if annotation is pk then pk= that field, if its = to column then we add column to array
        for (Field field : decFields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                primaryKey = field;
            } else if (field.isAnnotationPresent(Column.class)) {
                join.add(field.getName());
                columns.add(field);
            }
        }

        int number = columns.size() + 1;

        String questionMarks = IntStream.range(0, number).mapToObj(e -> "?").collect(Collectors.joining(","));
       // if(c.checkTableExists(HikariCPDS.getConnection(), cl.getSimpleName())) {
            try {
                connection.setAutoCommit(false);
                String sql = "insert into " + cl.getSimpleName() + "(" + primaryKey.getName() + "," + join.toString() + ")" + "values(" + questionMarks + ")";

                PreparedStatement st = connection.prepareStatement(sql);

                if (primaryKey.getType() == int.class) {
                    st.setInt(1, id.addAndGet(1));
                }

                int index = 2;
                for (Field field : columns) {
                    field.setAccessible(true);
                    if (field.getType() == String.class) {
                        st.setString(index++, (String) (field.get(t)));
                    }
                }
                st.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
     //  }

    }
}
