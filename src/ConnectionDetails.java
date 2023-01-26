import java.sql.*;

public class ConnectionDetails {

    public static java.sql.Connection con;
    public static PreparedStatement pst;

    public static void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost/Exchange", "postgres", "admin");
            System.out.println("Connection success");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            System.out.println("Connection failed");
        }
    }
}
