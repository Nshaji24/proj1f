package custorm;

import annotations.Column;
import annotations.PrimaryKey;
import config.HikariCPDS;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ORMupdate<T> {
    checkTable ct = new checkTable();

    ORMdelete o =new ORMdelete();
    ORMcreate c= new ORMcreate();
    private Connection connection;
    //so we can get and increment
    private AtomicInteger id = new AtomicInteger(0);


    public static <T> ORMupdate getConnection() throws SQLException {
        return new ORMupdate();
    }

    public ORMupdate() throws SQLException {
        // this.connection = DriverManager.getConnection("jdbc:h2:~/test","","");
        this.connection = HikariCPDS.getConnection();
    }
    @Test
    public void update(Class<T> class1, int i,T t) throws IllegalAccessException, SQLException {
/*
        UPDATE table_name
        SET column1 = value1,
                column2 = value2,
    ...
        WHERE condition;*/

        try {
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

            connection.setAutoCommit(false);

            for (Field field : decFields) {
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    primaryKey = field;
                    break;
                }
            }

            int number = columns.size() + 1;
            String questionMarks = IntStream.range(0, number).mapToObj(e -> "?").collect(Collectors.joining(","));




            o.delete(class1,i);
            c.create(t);





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

        } catch (Exception e) {
            connection.rollback();
        }


    }
}

