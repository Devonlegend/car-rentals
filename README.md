# car-rentals
building a java app to manage car rentals
https://grok.com/share/bGVnYWN5_e0538beb-e901-4da1-91a0-2010f42254fe

INSERT INTO users (username, password, name) VALUES ('testuser', 'testpass', 'Test User')
ON CONFLICT (username) DO NOTHING;  -- Avoid duplicates

INSERT INTO cars (name, model, year, description) VALUES 
('Toyota Camry', 'LE', 2023, 'A reliable sedan with good fuel efficiency.'),
('Ford Mustang', 'GT', 2024, 'High-performance sports car with powerful engine.');

package carapp; // Adjust to your package

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/carapp";
    private static final String USER = "postgres";
    private static final String PASS = "admin"; // Change to your PostgreSQL password

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // Hashing removed; use plain text passwords
}
