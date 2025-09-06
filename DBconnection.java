import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/carrentaldb";
    private static final String USER = "postgres";
    private static final String PASS = "postgrespass"; // Your password

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}
