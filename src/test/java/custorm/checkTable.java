package custorm;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkTable {

    @Test
    public boolean checkTableExists(Connection conn, String tableName) throws SQLException {

        boolean tableExists = false;
        DatabaseMetaData dbm = conn.getMetaData();

        ResultSet tables = dbm.getTables(null, null, tableName, null);
        if (tables.next()) {
            System.out.println("table exists");
        } else {
            System.out.println("table does not exist");
        }
        return false;
    }



    }


  /*boolean tableExists = true;
        DatabaseMetaData dbm = conn.getMetaData();

        ResultSet tables = dbm.getTables(null, null, "ormuser", null);
        if (tables.next()) {
            System.out.println("table exists");
        } else {
            System.out.println("table does not exists");
        }
        return false;
    }*/













/*try (ResultSet rs = conn.getMetaData().getTables(null, null, "ormuser", null)) {
            while (rs.next()) {
                String tName = rs.getString("id");
                if (tName != null && tName.equals(tableName)) {
                    tExists = true;
                    System.out.println("Table exists");
                    break;
                }else{
                    System.out.println("table no exist");
                }
            }
        }
        return tExists;
    }*/