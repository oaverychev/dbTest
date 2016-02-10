import javax.swing.*;
import java.sql.*;

public class DatabaseConnection {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/crash_db";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    private ResultSet rs;
    private Statement stmt;

    public DatabaseConnection() {
        try{

            Class.forName("com.mysql.jdbc.Driver");
            stmt = DriverManager.getConnection(DB_URL,USER,PASS).createStatement();


        }catch(SQLException se){se.printStackTrace();
        }catch(Exception e){e.printStackTrace();}

    }

    public String getField(String field) {
        String result = "";
        try{
            rs = stmt.executeQuery("SELECT " + field + " FROM crash_user LIMIT 1");
            if(rs.next()) {
                if (field.equals("email")) {
                    result = rs.getString(field);
                } else if (field.equals("id")) {
                    result = Integer.toString(rs.getInt(field));
                }
                rs.close();
            }

        }catch(SQLException se){se.printStackTrace();
        }catch(Exception e){e.printStackTrace();}


        return result;
    }
}
