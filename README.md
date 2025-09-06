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


package carapp; // Adjust to your package

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;

public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        initComponents();
        setupEventListeners();
        ((CardLayout)cardPanel.getLayout()).show(cardPanel, "login"); // Start on login
    }

    private void setupEventListeners() {
        // Navigation: Login to Register
        registerNavBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)cardPanel.getLayout();
                cl.show(cardPanel, "register");
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        // Navigation: Register to Login
        loginNavBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)cardPanel.getLayout();
                cl.show(cardPanel, "login");
                nameField.setText("");
                regUsernameField.setText("");
                regPasswordField.setText("");
            }
        });

        // Navigation: Car Search to About
        aboutNavBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)cardPanel.getLayout();
                cl.show(cardPanel, "about");
            }
        });

        // Navigation: About to Login
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)cardPanel.getLayout();
                cl.show(cardPanel, "login");
            }
        });
    }

    // Login Event (No hashing)
    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT name FROM users WHERE username = ? AND password = ?")) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String userName = rs.getString("name");
                        JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + userName + ".");
                        CardLayout cl = (CardLayout)cardPanel.getLayout();
                        cl.show(cardPanel, "carSearch");
                        usernameField.setText("");
                        passwordField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password!");
                        passwordField.setText("");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            } finally {
                try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    // Register Event (No hashing)
    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String name = nameField.getText().trim();
        String username = regUsernameField.getText().trim();
        String password = new String(regPasswordField.getPassword()).trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (name, username, password) VALUES (?, ?, ?)")) {
                pstmt.setString(1, name);
                pstmt.setString(2, username);
                pstmt.setString(3, password);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
                    CardLayout cl = (CardLayout)cardPanel.getLayout();
                    cl.show(cardPanel, "login");
                    nameField.setText("");
                    regUsernameField.setText("");
                    regPasswordField.setText("");
                }
            } catch (SQLException ex) {
                if (ex.getMessage().contains("duplicate key") || ex.getMessage().contains("unique")) {
                    JOptionPane.showMessageDialog(this, "Username already exists! Please choose another.");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration error: " + ex.getMessage());
                }
            } finally {
                try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    // Search Event (Unchanged)
    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String carName = carNameField.getText().trim();

        if (carName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a car name!");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT model, year, description FROM cars WHERE name ILIKE ?")) {
                pstmt.setString(1, "%" + carName + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        modelArea.setText(rs.getString("model"));
                        yearArea.setText(String.valueOf(rs.getInt("year")));
                        descArea.setText(rs.getString("description"));
                    } else {
                        JOptionPane.showMessageDialog(this, "Car not found!");
                        modelArea.setText("");
                        yearArea.setText("");
                        descArea.setText("");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
            } finally {
                try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
            }
        }
        carNameField.setText(""); // Clear search field
    }

    // New: Add Car Event
    private void addCarBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String name = addCarNameField.getText().trim();
        String model = addModelField.getText().trim();
        String yearStr = addYearField.getText().trim();
        String description = addDescArea.getText().trim();

        if (name.isEmpty() || model.isEmpty() || yearStr.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields to add a car!");
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
            if (year < 1900 || year > 2100) {  // Basic validation
                JOptionPane.showMessageDialog(this, "Year must be between 1900 and 2100!");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a valid integer!");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO cars (name, model, year, description) VALUES (?, ?, ?, ?)")) {
                pstmt.setString(1, name);
                pstmt.setString(2, model);
                pstmt.setInt(3, year);
                pstmt.setString(4, description);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Car added successfully! You can now search for it.");
                    // Clear add fields
                    addCarNameField.setText("");
                    addModelField.setText("");
                    addYearField.setText("");
                    addDescArea.setText("");
                }
            } catch (SQLException ex) {
                if (ex.getMessage().contains("duplicate key")) {
                    JOptionPane.showMessageDialog(this, "A car with this name already exists!");
                } else {
                    JOptionPane.showMessageDialog(this, "Add car error: " + ex.getMessage());
                }
            } finally {
                try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        // Auto-generated by NetBeans based on drag-and-drop. Includes all components now, e.g.,
        // addCarNameField = new javax.swing.JTextField();
        // addCarBtn = new javax.swing.JButton();
        // ... (login, register, search, about components)
        // getContentPane().add(cardPanel, java.awt.BorderLayout.CENTER);
        // pack();
        // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        // setTitle("Car App");
    }// </editor-fold>

    // Event stubs: Ensure in Design view, right-click addCarBtn > Events > Action > actionPerformed to link addCarBtnActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - Do not modify
    // Generated: e.g., private javax.swing.JButton addCarBtn; private javax.swing.JTextField addCarNameField; etc.
    // ... (all components)
}
