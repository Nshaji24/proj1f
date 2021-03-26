package custorm;

import annotations.Column;
import annotations.PrimaryKey;
import config.HikariCPDS;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class customORM<T> {

    private Connection connection;
    //so we can get and increment
    private AtomicInteger id = new AtomicInteger(0);

    public static <T> customORM getConnection() throws SQLException {
        return new customORM();
    }

    public customORM() throws SQLException {
        // this.connection = DriverManager.getConnection("jdbc:h2:~/test","","");
        this.connection = HikariCPDS.getConnection();
    }


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


    public T read(Class<T> class1, int i) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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
      //  }
        return null;
    }
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




            delete(class1,i);
            create(t);





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




