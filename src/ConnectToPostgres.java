import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection to Postgres database
 */
class ConnectToPostgres  {

    /*
     * DB configuration
     */
    final static String url = "jdbc:postgresql://127.0.0.1:5432/postgres";
    final static String user = "postgres";
    final static String password = "admin";
    static Connection connection = null;


    public static void Connect(boolean notes) {

        //Testing connection to database
        if(notes){System.out.println("-------- PostgreSQL "
        + "JDBC Connection Testing ------------");}
        try {
        Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {

            if(notes){System.out.println("Where is your PostgreSQL JDBC Driver? "
        + "Include in your library path!");}
        e.printStackTrace();
        return;
        }
        if(notes){System.out.println("PostgreSQL JDBC Driver Registered!");}

        try {
        connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e1) {
            if(notes){System.out.println("Connection Failed! Check output console");}
        e1.printStackTrace();
        }

        if (connection != null) {
            if(notes){System.out.println("Connection to database confirmed!");}
        } else {
                if(notes){System.out.println("Failed to make connection!");}
        }
    }

    public void Close(boolean notes){
        try {
            connection.close();
            if(notes){System.out.println("Connection closed.");}
        } catch (SQLException e) {
            e.printStackTrace();
                if(notes){System.out.println("Connection couldn't be closed.");}
        }
    }


}
